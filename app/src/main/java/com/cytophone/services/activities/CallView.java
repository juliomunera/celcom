package com.cytophone.services.activities;

import com.cytophone.services.utilities.CallStateStringKt;
import com.cytophone.services.telephone.OngoingCall;


import com.cytophone.services.CytophoneApp;

import com.cytophone.services.entities.PartyEntity;
import com.cytophone.services.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxkotlin.DisposableKt;
import io.reactivex.functions.Consumer;

import org.jetbrains.annotations.NotNull;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;
import kotlin.Unit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;
import android.view.View;

import android.content.pm.PackageManager;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import android.content.Context;
import android.telecom.Call;
import android.os.Bundle;
import android.Manifest;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallView extends AppCompatActivity {
    // Override methods declaration
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_view);
        Object o;

        if( (o = getIntent().getSerializableExtra("PartyEntity")) != null) {
            this._party = o instanceof  PartyEntity ? (PartyEntity) o: null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ImageView answer = findViewById(R.id.iv_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OngoingCall.INSTANCE.answer();
            }
        });

        ImageView hangup = findViewById(R.id.iv_hangout);
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OngoingCall.INSTANCE.hangup();
            }
        });

        BehaviorSubject states = OngoingCall.INSTANCE.getState();
        Function1<Integer, Unit> updateUI = new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer state) {
                if( null == CallView.this._party ) { // End call
                    OngoingCall.INSTANCE.reject();
                    OngoingCall.INSTANCE.hangup();
                } else {  // Show caller info
                    CallView.this.updateUI(state);
                }
                return null;
            }
        };

        Disposable subscribers = states.subscribe(new CustomerConsumer(updateUI));
        DisposableKt.addTo(subscribers, this._disposables);

        subscribers = OngoingCall.INSTANCE.getState().
                filter(i -> i.equals(Call.STATE_DISCONNECTING) ||
                       i.equals(Call.STATE_DISCONNECTED)).
                delay(1L, TimeUnit.SECONDS).
                firstElement().
                subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        CallView.this.finish();
                    }
                });
        //STATE_DISCONNECTED
        DisposableKt.addTo(subscribers, this._disposables);
    }

    // Private and protected methods declaration
    @SuppressLint({"SetTextI18n"})
    private final void updateUI(int state) {
        TextView tvw = (TextView)CallView.this.findViewById(R.id.tv_name);
        tvw.setText(this._party.getName());

        tvw = (TextView)this.findViewById(R.id.tv_action);
        String text = StringsKt.capitalize(CallStateStringKt.asString(state).toLowerCase());
        tvw.setText(text);

        Boolean flag = state == 2;
        ImageView btn = (ImageView)this.findViewById(R.id.iv_answer);
        btn.setVisibility( flag ? View.VISIBLE: 8);

        flag = CollectionsKt.listOf(new Integer[] { 1, 2, 4 }).contains(state);
        btn = (ImageView)this.findViewById(R.id.iv_hangout);
        btn.setVisibility( flag ? View.VISIBLE: 8);
    }

    protected void onStop() {
        super.onStop();
        this._disposables.clear();
    }

    private final class CustomerConsumer implements Consumer {
        public CustomerConsumer(Function1 func) { this._func = func; }

        @Override
        public void accept(Object o) throws Exception { this._func.invoke(o); }
        private final Function1 _func;
    }

    public static void start(@NotNull Context context,
                             @NotNull Call call,
                             PartyEntity party) {
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