package com.mk.pal.dialogue;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AlertDialogBuilderActivity extends AppCompatActivity {

    private enum CustomContentType {
        NONE,
        RICH_TEXT,
        IMAGE_URL,
        WEBVIEW_URL,
        PROGRESS,
        SIMPLE_LIST
    }

    private enum ValidatorPreset {
        NONE,
        REQUIRED,
        EMAIL,
        MIN_3,
        NUMERIC
    }

    private EditText etDialogTitle;
    private EditText etDialogMessage;
    private CheckBox cbCancelable;
    private CheckBox cbCancelableOutside;
    private LinearLayout actionsContainer;
    private LinearLayout inputsContainer;
    private EditText etBackgroundColor;
    private EditText etTitleColor;
    private EditText etMessageColor;
    private EditText etDividerColor;
    private EditText etTitleSize;
    private EditText etMessageSize;
    private EditText etInputTextSize;
    private EditText etCornerRadius;
    private EditText etButtonCornerRadius;
    private EditText etButtonPadding;
    private EditText etButtonSpacing;
    private EditText etPrimaryBg;
    private EditText etPrimaryText;
    private EditText etSecondaryBg;
    private EditText etSecondaryText;
    private EditText etDestructiveBg;
    private EditText etDestructiveText;
    private EditText etNeutralBg;
    private EditText etNeutralText;
    private Spinner spinnerContentGravity;
    private EditText etLayoutPadding;
    private EditText etMaxWidth;
    private Spinner spinnerEnterAnimation;
    private Spinner spinnerExitAnimation;
    private EditText etAnimationDuration;
    private Spinner spinnerCustomContent;
    private TextView tvCustomContentHelp;
    private EditText etCustomPrimary;
    private EditText etCustomSecondary;
    private EditText etCustomTertiary;
    private TextView tvBuilderOutput;
    private TextView tvGeneratedCode;
    private View builderRoot;
    private View builderContent;
    private View builderBottomBar;

    private final List<ActionRow> actionRows = new ArrayList<>();
    private final List<InputRow> inputRows = new ArrayList<>();
    private final List<String> callbackEvents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog_builder);
        setTitle("AlertDialog Builder");

        bindViews();
        applyWindowInsets();
        setupSpinners();
        setupStaticListeners();
        setupButtons();
        resetBuilder();
    }

    private void bindViews() {
        etDialogTitle = findViewById(R.id.et_dialog_title);
        etDialogMessage = findViewById(R.id.et_dialog_message);
        cbCancelable = findViewById(R.id.cb_cancelable);
        cbCancelableOutside = findViewById(R.id.cb_cancelable_outside);
        actionsContainer = findViewById(R.id.layout_actions_container);
        inputsContainer = findViewById(R.id.layout_inputs_container);
        etBackgroundColor = findViewById(R.id.et_background_color);
        etTitleColor = findViewById(R.id.et_title_color);
        etMessageColor = findViewById(R.id.et_message_color);
        etDividerColor = findViewById(R.id.et_divider_color);
        etTitleSize = findViewById(R.id.et_title_size);
        etMessageSize = findViewById(R.id.et_message_size);
        etInputTextSize = findViewById(R.id.et_input_text_size);
        etCornerRadius = findViewById(R.id.et_corner_radius);
        etButtonCornerRadius = findViewById(R.id.et_button_corner_radius);
        etButtonPadding = findViewById(R.id.et_button_padding);
        etButtonSpacing = findViewById(R.id.et_button_spacing);
        etPrimaryBg = findViewById(R.id.et_primary_bg);
        etPrimaryText = findViewById(R.id.et_primary_text);
        etSecondaryBg = findViewById(R.id.et_secondary_bg);
        etSecondaryText = findViewById(R.id.et_secondary_text);
        etDestructiveBg = findViewById(R.id.et_destructive_bg);
        etDestructiveText = findViewById(R.id.et_destructive_text);
        etNeutralBg = findViewById(R.id.et_neutral_bg);
        etNeutralText = findViewById(R.id.et_neutral_text);
        spinnerContentGravity = findViewById(R.id.spinner_content_gravity);
        etLayoutPadding = findViewById(R.id.et_layout_padding);
        etMaxWidth = findViewById(R.id.et_max_width);
        spinnerEnterAnimation = findViewById(R.id.spinner_enter_animation);
        spinnerExitAnimation = findViewById(R.id.spinner_exit_animation);
        etAnimationDuration = findViewById(R.id.et_animation_duration);
        spinnerCustomContent = findViewById(R.id.spinner_custom_content);
        tvCustomContentHelp = findViewById(R.id.tv_custom_content_help);
        etCustomPrimary = findViewById(R.id.et_custom_primary);
        etCustomSecondary = findViewById(R.id.et_custom_secondary);
        etCustomTertiary = findViewById(R.id.et_custom_tertiary);
        tvBuilderOutput = findViewById(R.id.tv_builder_output);
        tvGeneratedCode = findViewById(R.id.tv_generated_code);
        builderRoot = findViewById(R.id.builder_root);
        builderContent = findViewById(R.id.builder_content);
        builderBottomBar = findViewById(R.id.builder_bottom_bar);
    }

    private void applyWindowInsets() {
        final int contentLeft = builderContent.getPaddingLeft();
        final int contentTop = builderContent.getPaddingTop();
        final int contentRight = builderContent.getPaddingRight();
        final int contentBottom = builderContent.getPaddingBottom();
        final int barLeft = builderBottomBar.getPaddingLeft();
        final int barTop = builderBottomBar.getPaddingTop();
        final int barRight = builderBottomBar.getPaddingRight();
        final int barBottom = builderBottomBar.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(builderRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            builderContent.setPadding(
                    contentLeft + systemBars.left,
                    contentTop + systemBars.top,
                    contentRight + systemBars.right,
                    contentBottom);
            builderBottomBar.setPadding(
                    barLeft + systemBars.left,
                    barTop,
                    barRight + systemBars.right,
                    barBottom + systemBars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(builderRoot);
    }

    private void setupSpinners() {
        bindSpinner(spinnerContentGravity, new String[]{"LEFT", "CENTER", "RIGHT"});
        bindSpinner(spinnerEnterAnimation, new String[]{"FADE", "SLIDE_UP", "SLIDE_DOWN", "SCALE", "NONE"});
        bindSpinner(spinnerExitAnimation, new String[]{"FADE", "SLIDE_UP", "SLIDE_DOWN", "SCALE", "NONE"});
        bindSpinner(spinnerCustomContent, new String[]{"NONE", "RICH_TEXT", "IMAGE_URL", "WEBVIEW_URL", "PROGRESS", "SIMPLE_LIST"});
    }

    private void setupStaticListeners() {
        EditText[] trackedEdits = new EditText[]{
                etDialogTitle, etDialogMessage, etBackgroundColor, etTitleColor, etMessageColor,
                etDividerColor, etTitleSize, etMessageSize, etInputTextSize, etCornerRadius,
                etButtonCornerRadius, etButtonPadding, etButtonSpacing, etPrimaryBg, etPrimaryText,
                etSecondaryBg, etSecondaryText, etDestructiveBg, etDestructiveText, etNeutralBg,
                etNeutralText, etLayoutPadding, etMaxWidth, etAnimationDuration,
                etCustomPrimary, etCustomSecondary, etCustomTertiary
        };
        for (EditText editText : trackedEdits) {
            styleBuilderInput(editText);
            addAfterTextChanged(editText, this::refreshGeneratedCode);
        }

        bindColorPicker(etBackgroundColor, "Background Color");
        bindColorPicker(etTitleColor, "Title Color");
        bindColorPicker(etMessageColor, "Message Color");
        bindColorPicker(etDividerColor, "Divider Color");
        bindColorPicker(etPrimaryBg, "Primary Button Background");
        bindColorPicker(etPrimaryText, "Primary Button Text");
        bindColorPicker(etSecondaryBg, "Secondary Button Background");
        bindColorPicker(etSecondaryText, "Secondary Button Text");
        bindColorPicker(etDestructiveBg, "Destructive Button Background");
        bindColorPicker(etDestructiveText, "Destructive Button Text");
        bindColorPicker(etNeutralBg, "Neutral Button Background");
        bindColorPicker(etNeutralText, "Neutral Button Text");

        styleBuilderCheckBox(cbCancelable);
        styleBuilderCheckBox(cbCancelableOutside);
        cbCancelable.setOnCheckedChangeListener((buttonView, isChecked) -> refreshGeneratedCode());
        cbCancelableOutside.setOnCheckedChangeListener((buttonView, isChecked) -> refreshGeneratedCode());

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent == spinnerCustomContent) {
                    updateCustomContentHints();
                }
                refreshGeneratedCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerContentGravity.setOnItemSelectedListener(itemSelectedListener);
        spinnerEnterAnimation.setOnItemSelectedListener(itemSelectedListener);
        spinnerExitAnimation.setOnItemSelectedListener(itemSelectedListener);
        spinnerCustomContent.setOnItemSelectedListener(itemSelectedListener);
    }

    private void setupButtons() {
        findViewById(R.id.btn_add_action).setOnClickListener(v -> addActionRow(null));
        findViewById(R.id.btn_add_input).setOnClickListener(v -> addInputRow(null));
        findViewById(R.id.btn_preview_dialog).setOnClickListener(v -> previewDialog());
        findViewById(R.id.btn_reset_builder).setOnClickListener(v -> resetBuilder());
        findViewById(R.id.btn_copy_code).setOnClickListener(v -> copyCode());
    }

    private void resetBuilder() {
        actionRows.clear();
        inputRows.clear();
        actionsContainer.removeAllViews();
        inputsContainer.removeAllViews();
        callbackEvents.clear();

        etDialogTitle.setText("Builder Preview");
        etDialogMessage.setText("Customize every part of this dialog, then preview it.");
        cbCancelable.setChecked(true);
        cbCancelableOutside.setChecked(false);

        DialogueStyle defaultStyle = new DialogueStyle();
        DialogueLayout defaultLayout = new DialogueLayout();
        DialogueAnimation defaultAnimation = new DialogueAnimation();

        etBackgroundColor.setText(toHex(defaultStyle.backgroundColor));
        etTitleColor.setText(toHex(defaultStyle.titleColor));
        etMessageColor.setText(toHex(defaultStyle.messageColor));
        etDividerColor.setText(toHex(defaultStyle.dividerColor));
        etTitleSize.setText(formatFloat(defaultStyle.titleTextSize));
        etMessageSize.setText(formatFloat(defaultStyle.messageTextSize));
        etInputTextSize.setText(formatFloat(defaultStyle.inputTextSize));
        etCornerRadius.setText(formatFloat(defaultStyle.cornerRadius));
        etButtonCornerRadius.setText(formatFloat(defaultStyle.buttonCornerRadius));
        etButtonPadding.setText(String.valueOf(defaultStyle.buttonPadding));
        etButtonSpacing.setText(String.valueOf(defaultStyle.buttonSpacing));
        etPrimaryBg.setText(toHex(defaultStyle.primaryButtonBg));
        etPrimaryText.setText(toHex(defaultStyle.primaryButtonTextColor));
        etSecondaryBg.setText(toHex(defaultStyle.secondaryButtonBg));
        etSecondaryText.setText(toHex(defaultStyle.secondaryButtonTextColor));
        etDestructiveBg.setText(toHex(defaultStyle.destructiveButtonBg));
        etDestructiveText.setText(toHex(defaultStyle.destructiveButtonTextColor));
        etNeutralBg.setText(toHex(defaultStyle.neutralButtonBg));
        etNeutralText.setText(toHex(defaultStyle.neutralButtonTextColor));

        spinnerContentGravity.setSelection(0);
        etLayoutPadding.setText(formatFloat(defaultLayout.padding));
        etMaxWidth.setText(formatFloat(defaultLayout.maxWidthDp));

        spinnerEnterAnimation.setSelection(0);
        spinnerExitAnimation.setSelection(0);
        etAnimationDuration.setText(String.valueOf(defaultAnimation.duration));

        spinnerCustomContent.setSelection(0);
        etCustomPrimary.setText("");
        etCustomSecondary.setText("");
        etCustomTertiary.setText("");
        updateCustomContentHints();

        addActionRow(new ActionConfig("OK", DialogueAction.Style.PRIMARY, true));

        tvBuilderOutput.setText("Output: ready");
        refreshGeneratedCode();
    }

    private void addActionRow(@Nullable ActionConfig config) {
        ActionConfig safe = config != null ? config : new ActionConfig("Button", DialogueAction.Style.NEUTRAL, true);
        ActionRow row = new ActionRow(safe);
        actionRows.add(row);
        actionsContainer.addView(row.root);
        refreshGeneratedCode();
    }

    private void addInputRow(@Nullable InputConfig config) {
        InputConfig safe = config != null ? config : new InputConfig();
        InputRow row = new InputRow(safe);
        inputRows.add(row);
        inputsContainer.addView(row.root);
        refreshGeneratedCode();
    }

    private void previewDialog() {
        BuilderState state = collectState();
        appendOutput("Previewing dialog...");

        DialogueBuilder builder = new DialogueBuilder(this)
                .setTitle(state.title)
                .setMessage(state.message)
                .setCancelable(state.cancelable)
                .setCancelableOutside(state.cancelableOutside)
                .setStyle(state.style)
                .setLayout(state.layout)
                .setAnimation(state.animation)
                .setOnShowListener(d -> appendOutput("onShow"))
                .setOnDismissListener(d -> appendOutput("onDismiss"))
                .setOnActionListener((dialogue, action, index) ->
                        appendOutput("onAction[" + index + "]: " + action.text + " (" + action.style + ")"))
                .setOnInputResultListener((dialogue, results) ->
                        appendOutput("onInputResult: " + results));

        for (InputConfig inputConfig : state.inputs) {
            DialogueInput input = new DialogueInput(inputConfig.type, inputConfig.hint);
            input.initialValue = inputConfig.initialValue;
            input.maxLength = inputConfig.maxLength;
            input.singleLine = inputConfig.singleLine;
            input.validator = buildValidator(inputConfig.validatorPreset);
            builder.addInput(input);
        }

        for (ActionConfig actionConfig : state.actions) {
            DialogueAction action = new DialogueAction(actionConfig.text, actionConfig.style,
                    v -> appendOutput("clicked: " + actionConfig.text));
            action.enabled = actionConfig.enabled;
            builder.addAction(action);
        }

        View customView = createCustomView(state.customContentType, state.customPrimary, state.customSecondary, state.customTertiary);
        if (customView != null) {
            builder.setCustomView(customView);
        }

        builder.show();
    }

    private void copyCode() {
        String code = tvGeneratedCode.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("DialogueBuilder code", code));
            Toast.makeText(this, "Generated code copied", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshGeneratedCode() {
        tvGeneratedCode.setText(generateCode(collectState()));
    }

    private BuilderState collectState() {
        BuilderState state = new BuilderState();
        state.title = valueOf(etDialogTitle);
        state.message = valueOf(etDialogMessage);
        state.cancelable = cbCancelable.isChecked();
        state.cancelableOutside = cbCancelableOutside.isChecked();

        DialogueStyle style = new DialogueStyle();
        style.backgroundColor = parseColor(valueOf(etBackgroundColor), style.backgroundColor);
        style.titleColor = parseColor(valueOf(etTitleColor), style.titleColor);
        style.messageColor = parseColor(valueOf(etMessageColor), style.messageColor);
        style.dividerColor = parseColor(valueOf(etDividerColor), style.dividerColor);
        style.titleTextSize = parseFloat(valueOf(etTitleSize), style.titleTextSize);
        style.messageTextSize = parseFloat(valueOf(etMessageSize), style.messageTextSize);
        style.inputTextSize = parseFloat(valueOf(etInputTextSize), style.inputTextSize);
        style.cornerRadius = parseFloat(valueOf(etCornerRadius), style.cornerRadius);
        style.buttonCornerRadius = parseFloat(valueOf(etButtonCornerRadius), style.buttonCornerRadius);
        style.buttonPadding = parseInt(valueOf(etButtonPadding), style.buttonPadding);
        style.buttonSpacing = parseInt(valueOf(etButtonSpacing), style.buttonSpacing);
        style.primaryButtonBg = parseColor(valueOf(etPrimaryBg), style.primaryButtonBg);
        style.primaryButtonTextColor = parseColor(valueOf(etPrimaryText), style.primaryButtonTextColor);
        style.secondaryButtonBg = parseColor(valueOf(etSecondaryBg), style.secondaryButtonBg);
        style.secondaryButtonTextColor = parseColor(valueOf(etSecondaryText), style.secondaryButtonTextColor);
        style.destructiveButtonBg = parseColor(valueOf(etDestructiveBg), style.destructiveButtonBg);
        style.destructiveButtonTextColor = parseColor(valueOf(etDestructiveText), style.destructiveButtonTextColor);
        style.neutralButtonBg = parseColor(valueOf(etNeutralBg), style.neutralButtonBg);
        style.neutralButtonTextColor = parseColor(valueOf(etNeutralText), style.neutralButtonTextColor);
        state.style = style;

        DialogueLayout layout = new DialogueLayout();
        layout.padding = parseFloat(valueOf(etLayoutPadding), layout.padding);
        layout.maxWidthDp = parseFloat(valueOf(etMaxWidth), layout.maxWidthDp);
        layout.contentGravity = DialogueLayout.ContentGravity.valueOf(spinnerContentGravity.getSelectedItem().toString());
        state.layout = layout;

        DialogueAnimation animation = new DialogueAnimation();
        animation.enterType = DialogueAnimation.Type.valueOf(spinnerEnterAnimation.getSelectedItem().toString());
        animation.exitType = DialogueAnimation.Type.valueOf(spinnerExitAnimation.getSelectedItem().toString());
        animation.duration = parseInt(valueOf(etAnimationDuration), (int) animation.duration);
        state.animation = animation;

        for (ActionRow actionRow : actionRows) {
            state.actions.add(actionRow.toConfig());
        }
        for (InputRow inputRow : inputRows) {
            state.inputs.add(inputRow.toConfig());
        }

        state.customContentType = CustomContentType.valueOf(spinnerCustomContent.getSelectedItem().toString());
        state.customPrimary = valueOf(etCustomPrimary);
        state.customSecondary = valueOf(etCustomSecondary);
        state.customTertiary = valueOf(etCustomTertiary);
        return state;
    }

    private View createCustomView(CustomContentType type, String primary, String secondary, String tertiary) {
        switch (type) {
            case RICH_TEXT:
                TextView richView = new TextView(this);
                richView.setText(Html.fromHtml(primary.isEmpty() ? "<b>Rich text preview</b><br/><i>Configure HTML content here.</i>" : primary, Html.FROM_HTML_MODE_COMPACT));
                richView.setMovementMethod(LinkMovementMethod.getInstance());
                richView.setTextColor(Color.parseColor("#333333"));
                richView.setTextSize(15f);
                richView.setPadding(dp(12), dp(12), dp(12), dp(12));
                return richView;
            case IMAGE_URL:
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(parseInt(secondary, 200))));
                Glide.with(this)
                        .load(primary.isEmpty() ? "https://picsum.photos/seed/dialogue-builder/600/300" : primary)
                        .into(imageView);
                return imageView;
            case WEBVIEW_URL:
                WebView webView = new WebView(this);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(parseInt(secondary, 320))));
                webView.loadUrl(normalizeUrl(primary.isEmpty() ? "https://example.com" : primary));
                return webView;
            case PROGRESS:
                LinearLayout progressLayout = new LinearLayout(this);
                progressLayout.setOrientation(LinearLayout.VERTICAL);
                progressLayout.setPadding(dp(16), dp(16), dp(16), dp(16));
                TextView label = new TextView(this);
                label.setText(primary.isEmpty() ? "Uploading assets..." : primary);
                label.setTextSize(15f);
                label.setTextColor(Color.parseColor("#444444"));
                progressLayout.addView(label);
                ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
                progressBar.setMax(100);
                progressBar.setProgress(Math.max(0, Math.min(100, parseInt(secondary, 45))));
                LinearLayout.LayoutParams barParams = (LinearLayout.LayoutParams) progressBar.getLayoutParams();
                barParams.topMargin = dp(12);
                progressLayout.addView(progressBar);
                return progressLayout;
            case SIMPLE_LIST:
                LinearLayout listLayout = new LinearLayout(this);
                listLayout.setOrientation(LinearLayout.VERTICAL);
                listLayout.setPadding(dp(12), dp(8), dp(12), dp(8));
                String[] items = (primary.isEmpty() ? "Option A,Option B,Option C" : primary).split(",");
                boolean singleChoice = "single".equalsIgnoreCase(secondary);
                if (singleChoice) {
                    RadioGroup group = new RadioGroup(this);
                    for (String item : items) {
                        RadioButton radio = new RadioButton(this);
                        radio.setText(item.trim());
                        group.addView(radio);
                    }
                    listLayout.addView(group);
                } else {
                    for (String item : items) {
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setText(item.trim());
                        listLayout.addView(checkBox);
                    }
                }
                return listLayout;
            case NONE:
            default:
                return null;
        }
    }

    private String generateCode(BuilderState state) {
        StringBuilder code = new StringBuilder();
        code.append("// Generated from AlertDialogBuilderActivity\n");
        appendCustomViewHelperCode(code, state);
        code.append("DialogueBuilder builder = new DialogueBuilder(this)\n");
        code.append("        .setTitle(").append(toJavaString(state.title)).append(")\n");
        code.append("        .setMessage(").append(toJavaString(state.message)).append(")\n");
        code.append("        .setCancelable(").append(state.cancelable).append(")\n");
        code.append("        .setCancelableOutside(").append(state.cancelableOutside).append(");\n\n");

        code.append("DialogueStyle style = new DialogueStyle();\n");
        code.append("style.backgroundColor = Color.parseColor(").append(toJavaString(toHex(state.style.backgroundColor))).append(");\n");
        code.append("style.titleColor = Color.parseColor(").append(toJavaString(toHex(state.style.titleColor))).append(");\n");
        code.append("style.messageColor = Color.parseColor(").append(toJavaString(toHex(state.style.messageColor))).append(");\n");
        code.append("style.dividerColor = Color.parseColor(").append(toJavaString(toHex(state.style.dividerColor))).append(");\n");
        code.append("style.titleTextSize = ").append(formatFloat(state.style.titleTextSize)).append("f;\n");
        code.append("style.messageTextSize = ").append(formatFloat(state.style.messageTextSize)).append("f;\n");
        code.append("style.inputTextSize = ").append(formatFloat(state.style.inputTextSize)).append("f;\n");
        code.append("style.cornerRadius = ").append(formatFloat(state.style.cornerRadius)).append("f;\n");
        code.append("style.buttonCornerRadius = ").append(formatFloat(state.style.buttonCornerRadius)).append("f;\n");
        code.append("style.buttonPadding = ").append(state.style.buttonPadding).append(";\n");
        code.append("style.buttonSpacing = ").append(state.style.buttonSpacing).append(";\n");
        code.append("style.primaryButtonBg = Color.parseColor(").append(toJavaString(toHex(state.style.primaryButtonBg))).append(");\n");
        code.append("style.primaryButtonTextColor = Color.parseColor(").append(toJavaString(toHex(state.style.primaryButtonTextColor))).append(");\n");
        code.append("style.secondaryButtonBg = Color.parseColor(").append(toJavaString(toHex(state.style.secondaryButtonBg))).append(");\n");
        code.append("style.secondaryButtonTextColor = Color.parseColor(").append(toJavaString(toHex(state.style.secondaryButtonTextColor))).append(");\n");
        code.append("style.destructiveButtonBg = Color.parseColor(").append(toJavaString(toHex(state.style.destructiveButtonBg))).append(");\n");
        code.append("style.destructiveButtonTextColor = Color.parseColor(").append(toJavaString(toHex(state.style.destructiveButtonTextColor))).append(");\n");
        code.append("style.neutralButtonBg = Color.parseColor(").append(toJavaString(toHex(state.style.neutralButtonBg))).append(");\n");
        code.append("style.neutralButtonTextColor = Color.parseColor(").append(toJavaString(toHex(state.style.neutralButtonTextColor))).append(");\n\n");

        code.append("DialogueLayout layout = new DialogueLayout();\n");
        code.append("layout.padding = ").append(formatFloat(state.layout.padding)).append("f;\n");
        code.append("layout.maxWidthDp = ").append(formatFloat(state.layout.maxWidthDp)).append("f;\n");
        code.append("layout.contentGravity = DialogueLayout.ContentGravity.").append(state.layout.contentGravity.name()).append(";\n\n");

        code.append("DialogueAnimation animation = new DialogueAnimation();\n");
        code.append("animation.enterType = DialogueAnimation.Type.").append(state.animation.enterType.name()).append(";\n");
        code.append("animation.exitType = DialogueAnimation.Type.").append(state.animation.exitType.name()).append(";\n");
        code.append("animation.duration = ").append(state.animation.duration).append(";\n\n");

        code.append("builder\n");
        code.append("        .setStyle(style)\n");
        code.append("        .setLayout(layout)\n");
        code.append("        .setAnimation(animation)\n");
        code.append("        .setOnShowListener(d -> { })\n");
        code.append("        .setOnDismissListener(d -> { })\n");
        code.append("        .setOnActionListener((dialogue, action, index) -> { })\n");
        code.append("        .setOnInputResultListener((dialogue, results) -> { });\n\n");

        for (InputConfig input : state.inputs) {
            code.append("{\n");
            code.append("    DialogueInput input = new DialogueInput(DialogueInput.Type.").append(input.type.name()).append(", ")
                    .append(toJavaString(input.hint)).append(");\n");
            if (!input.initialValue.isEmpty()) {
                code.append("    input.initialValue = ").append(toJavaString(input.initialValue)).append(";\n");
            }
            code.append("    input.singleLine = ").append(input.singleLine).append(";\n");
            code.append("    input.maxLength = ").append(input.maxLength).append(";\n");
            appendValidatorCode(code, input.validatorPreset);
            code.append("    builder.addInput(input);\n");
            code.append("}\n");
        }
        if (!state.inputs.isEmpty()) {
            code.append("\n");
        }

        for (ActionConfig action : state.actions) {
            code.append("{\n");
            code.append("    DialogueAction action = new DialogueAction(")
                    .append(toJavaString(action.text)).append(", DialogueAction.Style.")
                    .append(action.style.name()).append(", v -> { });\n");
            code.append("    action.enabled = ").append(action.enabled).append(";\n");
            code.append("    builder.addAction(action);\n");
            code.append("}\n");
        }
        if (!state.actions.isEmpty()) {
            code.append("\n");
        }

        if (state.customContentType != CustomContentType.NONE) {
            code.append("builder.setCustomView(customView);\n");
        }
        code.append("builder.show();\n");
        return code.toString();
    }

    private void appendCustomViewHelperCode(StringBuilder code, BuilderState state) {
        switch (state.customContentType) {
            case RICH_TEXT:
                code.append("TextView customView = new TextView(this);\n");
                code.append("customView.setText(Html.fromHtml(").append(toJavaString(state.customPrimary.isEmpty()
                        ? "<b>Rich text preview</b><br/><i>Configure HTML content here.</i>"
                        : state.customPrimary)).append(", Html.FROM_HTML_MODE_COMPACT));\n");
                code.append("customView.setMovementMethod(LinkMovementMethod.getInstance());\n");
                code.append("customView.setPadding(dp(12), dp(12), dp(12), dp(12));\n\n");
                break;
            case IMAGE_URL:
                code.append("ImageView customView = new ImageView(this);\n");
                code.append("customView.setScaleType(ImageView.ScaleType.CENTER_CROP);\n");
                code.append("customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(")
                        .append(parseInt(state.customSecondary, 200)).append(")));\n");
                code.append("Glide.with(this).load(").append(toJavaString(state.customPrimary.isEmpty()
                        ? "https://picsum.photos/seed/dialogue-builder/600/300"
                        : state.customPrimary)).append(").into(customView);\n\n");
                break;
            case WEBVIEW_URL:
                code.append("WebView customView = new WebView(this);\n");
                code.append("customView.getSettings().setJavaScriptEnabled(true);\n");
                code.append("customView.setWebViewClient(new WebViewClient());\n");
                code.append("customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(")
                        .append(parseInt(state.customSecondary, 320)).append(")));\n");
                code.append("customView.loadUrl(").append(toJavaString(state.customPrimary.isEmpty()
                        ? "https://example.com"
                        : state.customPrimary)).append(");\n\n");
                break;
            case PROGRESS:
                code.append("LinearLayout customView = new LinearLayout(this);\n");
                code.append("customView.setOrientation(LinearLayout.VERTICAL);\n");
                code.append("customView.setPadding(dp(16), dp(16), dp(16), dp(16));\n");
                code.append("TextView progressLabel = new TextView(this);\n");
                code.append("progressLabel.setText(").append(toJavaString(state.customPrimary.isEmpty()
                        ? "Uploading assets..."
                        : state.customPrimary)).append(");\n");
                code.append("customView.addView(progressLabel);\n");
                code.append("ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);\n");
                code.append("progressBar.setMax(100);\n");
                code.append("progressBar.setProgress(").append(Math.max(0, Math.min(100, parseInt(state.customSecondary, 45)))).append(");\n");
                code.append("customView.addView(progressBar);\n\n");
                break;
            case SIMPLE_LIST:
                code.append("LinearLayout customView = new LinearLayout(this);\n");
                code.append("customView.setOrientation(LinearLayout.VERTICAL);\n");
                code.append("customView.setPadding(dp(12), dp(8), dp(12), dp(8));\n");
                code.append("String[] items = ").append(toJavaString(state.customPrimary.isEmpty()
                        ? "Option A,Option B,Option C"
                        : state.customPrimary)).append(".split(\",\");\n");
                code.append("for (String item : items) {\n");
                if ("single".equalsIgnoreCase(state.customSecondary)) {
                    code.append("    RadioButton radio = new RadioButton(this);\n");
                    code.append("    radio.setText(item.trim());\n");
                    code.append("    customView.addView(radio);\n");
                } else {
                    code.append("    CheckBox checkBox = new CheckBox(this);\n");
                    code.append("    checkBox.setText(item.trim());\n");
                    code.append("    customView.addView(checkBox);\n");
                }
                code.append("}\n\n");
                break;
            case NONE:
            default:
                break;
        }
    }

    private void appendValidatorCode(StringBuilder code, ValidatorPreset preset) {
        switch (preset) {
            case REQUIRED:
                code.append("    input.validator = s -> s.trim().isEmpty() ? \"Required\" : null;\n");
                break;
            case EMAIL:
                code.append("    input.validator = s -> s.contains(\"@\") ? null : \"Invalid email\";\n");
                break;
            case MIN_3:
                code.append("    input.validator = s -> s.length() >= 3 ? null : \"Minimum 3 characters\";\n");
                break;
            case NUMERIC:
                code.append("    input.validator = s -> s.matches(\"\\\\d\") ? null : \"Numbers only\";\n");
                break;
            case NONE:
            default:
                break;
        }
    }

    private java.util.function.Function<String, String> buildValidator(ValidatorPreset preset) {
        switch (preset) {
            case REQUIRED:
                return s -> s.trim().isEmpty() ? "Required" : null;
            case EMAIL:
                return s -> s.contains("@") ? null : "Invalid email";
            case MIN_3:
                return s -> s.length() >= 3 ? null : "Minimum 3 characters";
            case NUMERIC:
                return s -> s.matches("\\d") ? null : "Numbers only";
            case NONE:
            default:
                return null;
        }
    }

    private void updateCustomContentHints() {
        CustomContentType type = CustomContentType.valueOf(spinnerCustomContent.getSelectedItem().toString());
        switch (type) {
            case RICH_TEXT:
                tvCustomContentHelp.setText("Primary: HTML text");
                etCustomPrimary.setHint("HTML content");
                etCustomSecondary.setHint("Optional note");
                etCustomTertiary.setHint("Optional note");
                break;
            case IMAGE_URL:
                tvCustomContentHelp.setText("Primary: image URL, Secondary: height dp");
                etCustomPrimary.setHint("https://...");
                etCustomSecondary.setHint("Height in dp (e.g. 220)");
                etCustomTertiary.setHint("Optional caption");
                break;
            case WEBVIEW_URL:
                tvCustomContentHelp.setText("Primary: webpage URL, Secondary: height dp");
                etCustomPrimary.setHint("https://...");
                etCustomSecondary.setHint("Height in dp (e.g. 320)");
                etCustomTertiary.setHint("Optional note");
                break;
            case PROGRESS:
                tvCustomContentHelp.setText("Primary: status label, Secondary: progress percent");
                etCustomPrimary.setHint("Uploading files...");
                etCustomSecondary.setHint("0-100");
                etCustomTertiary.setHint("Optional note");
                break;
            case SIMPLE_LIST:
                tvCustomContentHelp.setText("Primary: comma-separated items, Secondary: 'single' for radio list");
                etCustomPrimary.setHint("WiFi,Bluetooth,Location");
                etCustomSecondary.setHint("single or empty");
                etCustomTertiary.setHint("Optional note");
                break;
            case NONE:
            default:
                tvCustomContentHelp.setText("No custom content selected.");
                etCustomPrimary.setHint("Primary custom value");
                etCustomSecondary.setHint("Secondary custom value");
                etCustomTertiary.setHint("Tertiary custom value");
                break;
        }
    }

    private void appendOutput(String line) {
        callbackEvents.add(line);
        StringBuilder builder = new StringBuilder("Output:");
        for (String event : callbackEvents) {
            builder.append("\n").append("- ").append(event);
        }
        tvBuilderOutput.setText(builder.toString());
    }

    private void bindSpinner(Spinner spinner, String[] values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values) {
            @Override
            public View getView(int position, @Nullable View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.parseColor("#212121"));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.parseColor("#212121"));
                    ((TextView) view).setBackgroundColor(Color.WHITE);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D0D0D0")));
    }

    private void addAfterTextChanged(EditText editText, Runnable action) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                action.run();
            }
        });
    }

    private void bindColorPicker(EditText editText, String label) {
        styleBuilderInput(editText);
        editText.setHint(editText.getHint() + "  Tap swatch to pick");
        updateColorFieldPreview(editText);
        addAfterTextChanged(editText, () -> updateColorFieldPreview(editText));
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_UP) {
                return false;
            }
            Drawable[] drawables = editText.getCompoundDrawablesRelative();
            Drawable start = drawables[0];
            if (start == null) {
                return false;
            }
            int tapZone = editText.getPaddingStart() + start.getBounds().width() + dp(12);
            if (event.getX() <= tapZone) {
                showColorPickerDialog(editText, label);
                return true;
            }
            return false;
        });
    }

    private void updateColorFieldPreview(EditText editText) {
        GradientDrawable swatch = new GradientDrawable();
        swatch.setShape(GradientDrawable.RECTANGLE);
        swatch.setCornerRadius(dp(6));
        swatch.setStroke(dp(1), Color.parseColor("#D0D0D0"));
        swatch.setColor(parseColor(valueOf(editText), Color.WHITE));
        swatch.setSize(dp(24), dp(24));
        editText.setCompoundDrawablePadding(dp(10));
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(swatch, null, null, null);
    }

    private void showColorPickerDialog(EditText target, String label) {
        int initialColor = parseColor(valueOf(target), Color.WHITE);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(16), dp(20), dp(8));

        TextView valueLabel = new TextView(this);
        valueLabel.setTextSize(14f);
        valueLabel.setTextColor(Color.parseColor("#333333"));
        valueLabel.setTypeface(Typeface.DEFAULT_BOLD);
        content.addView(valueLabel);

        View preview = new View(this);
        LinearLayout.LayoutParams previewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(56));
        previewParams.topMargin = dp(12);
        preview.setLayoutParams(previewParams);
        content.addView(preview);

        SeekBar redBar = createColorSeekBar(content, "Red", Color.red(initialColor));
        SeekBar greenBar = createColorSeekBar(content, "Green", Color.green(initialColor));
        SeekBar blueBar = createColorSeekBar(content, "Blue", Color.blue(initialColor));

        Runnable refresh = () -> {
            int color = Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
            valueLabel.setText(label + ": " + toHex(color));
            GradientDrawable previewBg = new GradientDrawable();
            previewBg.setCornerRadius(dp(12));
            previewBg.setStroke(dp(1), Color.parseColor("#D0D0D0"));
            previewBg.setColor(color);
            preview.setBackground(previewBg);
        };
        refresh.run();

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refresh.run();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        redBar.setOnSeekBarChangeListener(listener);
        greenBar.setOnSeekBarChangeListener(listener);
        blueBar.setOnSeekBarChangeListener(listener);

        new AlertDialog.Builder(this)
                .setTitle(label)
                .setView(content)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Use Color", (dialog, which) -> {
                    int color = Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
                    target.setText(toHex(color));
                })
                .show();
    }

    private SeekBar createColorSeekBar(LinearLayout parent, String label, int value) {
        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextColor(Color.parseColor("#555555"));
        tv.setPadding(0, dp(12), 0, dp(4));
        parent.addView(tv);

        SeekBar bar = new SeekBar(this);
        bar.setMax(255);
        bar.setProgress(value);
        parent.addView(bar);
        return bar;
    }

    private String valueOf(EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private int parseColor(String value, int fallback) {
        try {
            return Color.parseColor(value);
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private float parseFloat(String value, float fallback) {
        try {
            return Float.parseFloat(value.trim());
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private String toHex(int color) {
        return String.format(Locale.US, "#%06X", (0xFFFFFF & color));
    }

    private String formatFloat(float value) {
        if (Math.abs(value - Math.round(value)) < 0.001f) {
            return String.valueOf(Math.round(value));
        }
        return String.format(Locale.US, "%.1f", value);
    }

    private String toJavaString(String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")+  "\"";
    }

    private String normalizeUrl(String value) {
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return value;
        }
        return "https://" + value;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void styleBuilderInput(EditText editText) {
        editText.setTextColor(Color.parseColor("#212121"));
        editText.setHintTextColor(Color.parseColor("#7A7A7A"));
        editText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CFCFCF")));
    }

    private void styleBuilderCheckBox(CheckBox checkBox) {
        checkBox.setTextColor(Color.parseColor("#212121"));
        checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#1976D2")));
    }

    private void styleSecondaryButton(Button button) {
        button.setTextColor(Color.parseColor("#212121"));
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dp(10));
        bg.setColor(Color.parseColor("#F2F2F2"));
        bg.setStroke(dp(1), Color.parseColor("#D9D9D9"));
        button.setBackground(bg);
        button.setAllCaps(false);
    }

    private class ActionRow {
        final LinearLayout root;
        final EditText text;
        final Spinner style;
        final CheckBox enabled;

        ActionRow(ActionConfig config) {
            root = new LinearLayout(AlertDialogBuilderActivity.this);
            root.setOrientation(LinearLayout.VERTICAL);
            root.setBackground(createRowBackground());
            root.setPadding(dp(12), dp(12), dp(12), dp(12));
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootParams.topMargin = dp(8);
            root.setLayoutParams(rootParams);

            TextView label = createRowLabel("Action");
            root.addView(label);

            text = new EditText(AlertDialogBuilderActivity.this);
            text.setHint("Button text");
            text.setText(config.text);
            styleBuilderInput(text);
            root.addView(text);

            TextView styleLabel = createMiniLabel("Style");
            root.addView(styleLabel);
            style = new Spinner(AlertDialogBuilderActivity.this);
            bindSpinner(style, new String[]{"PRIMARY", "SECONDARY", "DESTRUCTIVE", "NEUTRAL"});
            style.setSelection(config.style.ordinal());
            root.addView(style);

            enabled = new CheckBox(AlertDialogBuilderActivity.this);
            enabled.setText("Enabled");
            enabled.setChecked(config.enabled);
            styleBuilderCheckBox(enabled);
            root.addView(enabled);

            Button remove = new Button(AlertDialogBuilderActivity.this);
            remove.setText("Remove Action");
            styleSecondaryButton(remove);
            remove.setOnClickListener(v -> {
                actionRows.remove(this);
                actionsContainer.removeView(root);
                refreshGeneratedCode();
            });
            root.addView(remove);

            addAfterTextChanged(text, AlertDialogBuilderActivity.this::refreshGeneratedCode);
            enabled.setOnCheckedChangeListener((buttonView, isChecked) -> refreshGeneratedCode());
            style.setOnItemSelectedListener(new SimpleRefreshListener());
        }

        ActionConfig toConfig() {
            return new ActionConfig(valueOf(text), DialogueAction.Style.valueOf(style.getSelectedItem().toString()), enabled.isChecked());
        }
    }

    private class InputRow {
        final LinearLayout root;
        final Spinner type;
        final EditText hint;
        final EditText initialValue;
        final CheckBox singleLine;
        final EditText maxLength;
        final Spinner validator;

        InputRow(InputConfig config) {
            root = new LinearLayout(AlertDialogBuilderActivity.this);
            root.setOrientation(LinearLayout.VERTICAL);
            root.setBackground(createRowBackground());
            root.setPadding(dp(12), dp(12), dp(12), dp(12));
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootParams.topMargin = dp(8);
            root.setLayoutParams(rootParams);

            TextView label = createRowLabel("Input");
            root.addView(label);

            TextView typeLabel = createMiniLabel("Type");
            root.addView(typeLabel);
            type = new Spinner(AlertDialogBuilderActivity.this);
            bindSpinner(type, new String[]{"TEXT", "NUMBER", "PASSWORD", "EMAIL", "PHONE", "MULTILINE"});
            type.setSelection(config.type.ordinal());
            root.addView(type);

            hint = new EditText(AlertDialogBuilderActivity.this);
            hint.setHint("Hint");
            hint.setText(config.hint);
            styleBuilderInput(hint);
            root.addView(hint);

            initialValue = new EditText(AlertDialogBuilderActivity.this);
            initialValue.setHint("Initial value");
            initialValue.setText(config.initialValue);
            styleBuilderInput(initialValue);
            root.addView(initialValue);

            singleLine = new CheckBox(AlertDialogBuilderActivity.this);
            singleLine.setText("Single line");
            singleLine.setChecked(config.singleLine);
            styleBuilderCheckBox(singleLine);
            root.addView(singleLine);

            maxLength = new EditText(AlertDialogBuilderActivity.this);
            maxLength.setHint("Max length (-1 for none)");
            maxLength.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
            maxLength.setText(String.valueOf(config.maxLength));
            styleBuilderInput(maxLength);
            root.addView(maxLength);

            TextView validatorLabel = createMiniLabel("Validator");
            root.addView(validatorLabel);
            validator = new Spinner(AlertDialogBuilderActivity.this);
            bindSpinner(validator, new String[]{"NONE", "REQUIRED", "EMAIL", "MIN_3", "NUMERIC"});
            validator.setSelection(config.validatorPreset.ordinal());
            root.addView(validator);

            Button remove = new Button(AlertDialogBuilderActivity.this);
            remove.setText("Remove Input");
            styleSecondaryButton(remove);
            remove.setOnClickListener(v -> {
                inputRows.remove(this);
                inputsContainer.removeView(root);
                refreshGeneratedCode();
            });
            root.addView(remove);

            addAfterTextChanged(hint, AlertDialogBuilderActivity.this::refreshGeneratedCode);
            addAfterTextChanged(initialValue, AlertDialogBuilderActivity.this::refreshGeneratedCode);
            addAfterTextChanged(maxLength, AlertDialogBuilderActivity.this::refreshGeneratedCode);
            singleLine.setOnCheckedChangeListener((buttonView, isChecked) -> refreshGeneratedCode());
            type.setOnItemSelectedListener(new SimpleRefreshListener());
            validator.setOnItemSelectedListener(new SimpleRefreshListener());
        }

        InputConfig toConfig() {
            InputConfig config = new InputConfig();
            config.type = DialogueInput.Type.valueOf(type.getSelectedItem().toString());
            config.hint = valueOf(hint);
            config.initialValue = valueOf(initialValue);
            config.singleLine = singleLine.isChecked();
            config.maxLength = parseInt(valueOf(maxLength), -1);
            config.validatorPreset = ValidatorPreset.valueOf(validator.getSelectedItem().toString());
            return config;
        }
    }

    private static class BuilderState {
        String title = "";
        String message = "";
        boolean cancelable = true;
        boolean cancelableOutside = false;
        DialogueStyle style = new DialogueStyle();
        DialogueLayout layout = new DialogueLayout();
        DialogueAnimation animation = new DialogueAnimation();
        final List<ActionConfig> actions = new ArrayList<>();
        final List<InputConfig> inputs = new ArrayList<>();
        CustomContentType customContentType = CustomContentType.NONE;
        String customPrimary = "";
        String customSecondary = "";
        String customTertiary = "";
    }

    private static class ActionConfig {
        String text;
        DialogueAction.Style style;
        boolean enabled;

        ActionConfig(String text, DialogueAction.Style style, boolean enabled) {
            this.text = text;
            this.style = style;
            this.enabled = enabled;
        }
    }

    private static class InputConfig {
        DialogueInput.Type type = DialogueInput.Type.TEXT;
        String hint = "";
        String initialValue = "";
        boolean singleLine = true;
        int maxLength = -1;
        ValidatorPreset validatorPreset = ValidatorPreset.NONE;
    }

    private class SimpleRefreshListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refreshGeneratedCode();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private TextView createRowLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTypeface(Typeface.DEFAULT_BOLD);
        label.setTextSize(15f);
        label.setTextColor(Color.parseColor("#212121"));
        return label;
    }

    private TextView createMiniLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(12f);
        label.setTextColor(Color.parseColor("#666666"));
        label.setPadding(0, dp(8), 0, dp(4));
        return label;
    }

    private GradientDrawable createRowBackground() {
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#FAFAFA"));
        background.setCornerRadius(dp(12));
        background.setStroke(dp(1), Color.parseColor("#E0E0E0"));
        return background;
    }
}
