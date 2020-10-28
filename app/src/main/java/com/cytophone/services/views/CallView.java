package com.cytophone.services.views;

import com.cytophone.services.utilities.CallStateStringKt;
import com.cytophone.services.telephone.OngoingCall;
import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.R;

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
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_view);
        Object party;

        if(null != (party = getIntent().getSerializableExtra("PartyEntity"))) {
            this._party = party instanceof PartyEntity ? (PartyEntity) party: null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.configAnswerButton();
        this.configHangUpButton();

        this.configAcceptCall();
        this.configFinishCall();
    }

    private final void configAcceptCall() {
        _disposables.add( OngoingCall.INSTANCE.getState().subscribe( new Consumer<Integer>() {
            @Override
            public void accept(Integer value) throws Exception {
                if( null == CallView.this._party ) { // End call
                    OngoingCall.INSTANCE.reject();
                    OngoingCall.INSTANCE.hangup();
                } else {  // Accept call and show caller info
                    updateUI(value);
                }
            }
        }));
    }

    private final void configAnswerButton() {
        ImageView answer = findViewById(R.id.iv_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OngoingCall.INSTANCE.answer();
            }
        });
    }

    private final void configFinishCall() {
        _disposables.add(OngoingCall.INSTANCE.getState()
                .filter(i -> i.equals(Call.STATE_DISCONNECTING) ||
                        i.equals(Call.STATE_DISCONNECTED))
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        CallView.this.finish();
                    }
                }));
    }

    private final void configHangUpButton() {
        ImageView hangup = findViewById(R.id.iv_hangup);
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OngoingCall.INSTANCE.hangup();
            }
        });
    }

    // Private and protected methods declaration
    @SuppressLint({"SetTextI18n"})
    private Consumer<? super Integer> updateUI(Integer state) {
        TextView tvw = (TextView)CallView.this.findViewById(R.id.tv_name);
        tvw.setText(this._party.getName());
        tvw.setSelected(true);

        tvw = (TextView)this.findViewById(R.id.tv_action);
        String text = StringsKt.capitalize(CallStateStringKt.asString(state).toLowerCase());
        tvw.setText(text);

        Boolean flag = state == 2;
        ImageView btn = (ImageView)this.findViewById(R.id.iv_answer);
        btn.setVisibility( flag ? View.VISIBLE: View.GONE);

        flag = CollectionsKt.listOf(new Integer[] { 1, 2, 4 }).contains(state);
        btn = (ImageView)this.findViewById(R.id.iv_hangup);
        btn.setVisibility( flag ? View.VISIBLE: View.GONE);
        return null;
    }

    protected void onStop() {
        super.onStop();
        this._disposables.clear();
    }

    public static void start(@NotNull Context context
            ,@NotNull Call call
            ,PartyEntity party) {
        @SuppressLint("WrongConstant")
        Intent i = new Intent(context, CallView.class).setFlags(268435456);
        i.putExtra("PartyEntity", party);
        context.startActivity(i);
    }

    //region fields declaration
    //Permissions request code.
    private final CompositeDisposable _disposables = new CompositeDisposable();

    // Caller info.
    private PartyEntity _party;
    //endregion
}