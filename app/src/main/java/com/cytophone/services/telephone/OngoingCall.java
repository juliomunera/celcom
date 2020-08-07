package com.cytophone.services.telephone;

import io.reactivex.subjects.BehaviorSubject;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import android.telecom.Call;

public class OngoingCall {
    static {
        state = BehaviorSubject.create();
        callback = new Call.Callback() {
            public void onStateChanged(@NotNull Call call, int newState) {
                if (null != call) {
                    OngoingCall.INSTANCE.getState().onNext(newState);
                }
            }
        };
        INSTANCE = new OngoingCall();
    }

    @NotNull
    public final BehaviorSubject getState() {
        return state;
    }

    @Nullable
    public final Call getCall() {
        return call;
    }

    public final void setCall(@Nullable Call value) {
        if (null != call) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (null != value ) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }
        call = value;
    }

    public final void answer() {
        if (null != call) {
            call.answer(0);
        }
    }

    public final void hangup() {
        if (null != call) {
            call.disconnect();
        }
    }

    public final void reject() {
        if (null != call) {
            call.reject(false,"ds");
        }
    }

    @NotNull private static BehaviorSubject<Integer> state;
    private static Call.Callback callback;
    public static OngoingCall INSTANCE;
    @Nullable private static Call call;
}