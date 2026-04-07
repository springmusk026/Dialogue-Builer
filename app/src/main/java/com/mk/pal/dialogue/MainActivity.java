package com.mk.pal.dialogue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mk.pal.dialogue.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBuilderScreen.setOnClickListener(v ->
                startActivity(new Intent(this, AlertDialogBuilderActivity.class)));

        // ═══════════════════════════════════════════
        //  PRESET DIALOGS
        // ═══════════════════════════════════════════

        // --- Alert ---
        binding.btnAlert.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Alert", "This is a simple alert dialog with an OK button.")
                        .setOnDismissListener(d -> setResultText("Alert dismissed"))
                        .show()
        );

        // --- Info ---
        binding.btnInfo.setOnClickListener(v ->
                DialogueBuilder.info(this, "Information",
                        "Here is some important information for you to know. " +
                        "This dialog is meant for displaying informational content to the user.")
                        .setOnDismissListener(d -> setResultText("Info dismissed"))
                        .show()
        );

        // --- Input ---
        binding.btnInput.setOnClickListener(v ->
                new DialogueBuilder(this)
                        .setTitle("Enter Name")
                        .setMessage("Please enter your name below:")
                        .addInput(new DialogueInput(DialogueInput.Type.TEXT, "Your name") {{
                            validator = s -> s.trim().isEmpty() ? "Name cannot be empty" : null;
                        }})
                        .addButton("OK", DialogueAction.Style.PRIMARY, v2 -> {})
                        .addButton("Cancel", DialogueAction.Style.PRIMARY, null)
                        .setOnDismissListener(d -> {
                            Map<Integer, String> results = d.getInputResults();
                            if (!results.isEmpty()) {
                                setResultText("Input: " + results.values().iterator().next());
                            } else {
                                setResultText("Input cancelled");
                            }
                        })
                        .show()
        );

        // --- Confirm ---
        binding.btnConfirm.setOnClickListener(v ->
                DialogueBuilder.confirm(this, "Confirm Action",
                        "Do you want to proceed with this action?",
                        v2 -> setResultText("You confirmed!"))
                        .show()
        );

        // --- Destructive Confirm ---
        binding.btnDestructive.setOnClickListener(v ->
                DialogueBuilder.destructiveConfirm(this, "Delete Item",
                        "This action cannot be undone. Are you sure?",
                        v2 -> setResultText("Item deleted!"))
                        .setAnimation(new DialogueAnimation() {{
                            enterType = DialogueAnimation.Type.SCALE;
                            exitType = DialogueAnimation.Type.SCALE;
                        }})
                        .show()
        );

        // --- Loading ---
        binding.btnLoading.setOnClickListener(v -> {
            Dialogue dialogue = DialogueBuilder.loading(this, "Loading...")
                    .show();
            new Handler(Looper.getMainLooper()).postDelayed(dialogue::dismiss, 2000);
            setResultText("Loading... auto-dismisses in 2s");
        });

        // ═══════════════════════════════════════════
        //  CUSTOM STYLING
        // ═══════════════════════════════════════════

        // --- Dark Theme ---
        binding.btnDarkTheme.setOnClickListener(v -> {
            DialogueStyle dark = new DialogueStyle();
            dark.backgroundColor = Color.parseColor("#1E1E1E");
            dark.titleColor = Color.parseColor("#FFFFFF");
            dark.messageColor = Color.parseColor("#B0B0B0");
            dark.inputTextColor = Color.parseColor("#FFFFFF");
            dark.primaryButtonBg = Color.parseColor("#4FC3F7");
            dark.primaryButtonTextColor = Color.parseColor("#1E1E1E");
            dark.secondaryButtonBg = Color.parseColor("#424242");
            dark.secondaryButtonTextColor = Color.parseColor("#E0E0E0");
            dark.destructiveButtonBg = Color.parseColor("#EF5350");
            dark.destructiveButtonTextColor = Color.WHITE;
            dark.neutralButtonBg = Color.parseColor("#424242");
            dark.neutralButtonTextColor = Color.parseColor("#E0E0E0");
            dark.dividerColor = Color.parseColor("#333333");

            new DialogueBuilder(this)
                    .setTitle("Dark Mode")
                    .setMessage("This dialog uses a custom dark theme style.")
                    .addButton("Got it", DialogueAction.Style.PRIMARY, null)
                    .addButton("Dismiss", DialogueAction.Style.NEUTRAL, null)
                    .setStyle(dark)
                    .show();
        });

        // --- Custom Button Styles ---
        binding.btnColoredButtons.setOnClickListener(v -> {
            DialogueStyle s = new DialogueStyle();
            s.primaryButtonBg = Color.parseColor("#4CAF50");
            s.primaryButtonTextColor = Color.WHITE;

            new DialogueBuilder(this)
                    .setTitle("Custom Buttons")
                    .setMessage("Each button can have its own color scheme.")
                    .addButton("Accept", DialogueAction.Style.PRIMARY, v2 -> setResultText("Accepted!"))
                    .addButton("Decline", DialogueAction.Style.DESTRUCTIVE, v2 -> setResultText("Declined"))
                    .addButton("Later", DialogueAction.Style.SECONDARY, v2 -> setResultText("Maybe later"))
                    .setStyle(s)
                    .show();
        });

        // --- Custom Layout ---
        binding.btnCustomLayout.setOnClickListener(v -> {
            DialogueLayout layout = new DialogueLayout();
            layout.padding = 12f;
            layout.contentGravity = DialogueLayout.ContentGravity.CENTER;

            new DialogueBuilder(this)
                    .setTitle("Centered & Compact")
                    .setMessage("This dialog has smaller padding and centered content.")
                    .addButton("OK", DialogueAction.Style.PRIMARY, null)
                    .setLayout(layout)
                    .show();
        });

        // ═══════════════════════════════════════════
        //  PLATFORM-STYLED DIALOGS
        // ═══════════════════════════════════════════

        // --- Apple / iOS Style ---
        binding.btnApple.setOnClickListener(v -> showAppleStyle());

        // --- Samsung OneUI Style ---
        binding.btnSamsung.setOnClickListener(v -> showSamsungStyle());

        // --- Material Design 3 Style ---
        binding.btnMaterial3.setOnClickListener(v -> showMaterial3Style());

        // --- Minimal / Outline Style ---
        binding.btnMinimal.setOnClickListener(v -> showMinimalStyle());

        // ═══════════════════════════════════════════
        //  RICH CONTENT
        // ═══════════════════════════════════════════

        // --- Rich text (HTML) ---
        binding.btnRichText.setOnClickListener(v -> {
            Spanned html = Html.fromHtml(
                    "<b>Bold text</b>, <i>italic text</i>, and <u>underlined</u>.<br/><br/>" +
                    "You can use <font color='#1976D2'>colored text</font> and " +
                    "<font color='#D32F2F'>error text</font>.<br/><br/>" +
                    "<ul><li>Point one</li><li>Point two</li><li>Point three</li></ul>",
                    Html.FROM_HTML_MODE_COMPACT);

            TextView richTv = new TextView(this);
            richTv.setText(html);
            richTv.setMovementMethod(LinkMovementMethod.getInstance());
            richTv.setTextSize(15);
            richTv.setTextColor(Color.parseColor("#333333"));
            richTv.setPadding(dp(24), dp(16), dp(24), dp(16));

            new DialogueBuilder(this)
                    .setTitle("Rich Text Content")
                    .setMessage("This title appears above the rich content.")
                    .setCustomView(richTv)
                    .addButton("Close", DialogueAction.Style.NEUTRAL, null)
                    .show();
        });

        // --- Image (from URL) ---
        binding.btnImage.setOnClickListener(v -> {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(200)));
            imageView.setBackgroundColor(Color.parseColor("#F5F5F5"));

            Glide.with(this)
                    .load("https://picsum.photos/seed/dialogue/600/300")
                    .placeholder(new GradientDrawable() {{
                        setShape(GradientDrawable.RECTANGLE);
                        setColor(Color.parseColor("#E0E0E0"));
                    }})
                    .into(imageView);

            new DialogueBuilder(this)
                    .setTitle("Image from URL")
                    .setMessage("Image loaded asynchronously via Glide.")
                    .setCustomView(imageView)
                    .addButton("Close", DialogueAction.Style.NEUTRAL, null)
                    .show();
        });

        // --- WebView ---
        binding.btnWebview.setOnClickListener(v -> {
            WebView webView = new WebView(this);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.setWebViewClient(new WebViewClient());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(300));
            webView.setLayoutParams(lp);
            webView.loadUrl("https://www.google.com");

            new DialogueBuilder(this)
                    .setTitle("Web Content")
                    .setCustomView(webView)
                    .addButton("Close", DialogueAction.Style.NEUTRAL, null)
                    .setLayout(new DialogueLayout() {{ padding = 8f; }})
                    .show();
        });

        // --- Custom View (LinearLayout) ---
        binding.btnCustomView.setOnClickListener(v -> {
            LinearLayout custom = new LinearLayout(this);
            custom.setOrientation(LinearLayout.VERTICAL);
            custom.setPadding(dp(16), dp(16), dp(16), dp(16));

            TextView label = new TextView(this);
            label.setText("Custom LinearLayout");
            label.setTextSize(16);
            label.setTextColor(Color.parseColor("#333333"));
            label.setPadding(0, 0, 0, dp(12));
            custom.addView(label);

            TextView desc = new TextView(this);
            desc.setText("This entire content area is a programmatically built LinearLayout. " +
                    "You can add any views you want: images, inputs, lists, etc.");
            desc.setTextSize(13);
            desc.setTextColor(Color.parseColor("#666666"));
            custom.addView(desc);

            View divider = new View(this);
            divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
            LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(1));
            dlp.topMargin = dp(12);
            dlp.bottomMargin = dp(12);
            divider.setLayoutParams(dlp);
            custom.addView(divider);

            TextView footer = new TextView(this);
            footer.setText("This is a footer inside the custom view.");
            footer.setTextSize(12);
            footer.setTextColor(Color.parseColor("#999999"));
            custom.addView(footer);

            new DialogueBuilder(this)
                    .setTitle("Custom View")
                    .setCustomView(custom)
                    .addButton("OK", DialogueAction.Style.PRIMARY, null)
                    .show();
        });

        // --- Multi-Input Form ---
        binding.btnMultiInput.setOnClickListener(v ->
                new DialogueBuilder(this)
                        .setTitle("Registration Form")
                        .setMessage("Fill in the fields below:")
                        .addInput(new DialogueInput(DialogueInput.Type.TEXT, "Full Name") {{
                            validator = s -> s.trim().isEmpty() ? "Name is required" : null;
                        }})
                        .addInput(new DialogueInput(DialogueInput.Type.EMAIL, "Email Address") {{
                            validator = s -> !s.contains("@") ? "Invalid email" : null;
                        }})
                        .addInput(new DialogueInput(DialogueInput.Type.PASSWORD, "Password") {{
                            validator = s -> s.length() < 6 ? "Min 6 characters" : null;
                        }})
                        .addButton("Submit", DialogueAction.Style.PRIMARY, v2 -> {})
                        .addButton("Cancel", DialogueAction.Style.SECONDARY, null)
                        .setOnDismissListener(d -> {
                            Map<Integer, String> results = d.getInputResults();
                            if (results.size() >= 3) {
                                setResultText("Registered: " + results.get(0));
                            } else {
                                setResultText("Registration cancelled");
                            }
                        })
                        .show()
        );

        // --- Checkbox List ---
        binding.btnCheckboxList.setOnClickListener(v -> {
            String[] items = {"WiFi", "Bluetooth", "Location", "Notifications", "Auto-sync"};
            boolean[] checked = {true, false, true, false, false};

            LinearLayout checkboxLayout = new LinearLayout(this);
            checkboxLayout.setOrientation(LinearLayout.VERTICAL);
            checkboxLayout.setPadding(dp(16), dp(8), dp(16), dp(8));

            for (int i = 0; i < items.length; i++) {
                CheckBox cb = new CheckBox(this);
                cb.setText(items[i]);
                cb.setChecked(checked[i]);
                cb.setTextSize(15);
                cb.setTextColor(Color.parseColor("#333333"));
                cb.setPadding(0, dp(8), 0, dp(8));
                cb.setTag(i);
                checkboxLayout.addView(cb);
            }

            new DialogueBuilder(this)
                    .setTitle("Select Preferences")
                    .setCustomView(checkboxLayout)
                    .addButton("Save", DialogueAction.Style.PRIMARY, v2 -> {
                        StringBuilder sb = new StringBuilder("Checked: ");
                        for (int i = 0; i < checkboxLayout.getChildCount(); i++) {
                            CheckBox cb = (CheckBox) checkboxLayout.getChildAt(i);
                            if (cb.isChecked()) sb.append(cb.getText()).append(", ");
                        }
                        setResultText(sb.toString());
                    })
                    .addButton("Cancel", DialogueAction.Style.SECONDARY, null)
                    .show();
        });

        // --- Rating ---
        binding.btnRating.setOnClickListener(v -> {
            LinearLayout rateLayout = new LinearLayout(this);
            rateLayout.setOrientation(LinearLayout.VERTICAL);
            rateLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            rateLayout.setPadding(dp(24), dp(16), dp(24), dp(16));

            TextView msg = new TextView(this);
            msg.setText("How would you rate your experience?");
            msg.setTextSize(15);
            msg.setTextColor(Color.parseColor("#555555"));
            msg.setGravity(Gravity.CENTER);
            rateLayout.addView(msg);

            RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(1f);
            ratingBar.setRating(3f);
            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlp.topMargin = dp(12);
            ratingBar.setLayoutParams(rlp);
            rateLayout.addView(ratingBar);

            new DialogueBuilder(this)
                    .setTitle("Rate Us")
                    .setCustomView(rateLayout)
                    .addButton("Submit", DialogueAction.Style.PRIMARY, v2 ->
                            setResultText("Rating: " + ratingBar.getRating() + "/5"))
                    .addButton("Skip", DialogueAction.Style.NEUTRAL, null)
                    .show();
        });

        // --- Single-Choice List ---
        binding.btnList.setOnClickListener(v -> {
            String[] items = {"Option A", "Option B", "Option C", "Option D"};

            LinearLayout listLayout = new LinearLayout(this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setPadding(dp(16), dp(8), dp(16), dp(8));

            final RadioGroup rg = new RadioGroup(this);
            for (int i = 0; i < items.length; i++) {
                RadioButton rb = new RadioButton(this);
                rb.setText(items[i]);
                rb.setTextSize(15);
                rb.setTextColor(Color.parseColor("#333333"));
                rb.setId(i);
                rg.addView(rb);
            }
            listLayout.addView(rg);

            new DialogueBuilder(this)
                    .setTitle("Choose an Option")
                    .setCustomView(listLayout)
                    .addButton("Select", DialogueAction.Style.PRIMARY, v2 -> {
                        int id = rg.getCheckedRadioButtonId();
                        if (id >= 0) {
                            setResultText("Selected: " + items[id]);
                        } else {
                            setResultText("Nothing selected");
                        }
                    })
                    .addButton("Cancel", DialogueAction.Style.SECONDARY, null)
                    .show();
        });

        // --- Progress Bar Dialog ---
        binding.btnProgress.setOnClickListener(v -> {
            LinearLayout progressLayout = new LinearLayout(this);
            progressLayout.setOrientation(LinearLayout.VERTICAL);
            progressLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            progressLayout.setPadding(dp(24), dp(24), dp(24), dp(24));

            TextView statusText = new TextView(this);
            statusText.setText("Uploading: 0%");
            statusText.setTextSize(16);
            statusText.setTextColor(Color.parseColor("#333333"));
            progressLayout.addView(statusText);

            final int[] progress = {0};
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            LinearLayout.LayoutParams plp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(8));
            plp.topMargin = dp(16);
            progressBar.setLayoutParams(plp);
            progressLayout.addView(progressBar);

            final Handler handler = new Handler(Looper.getMainLooper());
            final Dialogue[] uploadDialog = new Dialogue[1];
            final Runnable[] tickRef = new Runnable[1];

            uploadDialog[0] = new DialogueBuilder(this)
                    .setTitle("File Upload")
                    .setCustomView(progressLayout)
                    .setCancelable(false)
                    .setOnDismissListener(d -> {
                        if (tickRef[0] != null) {
                            handler.removeCallbacks(tickRef[0]);
                        }
                    })
                    .show();

            tickRef[0] = new Runnable() {
                @Override public void run() {
                    progress[0] = Math.min(progress[0] + 10, 100);
                    progressBar.setProgress(progress[0]);
                    statusText.setText("Uploading: " + progress[0] + "%");

                    if (progress[0] >= 100) {
                        if (uploadDialog[0] != null && uploadDialog[0].isShowing()) {
                            uploadDialog[0].dismiss();
                        }
                        setResultText("Upload complete!");
                        return;
                    }

                    handler.postDelayed(this, 200);
                }
            };
            handler.postDelayed(tickRef[0], 200);
        });

        // --- Color Picker ---
        binding.btnColorPicker.setOnClickListener(v -> {
            int[] colors = {
                    Color.parseColor("#F44336"), Color.parseColor("#E91E63"),
                    Color.parseColor("#9C27B0"), Color.parseColor("#673AB7"),
                    Color.parseColor("#3F51B5"), Color.parseColor("#2196F3"),
                    Color.parseColor("#00BCD4"), Color.parseColor("#4CAF50"),
                    Color.parseColor("#FF9800"), Color.parseColor("#FF5722"),
            };
            int[] selectedColor = {-1};

            LinearLayout colorLayout = new LinearLayout(this);
            colorLayout.setOrientation(LinearLayout.VERTICAL);
            colorLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            colorLayout.setPadding(dp(16), dp(8), dp(16), dp(16));

            LinearLayout row1 = new LinearLayout(this);
            row1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout row2 = new LinearLayout(this);
            row2.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < colors.length; i++) {
                View swatch = new View(this);
                int size = dp(40);
                GradientDrawable bg = new GradientDrawable();
                bg.setShape(GradientDrawable.OVAL);
                bg.setColor(colors[i]);
                swatch.setBackground(bg);
                LinearLayout.LayoutParams slp = new LinearLayout.LayoutParams(size, size);
                slp.setMargins(dp(6), dp(6), dp(6), dp(6));
                swatch.setLayoutParams(slp);
                final int idx = i;
                swatch.setOnClickListener(v2 -> {
                    selectedColor[0] = colors[idx];
                    setResultText("Color: #" + Integer.toHexString(colors[idx]).substring(2));
                });

                if (i < 5) row1.addView(swatch);
                else row2.addView(swatch);
            }

            colorLayout.addView(row1);
            colorLayout.addView(row2);

            new DialogueBuilder(this)
                    .setTitle("Pick a Color")
                    .setMessage("Tap a color to select it.")
                    .setCustomView(colorLayout)
                    .addButton("Done", DialogueAction.Style.PRIMARY, null)
                    .show();
        });

        // ═══════════════════════════════════════════
        //  CALLBACK DEMOS
        // ═══════════════════════════════════════════

        // --- onShow + onDismiss ---
        binding.btnShowCb.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Callback Demo", "Watch the result text for lifecycle callbacks.")
                        .setOnShowListener(d -> setResultText("Dialog SHOWN"))
                        .setOnDismissListener(d -> setResultText("Dialog DISMISSED"))
                        .show()
        );

        // --- onAction ---
        binding.btnActionCb.setOnClickListener(v ->
                new DialogueBuilder(this)
                        .setTitle("Action Tracking")
                        .setMessage("Tap any button to see which action index was triggered.")
                        .addButton("First", DialogueAction.Style.PRIMARY, null)
                        .addButton("Second", DialogueAction.Style.NEUTRAL, null)
                        .addButton("Third", DialogueAction.Style.SECONDARY, null)
                        .setOnActionListener((dialogue, action, index) ->
                                setResultText("Clicked action[" + index + "]: \"" + action.text + "\""))
                        .show()
        );

        // --- onInputResult (validated) ---
        binding.btnInputCb.setOnClickListener(v -> {
            DialogueInput name = new DialogueInput("Username");
            name.validator = s -> s.length() < 3 ? "Min 3 chars" : null;

            DialogueInput email = new DialogueInput(DialogueInput.Type.EMAIL, "Email");
            email.validator = s -> !s.contains("@") ? "Invalid email" : null;

            new DialogueBuilder(this)
                    .setTitle("Validated Input")
                    .addInput(name)
                    .addInput(email)
                    .addButton("Submit", DialogueAction.Style.PRIMARY, null)
                    .addButton("Cancel", DialogueAction.Style.SECONDARY, null)
                    .setOnInputResultListener((dialogue, results) ->
                            setResultText("Valid inputs: " + results))
                    .show();
        });

        // ═══════════════════════════════════════════
        //  ANIMATION DEMOS
        // ═══════════════════════════════════════════

        binding.btnAnimFade.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Fade", "This dialog fades in and out.")
                        .setAnimation(new DialogueAnimation() {{
                            enterType = DialogueAnimation.Type.FADE;
                            exitType = DialogueAnimation.Type.FADE;
                            duration = 400;
                        }})
                        .show()
        );

        binding.btnAnimSlide.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Slide Up", "This dialog slides up on enter, slides down on exit.")
                        .setAnimation(new DialogueAnimation() {{
                            enterType = DialogueAnimation.Type.SLIDE_UP;
                            exitType = DialogueAnimation.Type.SLIDE_DOWN;
                            duration = 300;
                        }})
                        .show()
        );

        binding.btnAnimScale.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Scale", "This dialog scales in and out.")
                        .setAnimation(new DialogueAnimation() {{
                            enterType = DialogueAnimation.Type.SCALE;
                            exitType = DialogueAnimation.Type.SCALE;
                            duration = 250;
                        }})
                        .show()
        );

        binding.btnAnimNone.setOnClickListener(v ->
                DialogueBuilder.alert(this, "Instant", "No animation — dialog appears instantly.")
                        .setAnimation(new DialogueAnimation() {{
                            enterType = DialogueAnimation.Type.NONE;
                            exitType = DialogueAnimation.Type.NONE;
                        }})
                        .show()
        );
    }

    // ═══════════════════════════════════════════
    //  PLATFORM-STYLED DIALOG IMPLEMENTATIONS
    // ═══════════════════════════════════════════

    /**
     * Apple / iOS-style alert dialog
     * - Small corner radius (~13dp)
     * - Blurred/white background
     * - Horizontal buttons with equal width and a vertical divider between them
     * - Centered text
     * - Blue primary action
     */
    private void showAppleStyle() {
        DialogueStyle ios = new DialogueStyle();
        ios.backgroundColor = Color.parseColor("#F2F2F7");
        ios.overlayColor = Color.parseColor("#40000000");
        ios.cornerRadius = 14f;
        ios.elevation = 10f;
        ios.titleColor = Color.parseColor("#000000");
        ios.titleTextSize = 17f;
        ios.messageColor = Color.parseColor("#3C3C43");
        ios.messageTextSize = 13f;
        ios.primaryButtonBg = Color.parseColor("#007AFF");
        ios.primaryButtonTextColor = Color.WHITE;
        ios.secondaryButtonBg = Color.parseColor("#F2F2F7");
        ios.secondaryButtonTextColor = Color.parseColor("#007AFF");
        ios.destructiveButtonBg = Color.parseColor("#F2F2F7");
        ios.destructiveButtonTextColor = Color.parseColor("#FF3B30");
        ios.neutralButtonBg = Color.parseColor("#F2F2F7");
        ios.neutralButtonTextColor = Color.parseColor("#007AFF");
        ios.buttonCornerRadius = 0f;
        ios.buttonPadding = 16;
        ios.buttonSpacing = 0;
        ios.dividerColor = Color.parseColor("#C6C6C8");

        DialogueLayout iosLayout = new DialogueLayout();
        iosLayout.padding = 20f;
        iosLayout.contentGravity = DialogueLayout.ContentGravity.CENTER;

        DialogueAnimation iosAnim = new DialogueAnimation();
        iosAnim.enterType = DialogueAnimation.Type.SCALE;
        iosAnim.exitType = DialogueAnimation.Type.SCALE;
        iosAnim.duration = 200;

        final Dialogue[] holder = new Dialogue[1];

        LinearLayout custom = createIosButtonRow(
                "Cancel", v -> setResultText("Cancelled (iOS)"),
                "OK", v -> {
                    setResultText("Confirmed (iOS)");
                    if (holder[0] != null) holder[0].dismiss();
                }
        );

        DialogueBuilder b = new DialogueBuilder(this)
                .setTitle("Allow \"Dialogue\" to access Photos?")
                .setMessage("This app would like to view your photos. Your photos are important and we want you to know that.")
                .setStyle(ios)
                .setLayout(iosLayout)
                .setAnimation(iosAnim)
                .setCustomView(custom);
        holder[0] = b.show();
    }

    /**
     * Creates a horizontal two-button layout mimicking iOS alert style.
     */
    private LinearLayout createIosButtonRow(String leftText, View.OnClickListener leftClick,
                                            String rightText, View.OnClickListener rightClick) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.topMargin = dp(8);
        row.setLayoutParams(rlp);

        Button leftBtn = new Button(this, null, android.R.attr.borderlessButtonStyle);
        leftBtn.setText(leftText);
        leftBtn.setTextSize(17);
        leftBtn.setTextColor(Color.parseColor("#007AFF"));
        leftBtn.setBackground(null);
        leftBtn.setAllCaps(false);
        leftBtn.setPadding(0, dp(10), 0, dp(10));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, dp(44), 1f);
        leftBtn.setLayoutParams(llp);
        leftBtn.setGravity(Gravity.CENTER);
        leftBtn.setOnClickListener(v -> {
            setResultText("Cancelled (iOS)");
            leftClick.onClick(v);
        });

        View divider = new View(this);
        divider.setBackgroundColor(Color.parseColor("#C6C6C8"));
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(dp(1), ViewGroup.LayoutParams.MATCH_PARENT);
        divider.setLayoutParams(dlp);

        Button rightBtn = new Button(this, null, android.R.attr.borderlessButtonStyle);
        rightBtn.setText(rightText);
        rightBtn.setTextSize(17);
        rightBtn.setTextColor(Color.parseColor("#007AFF"));
        rightBtn.setBackground(null);
        rightBtn.setAllCaps(false);
        rightBtn.setPadding(0, dp(10), 0, dp(10));
        LinearLayout.LayoutParams rlp2 = new LinearLayout.LayoutParams(0, dp(44), 1f);
        rightBtn.setLayoutParams(rlp2);
        rightBtn.setGravity(Gravity.CENTER);
        rightBtn.setOnClickListener(rightClick);

        row.addView(leftBtn);
        row.addView(divider);
        row.addView(rightBtn);
        return row;
    }

    /**
     * Samsung OneUI-style dialog
     * - Large corner radius (~28dp)
     * - Light gray background
     * - Vertically stacked full-width buttons with rounded backgrounds
     * - Left-aligned content
     * - Teal primary action color
     * - Generous padding
     */
    private void showSamsungStyle() {
        DialogueStyle oneui = new DialogueStyle();
        oneui.backgroundColor = Color.parseColor("#FAFAFA");
        oneui.overlayColor = Color.parseColor("#50000000");
        oneui.cornerRadius = 28f;
        oneui.elevation = 6f;
        oneui.titleColor = Color.parseColor("#1A1A1A");
        oneui.titleTextSize = 19f;
        oneui.messageColor = Color.parseColor("#555555");
        oneui.messageTextSize = 14f;
        oneui.primaryButtonBg = Color.parseColor("#0073CF");
        oneui.primaryButtonTextColor = Color.WHITE;
        oneui.secondaryButtonBg = Color.parseColor("#F0F0F0");
        oneui.secondaryButtonTextColor = Color.parseColor("#333333");
        oneui.destructiveButtonBg = Color.parseColor("#E53935");
        oneui.destructiveButtonTextColor = Color.WHITE;
        oneui.neutralButtonBg = Color.parseColor("#F0F0F0");
        oneui.neutralButtonTextColor = Color.parseColor("#333333");
        oneui.buttonCornerRadius = 24f;
        oneui.buttonPadding = 14;
        oneui.buttonSpacing = 6;
        oneui.dividerColor = Color.parseColor("#E8E8E8");

        DialogueLayout oneuiLayout = new DialogueLayout();
        oneuiLayout.padding = 28f;
        oneuiLayout.contentGravity = DialogueLayout.ContentGravity.LEFT;

        DialogueAnimation oneuiAnim = new DialogueAnimation();
        oneuiAnim.enterType = DialogueAnimation.Type.FADE;
        oneuiAnim.exitType = DialogueAnimation.Type.FADE;
        oneuiAnim.duration = 200;

        final Dialogue[] holder = new Dialogue[1];

        LinearLayout custom = createSamsungButtonRow(
                "Cancel", DialogueAction.Style.SECONDARY, v -> setResultText("Cancelled (Samsung)"),
                "Allow", DialogueAction.Style.PRIMARY, v -> {
                    setResultText("Allowed (Samsung)");
                    if (holder[0] != null) holder[0].dismiss();
                }
        );

        DialogueBuilder b = new DialogueBuilder(this)
                .setTitle("Permission request")
                .setMessage("Allow this app to access your location data? This helps the app provide location-based services.")
                .setStyle(oneui)
                .setLayout(oneuiLayout)
                .setAnimation(oneuiAnim)
                .setCustomView(custom);
        holder[0] = b.show();
    }

    /**
     * Creates vertically stacked full-width buttons (Samsung style).
     */
    private LinearLayout createSamsungButtonRow(String topText, DialogueAction.Style topStyle,
                                                 View.OnClickListener topClick,
                                                 String bottomText, DialogueAction.Style bottomStyle,
                                                 View.OnClickListener bottomClick) {
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        clp.topMargin = dp(8);
        col.setLayoutParams(clp);

        Button topBtn = new Button(this);
        topBtn.setText(topText);
        topBtn.setTextSize(15);
        topBtn.setTextColor(getSamsungButtonColor(topStyle));
        GradientDrawable topBg = ViewFactory.createRoundedBackground(this, getSamsungButtonBg(topStyle), 24);
        topBtn.setBackground(topBg);
        topBtn.setPadding(0, dp(12), 0, dp(12));
        topBtn.setAllCaps(false);
        topBtn.setOnClickListener(v -> {
            setResultText("Cancelled (Samsung)");
            topClick.onClick(v);
        });

        View spacer = new View(this);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(6)));

        Button bottomBtn = new Button(this);
        bottomBtn.setText(bottomText);
        bottomBtn.setTextSize(15);
        bottomBtn.setTextColor(getSamsungButtonColor(bottomStyle));
        GradientDrawable bottomBg = ViewFactory.createRoundedBackground(this, getSamsungButtonBg(bottomStyle), 24);
        bottomBtn.setBackground(bottomBg);
        bottomBtn.setPadding(0, dp(12), 0, dp(12));
        bottomBtn.setAllCaps(false);
        bottomBtn.setOnClickListener(bottomClick);

        col.addView(topBtn);
        col.addView(spacer);
        col.addView(bottomBtn);
        return col;
    }

    private int getSamsungButtonBg(DialogueAction.Style style) {
        switch (style) {
            case PRIMARY:  return Color.parseColor("#0073CF");
            default:       return Color.parseColor("#F0F0F0");
        }
    }

    private int getSamsungButtonColor(DialogueAction.Style style) {
        switch (style) {
            case PRIMARY:  return Color.WHITE;
            default:       return Color.parseColor("#333333");
        }
    }

    /**
     * Material Design 3-style dialog
     * - ~28dp corner radius
     * - Elevated surface with subtle shadow
     * - Text buttons (no filled backgrounds)
     * - Slightly tinted container
     */
    private void showMaterial3Style() {
        DialogueStyle md3 = new DialogueStyle();
        md3.backgroundColor = Color.parseColor("#FFF8F4");
        md3.overlayColor = Color.parseColor("#60000000");
        md3.cornerRadius = 28f;
        md3.elevation = 12f;
        md3.titleColor = Color.parseColor("#1C1B1F");
        md3.titleTextSize = 24f;
        md3.messageColor = Color.parseColor("#49454F");
        md3.messageTextSize = 14f;
        md3.primaryButtonBg = Color.parseColor("#FFF8F4");
        md3.primaryButtonTextColor = Color.parseColor("#0061A4");
        md3.secondaryButtonBg = Color.parseColor("#FFF8F4");
        md3.secondaryButtonTextColor = Color.parseColor("#0061A4");
        md3.destructiveButtonBg = Color.parseColor("#FFF8F4");
        md3.destructiveButtonTextColor = Color.parseColor("#BA1A1A");
        md3.neutralButtonBg = Color.parseColor("#FFF8F4");
        md3.neutralButtonTextColor = Color.parseColor("#0061A4");
        md3.buttonCornerRadius = 20f;
        md3.buttonPadding = 10;
        md3.buttonSpacing = 0;
        md3.dividerColor = Color.parseColor("#00000000");

        DialogueLayout md3Layout = new DialogueLayout();
        md3Layout.padding = 24f;
        md3Layout.contentGravity = DialogueLayout.ContentGravity.LEFT;

        DialogueAnimation md3Anim = new DialogueAnimation();
        md3Anim.enterType = DialogueAnimation.Type.SCALE;
        md3Anim.exitType = DialogueAnimation.Type.FADE;
        md3Anim.duration = 250;

        final Dialogue[] holder = new Dialogue[1];

        LinearLayout custom = createMd3ButtonRow(
                "Cancel", DialogueAction.Style.SECONDARY, v -> setResultText("Cancelled (M3)"),
                "Confirm", DialogueAction.Style.PRIMARY, v -> {
                    setResultText("Confirmed (M3)");
                    if (holder[0] != null) holder[0].dismiss();
                }
        );

        DialogueBuilder b = new DialogueBuilder(this)
                .setTitle("Use location services?")
                .setMessage("This app uses your location to provide nearby suggestions. You can change this in settings at any time.")
                .setStyle(md3)
                .setLayout(md3Layout)
                .setAnimation(md3Anim)
                .setCustomView(custom);
        holder[0] = b.show();
    }

    /**
     * Creates M3-style text buttons (no filled background, just colored text).
     */
    private LinearLayout createMd3ButtonRow(String leftText, DialogueAction.Style leftStyle,
                                             View.OnClickListener leftClick,
                                             String rightText, DialogueAction.Style rightStyle,
                                             View.OnClickListener rightClick) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.END);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.topMargin = dp(16);
        row.setLayoutParams(rlp);

        Button leftBtn = new Button(this, null, android.R.attr.borderlessButtonStyle);
        leftBtn.setText(leftText);
        leftBtn.setTextSize(14);
        leftBtn.setTextColor(getM3ButtonColor(leftStyle));
        leftBtn.setBackground(null);
        leftBtn.setAllCaps(false);
        leftBtn.setPadding(dp(12), dp(10), dp(12), dp(10));
        leftBtn.setOnClickListener(v -> {
            setResultText("Cancelled (M3)");
            leftClick.onClick(v);
        });

        Button rightBtn = new Button(this, null, android.R.attr.borderlessButtonStyle);
        rightBtn.setText(rightText);
        rightBtn.setTextSize(14);
        rightBtn.setTextColor(getM3ButtonColor(rightStyle));
        rightBtn.setBackground(null);
        rightBtn.setAllCaps(false);
        rightBtn.setPadding(dp(12), dp(10), dp(12), dp(10));
        rightBtn.setOnClickListener(rightClick);

        row.addView(leftBtn);
        row.addView(rightBtn);
        return row;
    }

    private int getM3ButtonColor(DialogueAction.Style style) {
        switch (style) {
            case DESTRUCTIVE: return Color.parseColor("#BA1A1A");
            case PRIMARY:     return Color.parseColor("#0061A4");
            default:          return Color.parseColor("#0061A4");
        }
    }

    /**
     * Minimal / Outline style dialog
     * - Sharp corners (0dp radius)
     * - Thin border stroke
     * - White background
     * - Minimal padding
     * - Subtle gray buttons with borders
     */
    private void showMinimalStyle() {
        DialogueStyle minimal = new DialogueStyle();
        minimal.backgroundColor = Color.WHITE;
        minimal.overlayColor = Color.parseColor("#20000000");
        minimal.cornerRadius = 4f;
        minimal.elevation = 2f;
        minimal.titleColor = Color.parseColor("#212121");
        minimal.titleTextSize = 16f;
        minimal.messageColor = Color.parseColor("#757575");
        minimal.messageTextSize = 13f;
        minimal.primaryButtonBg = Color.parseColor("#212121");
        minimal.primaryButtonTextColor = Color.WHITE;
        minimal.secondaryButtonBg = Color.WHITE;
        minimal.secondaryButtonTextColor = Color.parseColor("#757575");
        minimal.destructiveButtonBg = Color.WHITE;
        minimal.destructiveButtonTextColor = Color.parseColor("#D32F2F");
        minimal.neutralButtonBg = Color.WHITE;
        minimal.neutralButtonTextColor = Color.parseColor("#757575");
        minimal.buttonCornerRadius = 4f;
        minimal.buttonPadding = 10;
        minimal.buttonSpacing = 8;
        minimal.dividerColor = Color.parseColor("#E0E0E0");

        DialogueLayout minLayout = new DialogueLayout();
        minLayout.padding = 16f;
        minLayout.contentGravity = DialogueLayout.ContentGravity.LEFT;

        DialogueAnimation minAnim = new DialogueAnimation();
        minAnim.enterType = DialogueAnimation.Type.FADE;
        minAnim.exitType = DialogueAnimation.Type.FADE;
        minAnim.duration = 150;

        new DialogueBuilder(this)
                .setTitle("Minimal Dialog")
                .setMessage("Clean, minimal design with outline borders and no visual noise.")
                .addButton("OK", DialogueAction.Style.PRIMARY, v -> setResultText("OK (Minimal)"))
                .addButton("Cancel", DialogueAction.Style.SECONDARY, v -> setResultText("Cancel (Minimal)"))
                .setStyle(minimal)
                .setLayout(minLayout)
                .setAnimation(minAnim)
                .show();
    }

    // ═══════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════

    private void setResultText(String text) {
        binding.resultText.setText("Result: " + text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private int dp(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}
