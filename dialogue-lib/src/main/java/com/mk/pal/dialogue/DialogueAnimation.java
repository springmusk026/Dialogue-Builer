package com.mk.pal.dialogue;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Programmatic animation configuration and execution for Dialogue dialogs.
 * Uses ViewPropertyAnimator and ObjectAnimator instead of resource-based animations.
 */
public class DialogueAnimation {

    public enum Type { FADE, SLIDE_UP, SLIDE_DOWN, SCALE, NONE }

    public Type enterType = Type.FADE;
    public Type exitType = Type.FADE;
    public long duration = 200; // ms

    /**
     * Apply enter animation to the dialog's root view after it's shown.
     */
    public void animateEnter(View rootView) {
        switch (enterType) {
            case FADE:
                rootView.setAlpha(0f);
                rootView.animate()
                        .alpha(1f)
                        .setDuration(duration)
                        .start();
                break;
            case SLIDE_UP:
                rootView.setAlpha(0f);
                rootView.setTranslationY(ViewFactory.dpToPx(rootView.getContext(), 50));
                rootView.animate()
                        .alpha(1f)
                        .translationY(0)
                        .setDuration(duration)
                        .start();
                break;
            case SLIDE_DOWN:
                rootView.setAlpha(0f);
                rootView.setTranslationY(-ViewFactory.dpToPx(rootView.getContext(), 50));
                rootView.animate()
                        .alpha(1f)
                        .translationY(0)
                        .setDuration(duration)
                        .start();
                break;
            case SCALE:
                rootView.setScaleX(0.8f);
                rootView.setScaleY(0.8f);
                rootView.setAlpha(0f);
                rootView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(duration)
                        .start();
                break;
            case NONE:
            default:
                rootView.setAlpha(1f);
                break;
        }
    }

    /**
     * Apply exit animation and return true if caller should delay dismiss until animation completes.
     */
    public boolean animateExit(View rootView, Runnable onEnd) {
        if (exitType == Type.NONE) return false;
        switch (exitType) {
            case FADE:
                rootView.animate()
                        .alpha(0f)
                        .setDuration(duration)
                        .withEndAction(onEnd)
                        .start();
                return true;
            case SLIDE_UP:
            case SLIDE_DOWN:
                float targetY = (exitType == Type.SLIDE_UP)
                        ? -ViewFactory.dpToPx(rootView.getContext(), 50)
                        : ViewFactory.dpToPx(rootView.getContext(), 50);
                rootView.animate()
                        .alpha(0f)
                        .translationY(targetY)
                        .setDuration(duration)
                        .withEndAction(onEnd)
                        .start();
                return true;
            case SCALE:
                ObjectAnimator sx = ObjectAnimator.ofFloat(rootView, View.SCALE_X, 0.8f);
                ObjectAnimator sy = ObjectAnimator.ofFloat(rootView, View.SCALE_Y, 0.8f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(rootView, View.ALPHA, 0f);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(sx, sy, alpha);
                set.setDuration(duration);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) { onEnd.run(); }
                });
                set.start();
                return true;
            case NONE:
            default:
                return false;
        }
    }
}
