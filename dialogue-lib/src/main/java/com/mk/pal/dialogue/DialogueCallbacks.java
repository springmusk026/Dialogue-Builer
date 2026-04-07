package com.mk.pal.dialogue;

import java.util.Map;

/**
 * Listener interfaces for Dialogue dialog lifecycle and user interaction events.
 */
public class DialogueCallbacks {

    public interface OnShowListener {
        void onShow(Dialogue dialogue);
    }

    public interface OnDismissListener {
        void onDismiss(Dialogue dialogue);
    }

    public interface OnActionListener {
        void onAction(Dialogue dialogue, DialogueAction action, int index);
    }

    public interface OnInputResultListener {
        void onInputResult(Dialogue dialogue, Map<Integer, String> inputValues);
    }
}
