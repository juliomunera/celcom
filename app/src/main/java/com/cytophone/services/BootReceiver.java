package com.cytophone.services;

import com.cytophone.services.activities.ContactView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, ContactView.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
