package com.mk.pal.dialogue;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialogue extends Dialog {

    private final String title;
    private final String message;
    private final List<DialogueAction> actions;
    private final List<DialogueInput> inputs;
    private final DialogueStyle style;
    private final DialogueLayout layout;
    private final DialogueAnimation animation;
    private final boolean cancelableOutside;

    private final DialogueCallbacks.OnShowListener onShowListener;
    private final DialogueCallbacks.OnDismissListener onDismissListener;
    private final DialogueCallbacks.OnActionListener onActionListener;
    private final DialogueCallbacks.OnInputResultListener onInputResultListener;

    private final View customView;
    private boolean customViewBeneathTitle = false;

    private View rootView;
    private List<EditText> inputViews;

    Dialogue(@NonNull Context context,
             @Nullable String title,
             @Nullable String message,
             @NonNull List<DialogueAction> actions,
             @NonNull List<DialogueInput> inputs,
             @NonNull DialogueStyle style,
             @NonNull DialogueLayout layout,
             @NonNull DialogueAnimation animation,
             boolean cancelable,
             boolean cancelableOutside,
             @Nullable DialogueCallbacks.OnShowListener onShowListener,
             @Nullable DialogueCallbacks.OnDismissListener onDismissListener,
             @Nullable DialogueCallbacks.OnActionListener onActionListener,
             @Nullable DialogueCallbacks.OnInputResultListener onInputResultListener,
             @Nullable View customView,
             boolean customViewBeneathTitle) {
        super(context);
        this.title = title;
        this.message = message;
        this.actions = actions;
        this.inputs = inputs;
        this.style = style;
        this.layout = layout;
        this.animation = animation;
        this.cancelableOutside = cancelableOutside;
        this.onShowListener = onShowListener;
        this.onDismissListener = onDismissListener;
        this.onActionListener = onActionListener;
        this.onInputResultListener = onInputResultListener;
        this.customView = customView;
        this.customViewBeneathTitle = customViewBeneathTitle;
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        buildViewTree();
        configureWindow();
        applyAnimation();
    }

    private void buildViewTree() {
        Context ctx = getContext();
        int pad = ViewFactory.dpToPx(ctx, layout.padding);
        int contentGrav = layout.getGravity();

        // Root container - use FrameLayout.LayoutParams since Dialog's content parent is FrameLayout
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackground(ViewFactory.createRoundedBackground(ctx, style.backgroundColor, style.cornerRadius));
        root.setPadding(pad, pad, pad, pad);

        int screenWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int maxWidth = ViewFactory.dpToPx(ctx, layout.maxWidthDp);
        int sideMargin = ViewFactory.dpToPx(ctx, 24);
        int dialogWidth = Math.min(maxWidth, screenWidth - sideMargin * 2);

        android.widget.FrameLayout.LayoutParams rootLp = new android.widget.FrameLayout.LayoutParams(
                dialogWidth,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
        rootLp.gravity = Gravity.CENTER;
        root.setLayoutParams(rootLp);

        // Content container
        LinearLayout content = new LinearLayout(ctx);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(contentGrav);
        LinearLayout.LayoutParams contentLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(contentLp);

        // Title
        if (title != null && !title.isEmpty()) {
            TextView titleView = ViewFactory.createTitleTextView(ctx, title, style.titleColor, style.titleTextSize);
            content.addView(titleView);
        }

        // Custom view
        if (customView != null) {
            ViewGroup.LayoutParams existingLp = customView.getLayoutParams();
            int customWidth = existingLp != null ? existingLp.width : ViewGroup.LayoutParams.MATCH_PARENT;
            int customHeight = existingLp != null ? existingLp.height : ViewGroup.LayoutParams.WRAP_CONTENT;
            if (customWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                customWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            LinearLayout.LayoutParams customLp = new LinearLayout.LayoutParams(customWidth, customHeight);
            customView.setLayoutParams(customLp);
            if (customViewBeneathTitle) {
                // Insert after title (position 1 or 0 if no title)
                content.addView(customView, title != null && !title.isEmpty() ? 1 : 0);
            } else {
                content.addView(customView);
            }
        }

        // Message
        if (message != null && !message.isEmpty()) {
            TextView msgView = ViewFactory.createMessageTextView(ctx, message, style.messageColor, style.messageTextSize);
            content.addView(msgView);
        }

        // Input fields
        inputViews = new java.util.ArrayList<>();
        for (DialogueInput input : inputs) {
            EditText et = ViewFactory.createInputEditText(ctx, input.hint, input.getInputType(),
                    style.inputTextColor, style.inputTextSize);
            if (input.initialValue != null && !input.initialValue.isEmpty()) {
                et.setText(input.initialValue);
                et.setSelection(input.initialValue.length());
            }
            if (!input.singleLine) {
                et.setSingleLine(false);
                et.setMinLines(3);
            }
            if (input.maxLength > 0) {
                // We'll handle validation in onSubmit
            }
            content.addView(et);
            inputViews.add(et);
        }

        // Wrap content in scrollview if many inputs/long content
        if (inputs.size() > 2) {
            root.addView(ViewFactory.wrapInScrollView(ctx, content));
        } else {
            root.addView(content);
        }

        // Divider before buttons
        if (!actions.isEmpty()) {
            root.addView(ViewFactory.createDivider(ctx, style.dividerColor));
        }

        // Button row
        if (!actions.isEmpty()) {
            boolean stackActions = actions.size() > 1;
            LinearLayout buttonRow = new LinearLayout(ctx);
            buttonRow.setOrientation(stackActions ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
            buttonRow.setBaselineAligned(false);
            buttonRow.setGravity(stackActions ? Gravity.FILL_HORIZONTAL : Gravity.END);
            LinearLayout.LayoutParams btnRowLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnRowLp.topMargin = ViewFactory.dpToPx(ctx, style.buttonSpacing);
            buttonRow.setLayoutParams(btnRowLp);

            for (int i = 0; i < actions.size(); i++) {
                DialogueAction action = actions.get(i);
                if (i > 0) {
                    View spacer = new View(ctx);
                    LinearLayout.LayoutParams spacerLp = new LinearLayout.LayoutParams(
                            stackActions ? ViewGroup.LayoutParams.MATCH_PARENT : ViewFactory.dpToPx(ctx, style.buttonSpacing),
                            stackActions ? ViewFactory.dpToPx(ctx, style.buttonSpacing) : ViewGroup.LayoutParams.WRAP_CONTENT);
                    spacer.setLayoutParams(spacerLp);
                    buttonRow.addView(spacer);
                }
                GradientDrawable bg = ViewFactory.createButtonBackground(ctx, action.style, style);
                int textColor = getButtonTextColor(action.style);
                android.widget.Button btn = ViewFactory.createButton(ctx, action.text, textColor, bg, style.buttonPadding);
                if (stackActions) {
                    btn.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                btn.setEnabled(action.enabled);
                final int index = i;
                btn.setOnClickListener(v -> {
                    collectInputResults();
                    if (onActionListener != null) {
                        onActionListener.onAction(Dialogue.this, action, index);
                    }
                    if (action.onClickListener != null) {
                        action.onClickListener.onClick(v);
                    }
                    dismissWithAnimation();
                });
                buttonRow.addView(btn);
            }
            root.addView(buttonRow);
        }

        // Outside tap dismiss
        if (cancelableOutside) {
            root.setOnClickListener(v -> dismissWithAnimated());
        }

        rootView = root;
        setContentView(root, new android.widget.FrameLayout.LayoutParams(
                dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
    }

    private void collectInputResults() {
        Map<Integer, String> results = new HashMap<>();
        for (int i = 0; i < inputs.size() && i < inputViews.size(); i++) {
            DialogueInput input = inputs.get(i);
            EditText et = inputViews.get(i);
            String value = et.getText() != null ? et.getText().toString() : "";
            // Validate
            if (input.maxLength > 0 && value.length() > input.maxLength) {
                input.error = "Maximum " + input.maxLength + " characters";
                et.setError(input.error);
            } else if (input.validator != null) {
                String err = input.validator.apply(value);
                if (err != null) {
                    input.error = err;
                    et.setError(err);
                } else {
                    input.error = null;
                    input.result = value;
                    results.put(i, value);
                }
            } else {
                input.error = null;
                input.result = value;
                results.put(i, value);
            }
        }
        if (onInputResultListener != null && !results.isEmpty()) {
            onInputResultListener.onInputResult(this, results);
        }
    }

    private void configureWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND
            );
        }
    }

    private void applyAnimation() {
        // We handle animation in onShow via post
    }

    @Override
    public void show() {
        super.show();

        if (rootView != null) {
            rootView.post(() -> animation.animateEnter(rootView));
        }
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
    }

    private void dismissWithAnimated() {
        if (rootView != null) {
            boolean animated = animation.animateExit(rootView, this::dismiss);
            if (!animated) {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    private void dismissWithAnimation() {
        dismissWithAnimated();
    }

    @Override
    public void dismiss() {
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }
        super.dismiss();
    }

    /**
     * Get the validated input values. Call this after the dialog is dismissed.
     */
    public Map<Integer, String> getInputResults() {
        Map<Integer, String> results = new HashMap<>();
        for (int i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).result != null) {
                results.put(i, inputs.get(i).result);
            }
        }
        return results;
    }

    /**
     * Return the Dialogue instance for chaining.
     */
    public Dialogue getDialogue() {
        return this;
    }

    private int getButtonTextColor(DialogueAction.Style actionStyle) {
        switch (actionStyle) {
            case PRIMARY:
                return this.style.primaryButtonTextColor;
            case DESTRUCTIVE:
                return this.style.destructiveButtonTextColor;
            case SECONDARY:
                return this.style.secondaryButtonTextColor;
            case NEUTRAL:
            default:
                return this.style.neutralButtonTextColor;
        }
    }
}
