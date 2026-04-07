package com.mk.pal.dialogue;

import android.graphics.Color;

/**
 * Default style constants for Dialogue. All fields are public and mutable,
 * so callers can customize every aspect of the dialog appearance.
 */
public class DialogueStyle {

    // Background
    public int backgroundColor = Color.WHITE;
    public int overlayColor = Color.parseColor("#66000000");

    // Corner & elevation
    public float cornerRadius = 16f;          // dp
    public float elevation = 8f;              // dp

    // Title
    public int titleColor = Color.parseColor("#212121");
    public float titleTextSize = 18f;         // sp

    // Message
    public int messageColor = Color.parseColor("#757575");
    public float messageTextSize = 14f;       // sp

    // Button
    public int primaryButtonBg = Color.parseColor("#1976D2");
    public int primaryButtonTextColor = Color.WHITE;
    public int secondaryButtonBg = Color.parseColor("#E0E0E0");
    public int secondaryButtonTextColor = Color.parseColor("#212121");
    public int destructiveButtonBg = Color.parseColor("#D32F2F");
    public int destructiveButtonTextColor = Color.WHITE;
    public int neutralButtonBg = Color.parseColor("#E0E0E0");
    public int neutralButtonTextColor = Color.parseColor("#212121");
    public float buttonCornerRadius = 8f;     // dp
    public int buttonPadding = 12;            // dp
    public int buttonSpacing = 8;             // dp

    // Input
    public int inputTextColor = Color.parseColor("#212121");
    public float inputTextSize = 14f;         // sp
    public int inputErrorColor = Color.parseColor("#D32F2F");

    // Divider
    public int dividerColor = Color.parseColor("#EEEEEE");

    // Loading
    public int loadingColor = Color.parseColor("#1976D2");
}
