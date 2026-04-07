package com.mk.pal.dialogue;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for creating and showing Dialogue dialogs.
 */
public class DialogueBuilder {

    private final Context context;
    private String title;
    private String message;
    private final List<DialogueAction> actions = new ArrayList<>();
    private final List<DialogueInput> inputs = new ArrayList<>();
    private DialogueStyle style = new DialogueStyle();
    private DialogueLayout layout = new DialogueLayout();
    private DialogueAnimation animation = new DialogueAnimation();
    private boolean cancelable = true;
    private boolean cancelableOutside = false;
    private View customView;
    private boolean customViewBeneathTitle = false;

    private DialogueCallbacks.OnShowListener onShowListener;
    private DialogueCallbacks.OnDismissListener onDismissListener;
    private DialogueCallbacks.OnActionListener onActionListener;
    private DialogueCallbacks.OnInputResultListener onInputResultListener;

    public DialogueBuilder(Context context) {
        this.context = context;
    }

    // ── Title / Message ──

    public DialogueBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogueBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    // ── Buttons ──

    public DialogueBuilder addButton(String text, DialogueAction.Style style, View.OnClickListener listener) {
        actions.add(new DialogueAction(text, style, listener));
        return this;
    }

    public DialogueBuilder addAction(DialogueAction action) {
        actions.add(action);
        return this;
    }

    // ── Inputs ──

    public DialogueBuilder addInput(DialogueInput input) {
        inputs.add(input);
        return this;
    }

    // ── Style / Layout / Animation ──

    public DialogueBuilder setStyle(DialogueStyle style) {
        this.style = style;
        return this;
    }

    public DialogueBuilder setLayout(DialogueLayout layout) {
        this.layout = layout;
        return this;
    }

    public DialogueBuilder setAnimation(DialogueAnimation animation) {
        this.animation = animation;
        return this;
    }

    // ── Cancelable ──

    public DialogueBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogueBuilder setCancelableOutside(boolean cancelableOutside) {
        this.cancelableOutside = cancelableOutside;
        return this;
    }

    // ── Custom View ──

    /**
     * Attach a fully custom view (WebView, ImageView, etc.) beneath the title.
     */
    public DialogueBuilder setCustomView(View customView) {
        this.customView = customView;
        this.customViewBeneathTitle = true;
        return this;
    }

    /**
     * Attach a fully custom view after message (replaces body content).
     */
    public DialogueBuilder setCustomViewAfterMessage(View customView) {
        this.customView = customView;
        this.customViewBeneathTitle = false;
        return this;
    }

    // ── Callbacks ──

    public DialogueBuilder setOnShowListener(DialogueCallbacks.OnShowListener listener) {
        this.onShowListener = listener;
        return this;
    }

    public DialogueBuilder setOnDismissListener(DialogueCallbacks.OnDismissListener listener) {
        this.onDismissListener = listener;
        return this;
    }

    public DialogueBuilder setOnActionListener(DialogueCallbacks.OnActionListener listener) {
        this.onActionListener = listener;
        return this;
    }

    public DialogueBuilder setOnInputResultListener(DialogueCallbacks.OnInputResultListener listener) {
        this.onInputResultListener = listener;
        return this;
    }

    // ── Build & Show ──

    public Dialogue build() {
        return new Dialogue(context, title, message, actions, inputs, style, layout, animation,
                cancelable, cancelableOutside, onShowListener, onDismissListener, onActionListener,
                onInputResultListener, customView, customViewBeneathTitle);
    }

    public Dialogue show() {
        Dialogue dialogue = build();
        dialogue.show();
        return dialogue;
    }

    // ── Preset factories ──

    /**
     * Simple alert dialog with title, message, and OK button.
     */
    public static DialogueBuilder alert(Context ctx, String title, String message) {
        return new DialogueBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .addButton("OK", DialogueAction.Style.PRIMARY, null);
    }

    /**
     * Loading/progress dialog with spinner and no buttons.
     */
    public static DialogueBuilder loading(Context ctx, String message) {
        return new DialogueBuilder(ctx)
                .setMessage(message)
                .setCancelable(false)
                .setCancelableOutside(false);
    }

    /**
     * Input dialog with a single text field and OK/Cancel buttons.
     */
    public static DialogueBuilder input(Context ctx, String title, String hint) {
        return new DialogueBuilder(ctx)
                .setTitle(title)
                .addInput(new DialogueInput(hint))
                .addButton("OK", DialogueAction.Style.PRIMARY, null)
                .addButton("Cancel", DialogueAction.Style.SECONDARY, null);
    }

    /**
     * Info dialog with title, message, and a dismiss button.
     */
    public static DialogueBuilder info(Context ctx, String title, String message) {
        return new DialogueBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .addButton("Dismiss", DialogueAction.Style.NEUTRAL, null);
    }

    /**
     * Confirmation dialog with OK and Cancel buttons.
     */
    public static DialogueBuilder confirm(Context ctx, String title, String message,
                                          View.OnClickListener onOk) {
        return new DialogueBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setCancelableOutside(false)
                .addButton("OK", DialogueAction.Style.PRIMARY, onOk)
                .addButton("Cancel", DialogueAction.Style.SECONDARY, v -> {});
    }

    /**
     * Destructive confirmation dialog (e.g., delete confirmation).
     */
    public static DialogueBuilder destructiveConfirm(Context ctx, String title, String message,
                                                     View.OnClickListener onConfirm) {
        return new DialogueBuilder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setCancelableOutside(false)
                .addButton("Delete", DialogueAction.Style.DESTRUCTIVE, onConfirm)
                .addButton("Cancel", DialogueAction.Style.NEUTRAL, v -> {});
    }

}
