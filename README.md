# Dialogue

A lightweight Android library for building rich, customizable dialogs programmatically — no XML required. Features a fluent builder API, pre-built dialog presets, platform-inspired themes (Apple, Samsung OneUI, Material 3), and support for complex content like forms, lists, images, and web views.

## Features

- **Fluent Builder API** — construct dialogs with a readable, chainable API
- **Zero XML** — all views are built programmatically, keeping the library self-contained
- **Preset Dialogs** — one-liners for common patterns: alert, info, confirm, destructive confirm, loading, input
- **Platform Themes** — Apple/iOS, Samsung OneUI, Material 3, and Minimal outline styles out of the box
- **Rich Content** — embed HTML text, images (via Glide), WebViews, progress bars, ratings, checkbox lists, and radio lists
- **Custom Inputs** — text, number, password, email, phone, and multiline fields with validation
- **Animations** — fade, slide-up, scale, or none with configurable duration
- **Dark Theme** — built-in dark mode support
- **Builder Playground** — a visual dialog designer in the demo app with live Java code generation

## Screenshots

### Demo Showcase

Scrollable list of 30+ dialog examples covering presets, custom styling, platform themes, rich content, and animations.

### Builder Playground

Interactive form to configure every aspect of a dialog — title, message, buttons, inputs, colors, corner radii, padding, animations, and custom content — with a live preview and auto-generated Java code you can copy to clipboard.

## Quick Start

### Basic Alert

```java
new DialogueBuilder(context)
    .setTitle("Update Available")
    .setMessage("A new version is available. Would you like to update now?")
    .addButton("Update", DialogueAction.Style.PRIMARY, () -> { /* handle */ })
    .addButton("Later", DialogueAction.Style.SECONDARY, () -> { /* handle */ })
    .build()
    .show();
```

### Preset Dialogs

```java
// Simple alert
DialogueBuilder.alert(context, "Title", "Message").show();

// Info dialog
DialogueBuilder.info(context, "Title", "Message").show();

// Confirm dialog
DialogueBuilder.confirm(context, "Title", "Message")
    .onConfirm(() -> { /* confirmed */ })
    .onCancel(() -> { /* cancelled */ })
    .show();

// Destructive confirm
DialogueBuilder.destructiveConfirm(context, "Delete Item", "This action cannot be undone.")
    .onConfirm(() -> { /* delete */ })
    .show();

// Loading dialog
DialogueBuilder.loading(context, "Loading...").show();

// Input dialog with validation
DialogueBuilder.input(context, "Enter Email", "Email address")
    .setValidator(input -> input.contains("@") ? null : "Invalid email")
    .onResult((input, result) -> { /* handle result */ })
    .show();
```

### Custom Styling

```java
DialogueBuilder builder = new DialogueBuilder(context)
    .setTitle("Custom Dialog")
    .setMessage("This dialog has custom styling.")
    .addButton("OK", DialogueAction.Style.PRIMARY, () -> {});

builder.setStyle()
    .setBackgroundColor(0xFF1A1A2E)
    .setTitleColor(0xFFEAEAEA)
    .setMessageColor(0xFFB0B0B0)
    .setCornerRadius(20f)
    .apply();

builder.setAnimation()
    .setEnterType(DialogueAnimation.Type.SCALE)
    .setExitType(DialogueAnimation.Type.FADE)
    .setDuration(300)
    .apply();

builder.build().show();
```

### Rich Content

```java
// Image from URL (requires Glide in your project)
builder.setCustomContent("image", "https://example.com/photo.jpg");

// WebView content
builder.setCustomContent("webview", "https://example.com");

// Rich HTML text
builder.setCustomContent("html", "<b>Bold</b> and <i>italic</i> text");

// Progress bar
builder.setCustomContent("progress", null);
```

### Callbacks

```java
Dialogue dialog = new DialogueBuilder(context)
    .setTitle("Example")
    .setMessage("Watch the callbacks fire.")
    .addButton("OK", DialogueAction.Style.PRIMARY, () -> {})
    .setOnShowListener(() -> Log.d("Dialogue", "Dialog shown"))
    .setOnDismissListener(() -> Log.d("Dialogue", "Dialog dismissed"))
    .setOnActionListener((action, index) -> Log.d("Dialogue", "Clicked: " + action.getText()))
    .build();

dialog.show();
```

## Installation

### Gradle

Include the `dialogue-lib` module in your project:

**settings.gradle**
```gradle
include ':dialogue-lib'
```

**app/build.gradle**
```gradle
dependencies {
    implementation project(':dialogue-lib')
}
```

### Requirements

- Min SDK 24 (Android 7.0)
- Java 11+

### Dependencies

The library itself has no external dependencies beyond:

- `androidx.appcompat:appcompat`
- `com.google.android.material:material`

## API Reference

### DialogueBuilder

| Method | Description |
|---|---|
| `setTitle(String)` | Set the dialog title |
| `setMessage(String)` | Set the dialog message |
| `addButton(text, style, listener)` | Add a button with style and click listener |
| `addInput(input)` | Add a custom input field |
| `setStyle()` | Get the `DialogueStyle` builder for theming |
| `setLayout()` | Get the `DialogueLayout` builder for layout config |
| `setAnimation()` | Get the `DialogueAnimation` builder for animations |
| `setCustomContent(type, data)` | Set rich content (image, webview, html, progress) |
| `setOnShowListener()` | Set callback for when dialog is shown |
| `setOnDismissListener()` | Set callback for when dialog is dismissed |
| `setOnActionListener()` | Set callback for button clicks |
| `setOnInputResultListener()` | Set callback for input results |
| `build()` | Build and return the `Dialogue` instance |

### DialogueAction.Style

| Style | Typical Use |
|---|---|
| `PRIMARY` | Main affirmative action (OK, Yes, Update) |
| `SECONDARY` | Secondary/cancel action (Cancel, Later) |
| `DESTRUCTIVE` | Destructive action (Delete, Remove) |
| `NEUTRAL` | Tertiary action (Learn More, Remind Me) |

### DialogueInput.Type

| Type | Description |
|---|---|
| `TEXT` | Standard text input |
| `NUMBER` | Numeric keyboard |
| `PASSWORD` | Password field (masked) |
| `EMAIL` | Email keyboard with validation |
| `PHONE` | Phone keyboard |
| `MULTILINE` | Multi-line text area |

### DialogueAnimation.Type

| Type | Description |
|---|---|
| `FADE` | Fade in/out |
| `SLIDE_UP` | Slide up from bottom (enter), slide down (exit) |
| `SCALE` | Scale up from center |
| `NONE` | No animation |

## Project Structure

```
Dialogue/
├── app/                          # Demo application
│   └── src/main/java/.../
│       ├── MainActivity.java          # Dialog showcase (30+ examples)
│       └── AlertDialogBuilderActivity.java # Visual builder playground
├── dialogue-lib/                 # Library module
│   └── src/main/java/.../
│       ├── Dialogue.java              # Core dialog class
│       ├── DialogueBuilder.java       # Fluent builder API
│       ├── DialogueStyle.java         # Style constants and configuration
│       ├── DialogueAction.java        # Button model
│       ├── DialogueInput.java         # Input field model
│       ├── DialogueLayout.java        # Layout configuration
│       ├── DialogueAnimation.java     # Animation configuration
│       ├── DialogueCallbacks.java     # Listener interfaces
│       └── ViewFactory.java           # Programmatic UI component factory
└── gradle/
    └── libs.versions.toml        # Version catalog
```

## License

This project is available for personal and commercial use.
