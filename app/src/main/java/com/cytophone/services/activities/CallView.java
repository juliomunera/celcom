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
    private Runnable dialer = new Runnable() {
        @Override
        public void run() {
            if( CallView.this.getIntent().getData() != null ) {
                String callerNumber = CallView.this.getIntent().getData().getSchemeSpecificPart();
                PartyEntity caller = CytophoneApp.getPartyHandlerDB().
                        searchSuscriber(callerNumber);

                if( caller != null ) {
                    TextView tvw = (TextView)CallView.this.findViewById(R.id.tv_name);
                    String text = caller.getName();
                    tvw.setText(text);
                } else {
                    OngoingCall.INSTANCE.hangup();
                }
            }
        }
    };
    // Override methods declaration
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_view);

        this._handler.removeCallbacks(dialer);
        this._handler.postDelayed(dialer, 100);

        /*if( this.getIntent().getData() != null ) {
        this._number = this.getIntent().getData().getSchemeSpecificPart();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            ,@NonNull String permissions[]
            ,@NonNull int[] grantResults) {
        if( requestCode == REQUEST_CODE_ASK_PERMISSIONS ) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    String msg = "El permiso '" + permissions[index] +
                            "' no está autorizado.";
                    // Exit the app if one permission is not granted.
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        ImageView answer = findViewById(R.id.iv_answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OngoingCall.INSTANCE.answer(); }
        });

        ImageView hangup = findViewById(R.id.iv_hangout);
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OngoingCall.INSTANCE.hangup(); }
        });

        BehaviorSubject states = OngoingCall.INSTANCE.getState();
        Function1<Integer, Unit> updateUI = new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer state) {
                CallView.this.updateUI(state);
                return null;
            }
        };

        Disposable subscribers = states.subscribe(new CustomerConsumer(updateUI));
        DisposableKt.addTo(subscribers, this._disposables);

        subscribers = OngoingCall.INSTANCE.getState().
                filter( i -> i.equals(Call.STATE_DISCONNECTED)).
                delay(1L,TimeUnit.SECONDS).
                firstElement().
                subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception { CallView.this.finish(); }
                });
        DisposableKt.addTo(subscribers, this._disposables);
    }
    // Private and protected methods declaration
    @SuppressLint({"SetTextI18n"})
    private final void updateUI(int state) {
        TextView tvw = (TextView)this.findViewById(R.id.tv_name);
        String text = StringsKt.capitalize(CallStateStringKt.asString(state).toLowerCase());
        tvw.setText(text);

        Boolean flag = state == 2;
        ImageView btn = (ImageView)this.findViewById(R.id.iv_answer);
        btn.setVisibility( flag ? 0: 8);

        flag = CollectionsKt.listOf(new Integer[] { 1, 2, 4 }).contains(state);
        btn = (ImageView)this.findViewById(R.id.iv_hangout);
        btn.setVisibility( flag ? 0: 8);
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

    public static void start(@NotNull Context context, @NotNull Call call) {
        @SuppressLint("WrongConstant")
        Intent i1 = new Intent(context, CallView.class).setFlags(268435456);
        Call.Details cd = call.getDetails();

        Intent i2 = i1.setData(cd.getHandle());
        context.startActivity(i2);
    }
    // Checks the dynamically-controlled permissions and requests missing permissions from end user.
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();

        // Check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
        // Request all missing permissions
            final String[] permissions = missingPermissions.toArray(
                    new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS,
                    REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }
    // Fields declarations
    // Permissions that need to be explicitly requested from end user.
    private final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
    };

    //region fields declaration
    //Permissions request code.
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private final CompositeDisposable _disposables = new CompositeDisposable();
    private Handler _handler = new Handler();

    //Caller phone number
    private String _number;
    //endregion
}