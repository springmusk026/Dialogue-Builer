package com.mk.pal.dialogue;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;

/**
 * Static factory for creating UI components programmatically without XML resources.
 */
final class ViewFactory {

    private ViewFactory() {}

    static GradientDrawable createRoundedBackground(Context ctx, int color, float radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dpToPx(ctx, radiusDp));
        drawable.setColor(color);
        return drawable;
    }

    static TextView createTitleTextView(Context ctx, String text, int textColor, float textSizeSp) {
        TextView tv = new TextView(ctx);
        tv.setText(text != null ? text : "");
        tv.setTextColor(textColor);
        tv.setTextSize(textSizeSp);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dpToPx(ctx, 8);
        tv.setLayoutParams(lp);
        return tv;
    }

    static TextView createMessageTextView(Context ctx, String text, int textColor, float textSizeSp) {
        TextView tv = new TextView(ctx);
        tv.setText(text != null ? text : "");
        tv.setTextColor(textColor);
        tv.setTextSize(textSizeSp);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        return tv;
    }

    static Button createButton(Context ctx, String text, int textColor, GradientDrawable bgDrawable, int paddingDp) {
        Button btn = new Button(ctx);
        btn.setText(text != null ? text : "");
        btn.setTextColor(textColor);
        int pad = dpToPx(ctx, paddingDp);
        btn.setPadding(pad, pad / 2, pad, pad / 2);
        btn.setBackground(bgDrawable);
        btn.setAllCaps(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        btn.setLayoutParams(lp);
        return btn;
    }

    static EditText createInputEditText(Context ctx, String hint, int inputType, int textColor, float textSizeSp) {
        EditText et = new EditText(ctx);
        et.setHint(hint != null ? hint : "");
        et.setInputType(inputType);
        et.setTextColor(textColor);
        et.setTextSize(textSizeSp);
        int pad = dpToPx(ctx, 12);
        et.setPadding(pad, pad, pad, pad);
        et.setBackground(createRoundedBackground(ctx, Color.parseColor("#F5F5F5"), 8));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dpToPx(ctx, 4);
        lp.bottomMargin = dpToPx(ctx, 4);
        et.setLayoutParams(lp);
        return et;
    }

    static ProgressBar createProgressBar(Context ctx) {
        ProgressBar pb = new ProgressBar(ctx);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                dpToPx(ctx, 48), dpToPx(ctx, 48));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = dpToPx(ctx, 16);
        lp.bottomMargin = dpToPx(ctx, 16);
        pb.setLayoutParams(lp);
        return pb;
    }

    static ScrollView wrapInScrollView(Context ctx, View view) {
        ScrollView sv = new ScrollView(ctx);
        sv.setFillViewport(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        sv.setLayoutParams(lp);
        sv.addView(view);
        return sv;
    }

    static GradientDrawable createButtonBackground(Context ctx, DialogueAction.Style style, DialogueStyle s) {
        int bgColor;
        switch (style) {
            case PRIMARY:
                bgColor = s.primaryButtonBg;
                break;
            case SECONDARY:
                bgColor = s.secondaryButtonBg;
                break;
            case DESTRUCTIVE:
                bgColor = s.destructiveButtonBg;
                break;
            case NEUTRAL:
            default:
                bgColor = s.neutralButtonBg;
                break;
        }
        return createRoundedBackground(ctx, bgColor, s.buttonCornerRadius);
    }

    static View createDivider(Context ctx, int color) {
        View divider = new View(ctx);
        divider.setBackgroundColor(color);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(ctx, 1));
        divider.setLayoutParams(lp);
        return divider;
    }

    static int dpToPx(Context ctx, float dp) {
        return (int) (dp * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }
}
