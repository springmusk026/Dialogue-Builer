package com.mk.pal.dialogue;

import android.view.Gravity;

/**
 * Layout configuration for Dialogue dialogs.
 */
public class DialogueLayout {

    public enum ContentGravity { LEFT, CENTER, RIGHT }
    public enum WidthMode { WRAP, FIXED }

    public float padding = 24f;            // dp
    public ContentGravity contentGravity = ContentGravity.LEFT;
    public WidthMode widthMode = WidthMode.WRAP;
    public float maxWidthDp = 320f;        // max width in dp

    int getGravity() {
        switch (contentGravity) {
            case LEFT:   return Gravity.LEFT | Gravity.CENTER_VERTICAL;
            case RIGHT:  return Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            case CENTER:
            default:     return Gravity.CENTER;
        }
    }
}
