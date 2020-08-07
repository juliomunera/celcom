package com.cytophone.services.activities;

import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.cytophone.services.R;

public class MakeCall extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private int mInterval = 400;
    private boolean initialState = true;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_call);

        mTextView = (TextView) findViewById(R.id.txtaction);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                doTask();
            }
        };

        mHandler.postDelayed(mRunnable,mInterval);
    }


    protected void doTask(){
        if(initialState){
            initialState = false;
            mTextView.setTextColor(Color.parseColor("#BA6B1D"));
        }else {
            initialState = true;
            mTextView.setTextColor(Color.TRANSPARENT);
        }

        mHandler.postDelayed(mRunnable,mInterval);
    }

}
