package com.cytophone.services.telephone;

import io.reactivex.subjects.BehaviorSubject;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import android.telecom.Call;
import android.util.Log;

public class OngoingCall {
    static {
        _callback = new Call.Callback() {
            public void onStateChanged(@NotNull Call call, int newState) {
                Log.d("D/Cellcom.OngoingCall", "onStateChanged");
                if (null != call) {
                    OngoingCall.INSTANCE.getState().onNext(newState);
                }
            }
        };
        _state = BehaviorSubject.create();
        INSTANCE = new OngoingCall();
    }

    @NotNull
    public final BehaviorSubject getState() {
        return _state;
    }

    @Nullable
    public final Call getCall() {
        return _call;
    }

    public final void setCall(@Nullable Call value) {
        if (null != _call) {
            _call.unregisterCallback((Call.Callback)_callback);
        }

        if (null != value ) {
            value.registerCallback((Call.Callback)_callback);
            _state.onNext(value.getState());
        }
        _call = value;
    }

    public final void answer() {
        if (null != _call) {
            _call.answer(0);
        }
    }

    public final void hangup() {
        if (null != _call) {
            _call.disconnect();
        }
    }

    public final void reject() {
        if (null != _call) {
            _call.reject(false,"ds");
        }
    }

    @NotNull private static BehaviorSubject<Integer> _state;
    private static Call.Callback _callback;
    public static OngoingCall INSTANCE;
    @Nullable private static Call _call;
}