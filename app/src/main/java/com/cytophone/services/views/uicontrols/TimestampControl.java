package com.cytophone.services.views.uicontrols;

import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.content.Context;
import java.util.Calendar;
import android.os.Handler;

public class TimestampControl extends AppCompatTextView {
    public TimestampControl(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        this.initialize(context);
    }

    public TimestampControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize(context);
    }

    public TimestampControl(Context context) {
        super(context);
        this.initialize(context);
    }

    private void initialize(Context context) {
        handler.removeCallbacks(update);
        setText(getCurrentTime());
        handler.postDelayed(update, 1000); // 60000 a minute
    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return (hour <= 9 ? "0" + hour : hour) + ":" +
                (minute <= 9 ? "0" + minute : minute) + " " +
                (c.get(Calendar.AM_PM) == Calendar.PM ? "pm":"am");
    }

    private Runnable update = new Runnable() {
        @Override
        public void run() {
            setText(getCurrentTime());
            handler.postDelayed(this, 1000);
        }
    };

    private Handler handler = new Handler();
}
