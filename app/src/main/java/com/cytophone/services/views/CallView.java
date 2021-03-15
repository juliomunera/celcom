package com.cytophone.services.views;

import com.cytophone.services.utilities.CallStateStringKt;
import com.cytophone.services.telephone.OngoingCall;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.R;
import com.cytophone.services.utilities.Constants;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import org.jetbrains.annotations.NotNull;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.admin.DevicePolicyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import android.telecom.Call;
import android.os.Bundle;

public class CallView extends AppCompatActivity {
    // Override methods declaration
    @Override
    public void onBackPressed() {
        Log.d(this.TAG+ ".onBackPressed", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CallView._isActive = false;
        Log.d(this.TAG + ".onDestroy", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowsFlags();
        setContentView(R.layout.activity_call_view);

        setParty();

        CallView._isActive = true;
        Log.d(this.TAG, "OnCreated.");
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.setAnswerButton();
        this.setHangUpButton();

        this.setAcceptCall();
        this.setFinishCall();
    }

    private final void setAcceptCall() {
        try {
            _disposables.add(OngoingCall.INSTANCE.getState().subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer value) throws Exception {
                    if (null == CallView.this._party) { // End call
                        OngoingCall.INSTANCE.reject();
                        OngoingCall.INSTANCE.hangup();
                    } else {  // Accept call and show caller info
                        updateUI(value);
                    }
                }
            }));
        }catch (Exception ex) {
            ex.printStackTrace();
            Log.e( CallView.TAG + ".setAcceptCall", ex.getMessage() );
        }
    }

    private final void setAnswerButton() {
        try {
            ImageView answer = findViewById(R.id.iv_answer);
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OngoingCall.INSTANCE.answer();
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
            Log.e( CallView.TAG + ".setAnswerButton", ex.getMessage() );
        }
    }

    private final void setFinishCall() {
        try {
            _disposables.add(OngoingCall.INSTANCE.getState()
                .filter(i -> i.equals(Call.STATE_DISCONNECTING) ||
                        i.equals(Call.STATE_DISCONNECTED))
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(CallView.this.TAG, "OnFinishCall.");
                        //CallView.this.stopLockTask();
                        CallView.this.finish();
                    }
                }));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(CallView.TAG + ".setFinishCall", ex.getMessage());
        }
    }

    private final void setHangUpButton() {
        try {
            ImageView hangup = findViewById(R.id.iv_hangup);
            hangup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(CallView.this.TAG, "OnHangUp.");
                    OngoingCall.INSTANCE.reject();
                    OngoingCall.INSTANCE.hangup();
                    //CallView.this.stopLockTask();
                    CallView.this.finish();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(CallView.TAG + ".setHangUpButton", ex.getMessage());
        }
    }

    public static Boolean isActive(){
        return CallView._isActive;
    }

    // Private and protected methods declaration
    @SuppressLint({"SetTextI18n"})
    private Consumer<? super Integer> updateUI(Integer state) {
        try {
            TextView tvw = (TextView) CallView.this.findViewById(R.id.tv_name);
            tvw.setText(this._party.getName());
            tvw.setSelected(true);

            tvw = (TextView) this.findViewById(R.id.tv_action);
            String text = StringsKt.capitalize(CallStateStringKt.asString(state).toLowerCase());
            tvw.setText(text);

            Boolean flag = state == 2;
            ImageView btn = (ImageView) this.findViewById(R.id.iv_answer);
            btn.setVisibility(flag ? View.VISIBLE : View.GONE);

            flag = CollectionsKt.listOf(new Integer[]{1, 2, 4}).contains(state);
            btn = (ImageView) this.findViewById(R.id.iv_hangup);
            btn.setVisibility(flag ? View.VISIBLE : View.GONE);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(CallView.TAG + ".updateUI", ex.getMessage());
        } finally {
            return null;
        }
    }

    protected void onStop() {
        super.onStop();
        this._disposables.clear();
    }

    private void setWindowsFlags(){
        this.getWindow().setFlags(Constants.SCREEN_FLAGS, Constants.SCREEN_FLAGS);
    }

    private void setParty(){
        Object party;

        if(null != (party = getIntent().getSerializableExtra("PartyEntity"))) {
            this._party = party instanceof PartyEntity ? (PartyEntity) party: null;
        }
    }

    public static void start(@NotNull Context context
            ,@NotNull Call call
            ,PartyEntity party) {
        try {
            @SuppressLint("WrongConstant")
            Intent i = new Intent(context, CallView.class).setFlags(268435456);
            i.putExtra("PartyEntity", party);
            context.startActivity(i);
        }catch (Exception ex) {
            ex.printStackTrace();
            Log.e(CallView.TAG + ".start", ex.getMessage());
        }
    }

    //region fields declaration
    //Permissions request code.
    private final CompositeDisposable _disposables = new CompositeDisposable();
    // Caller info.
    private PartyEntity _party = null;

    private static final String TAG = "CallView";
    private static Boolean _isActive = false;
    //endregion
}