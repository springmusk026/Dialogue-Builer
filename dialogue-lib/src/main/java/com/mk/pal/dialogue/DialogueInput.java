package com.mk.pal.dialogue;

import android.text.InputType;
import java.util.function.Function;

/**
 * Input field model for Dialogue dialogs.
 */
public class DialogueInput {

    public enum Type { TEXT, NUMBER, PASSWORD, EMAIL, PHONE, MULTILINE }

    public Type type = Type.TEXT;
    public String hint = "";
    public String initialValue = "";
    public Function<String, String> validator; // returns error message or null if valid
    public int maxLength = -1; // -1 means no limit
    public boolean singleLine = true;
    // Populated after dialog: validated input value
    public String result;
    public String error;

    public DialogueInput(Type type, String hint) {
        this.type = type;
        this.hint = hint;
    }

    public DialogueInput(String hint) {
        this(Type.TEXT, hint);
    }

    public int getInputType() {
        switch (type) {
            case NUMBER:
                return InputType.TYPE_CLASS_NUMBER;
            case PASSWORD:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            case EMAIL:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            case PHONE:
                return InputType.TYPE_CLASS_PHONE;
            case MULTILINE:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
            case TEXT:
            default:
                return InputType.TYPE_CLASS_TEXT;
        }
    }
}
