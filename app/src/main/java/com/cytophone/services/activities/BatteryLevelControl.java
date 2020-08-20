package com.cytophone.services.activities;

import androidx.appcompat.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.content.Context;
import android.util.Log;

import com.cytophone.services.R;

public class BatteryLevelControl extends AppCompatImageButton {
    public BatteryLevelControl(Context context) {
        super(context);
        super.setImageResource(R.drawable.ic_battery_full_black_24dp);
    }

    public BatteryLevelControl(Context context, AttributeSet attributes) {
        super(context, attributes);
        super.setImageResource(R.drawable.ic_battery_full_black_24dp);
    }

    public BatteryLevelControl(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        super.setImageResource(R.drawable.ic_battery_full_black_24dp);
    }

    public void setLevel(Float level) {
        Log.i("D/CellComm.BatteryLevelImage", "setLevel -> " + level);

        if (level <= 20) {
            super.setImageResource(R.drawable.ic_battery_20_black_24dp);
        } else if (level <= 30) {
            super.setImageResource(R.drawable.ic_battery_30_black_24dp);
        } else if (level <= 50) {
            super.setImageResource(R.drawable.ic_battery_50_black_24dp);
        } else if (level <= 60) {
            super.setImageResource(R.drawable.ic_battery_60_black_24dp);
        } else if (level <= 80) {
            super.setImageResource(R.drawable.ic_battery_80_black_24dp);
        } else if (level <= 90) {
            super.setImageResource(R.drawable.ic_battery_90_black_24dp);
        } else {
            super.setImageResource(R.drawable.ic_battery_full_black_24dp);
        }
    }
}