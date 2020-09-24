package com.cytophone.services.messaging;

import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.CellCommApp;
import com.cytophone.services.handlers.*;

import android.content.BroadcastReceiver;
import android.telephony.SmsMessage;
import android.provider.Telephony;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Log.d(this.TAG + ".onReceive","processing" );
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                InternalClass dto = new InternalClass(smsMessage, context);

                if(executeAction(dto.getHandler(), dto.getAction(), dto.getEntity())) {
                    notifyMessage(context, dto.getEntity());
                }
            }
        }
    }

    private boolean executeAction(IHandler handler, String methodName, SMSEntity arguments) {
        try {
            Log.d(this.TAG + ".executeAction","" );
            Method action = handler.getClass().getMethod(methodName,
                    new Class[] { SMSEntity.class } );
            action.invoke(handler, new Object[]{ arguments });
            return true;
        } catch (Exception e) {
            Log.e(this.TAG + ".executeAction", "error: " + e.getMessage());
            return false;
        }
    }

    private void notifyMessage(Context context, SMSEntity message) {
        try {
            Log.d(this.TAG + ".notifyMessage", "");

            String name = message.getActionName() + message.getTypeName();
            Intent intent = new Intent("CELLCOMM_MESSAGE_CONTACTMGMT").
                    putExtra( "action", name ).
                    putExtra( "data", message );
            context.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(this.TAG + ".notifyMessage", "error: " + e.getMessage());
        }
    }

    private class InternalClass {
        public InternalClass(SmsMessage message, Context context) {
            this._entity = new SMSEntity(message);
            this._context = context;
        }

        public SMSEntity getEntity() {
            return this._entity;
        }

        public String getAction() {
            return this._entity.getActionName() + this._entity.getTypeName();
        }

        public IHandler getHandler() {
            try {
                CellCommApp app = (CellCommApp)this._context.getApplicationContext();
                String type = this._entity.getTypeName();

                return  type.toLowerCase().equals( "authorizator" ) ||
                        type.toLowerCase().equals( "suscriber")
                        ? app.getPartyHandlerDB()
                        : app.getUnlockHandlerDB();
            } catch (Exception e) {
                Log.e(this.TAG + ".getHandler", "error: " + e.getMessage());
                return null;
            }
        }

        final String TAG = "SMSBroadcastReceiver.InternalClass";
        SMSEntity _entity;
        Context _context;
    }

    final String TAG = "SMSBroadcastReceiver";
}