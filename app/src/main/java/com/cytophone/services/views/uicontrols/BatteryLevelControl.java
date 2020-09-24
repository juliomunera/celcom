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

public class BatteryLevelControl extends AppCompatImageButton {
    //region constructors declaration
    public BatteryLevelControl(Context context) {
        super(context);
        this.initialize(context);
    }

    public BatteryLevelControl(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.initialize(context);
    }

    public BatteryLevelControl(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        this.initialize(context);
    }
    //endregion constructors methods declaration


    private void initialize(Context context) {
        try {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper
                    (context, R.style.defaultBatteryLevelScene);
            changeTheme(wrapper.getTheme());
        } catch(Exception ex) {
            Log.e(TAG + ".initialize", "error: " + ex.getMessage());
        }
    }

    private void changeTheme(@SuppressLint("SupportAnnotationUsage")
                             @StyleRes final Resources.Theme theme) {
        final Drawable drawable = ResourcesCompat.getDrawable(getResources()
                , R.drawable.icon_battery_level
                , theme);
        this.setImageDrawable(drawable);
    }


    public void setLevel(Float level) {
        Log.i(this.TAG + ".setLevel", "" + level);
        final Resources.Theme theme = getResources().newTheme();

        if( level <= 3 ) {
            theme.applyStyle(R.style.poorLevelScene, false);
        } else if( level <= 33.33 ) {
            theme.applyStyle(R.style.lowLevelScene, false);
        } else if( level <= 66.66) {
            theme.applyStyle(R.style.middleLevelScene , false);
        } else {
            theme.applyStyle(R.style.defaultBatteryLevelScene, false);
        }
        changeTheme(theme);
    }

    final String TAG = "BatteryLevelControl";
}