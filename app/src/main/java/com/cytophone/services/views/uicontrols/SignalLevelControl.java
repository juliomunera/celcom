package com.cytophone.services.views.uicontrols;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.res.ResourcesCompat;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;

import android.annotation.SuppressLint;
import androidx.annotation.StyleRes;

import android.content.res.Resources;
import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;

import com.cytophone.services.R;

public class SignalLevelControl extends AppCompatImageButton {
    public SignalLevelControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public SignalLevelControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SignalLevelControl(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        try {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper
                    (context, R.style.defaultSignalLevelScene);
            changeTheme(wrapper.getTheme());
        } catch(Exception ex) {
            Log.e(TAG + ".initialize", "error: " + ex.getMessage());
        }
    }

    private void changeTheme(
            @SuppressLint("SupportAnnotationUsage")
            @StyleRes final Resources.Theme theme
    ) {
        final Drawable drawable = ResourcesCompat.getDrawable(getResources()
                , R.drawable.icon_signal_level
                , theme);
        this.setImageDrawable(drawable);
    }

    public void setLevel(int level) {
        Log.i(this.TAG + ".setLevel", "" + level);

        final Resources.Theme theme = getResources().newTheme();

        if(level == SIGNAL_STRENGTH_GREAT) {
            theme.applyStyle(R.style.defaultSignalLevelScene, false);
        } else if(level == SIGNAL_STRENGTH_GOOD) {
            theme.applyStyle(R.style.goodSignalLevelScene, false);
        } else if(level == SIGNAL_STRENGTH_MODERATE) {
            theme.applyStyle(R.style.moderateSignalLevelScene, false);
        } else if(level == SIGNAL_STRENGTH_POOR) {
            theme.applyStyle(R.style.weakSignalLevelScene, false);
        } else if( level == SIGNAL_STRENGTH_NONE_OR_UNKNOWN ){
            theme.applyStyle(R.style.unKnowSignalLevelScene, false);
        }
        changeTheme(theme);
    }

    private static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    private static final int SIGNAL_STRENGTH_MODERATE = 2;
    private static final int SIGNAL_STRENGTH_GREAT = 4;
    private static final int SIGNAL_STRENGTH_GOOD = 3;
    private static final int SIGNAL_STRENGTH_POOR = 1;

    final String TAG = "SignalLevelControl";
}