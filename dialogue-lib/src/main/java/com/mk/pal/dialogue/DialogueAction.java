package com.mk.pal.dialogue;

import android.view.View;

/**
 * Button model for Dialogue actions.
 */
public class DialogueAction {

    public enum Style { PRIMARY, SECONDARY, DESTRUCTIVE, NEUTRAL }

    public String text;
    public Style style = Style.NEUTRAL;
    public boolean enabled = true;
    public View.OnClickListener onClickListener;

    public DialogueAction(String text, Style style, View.OnClickListener listener) {
        this.text = text;
        this.style = style;
        this.onClickListener = listener;
    }

    public static DialogueAction primary(String text, View.OnClickListener listener) {
        return new DialogueAction(text, Style.PRIMARY, listener);
    }

    public static DialogueAction secondary(String text, View.OnClickListener listener) {
        return new DialogueAction(text, Style.SECONDARY, listener);
    }

    public static DialogueAction destructive(String text, View.OnClickListener listener) {
        return new DialogueAction(text, Style.DESTRUCTIVE, listener);
    }

    public static DialogueAction neutral(String text, View.OnClickListener listener) {
        return new DialogueAction(text, Style.NEUTRAL, listener);
    }
}
