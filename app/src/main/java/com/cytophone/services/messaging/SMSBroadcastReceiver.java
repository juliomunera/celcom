package com.cytophone.services.messaging;

import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.CytophoneApp;
import com.cytophone.services.handlers.*;

import android.content.BroadcastReceiver;
import android.telephony.SmsMessage;
import android.provider.Telephony;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Log.d("D/CellComm", "SMSBroadcastReceiver.onReceiveSMS");
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                InternalStructure dto = new InternalStructure(smsMessage, context);

                if(executeAction(dto.getHandler(), dto.getAction(), dto.getEntity())) {
                    notifyMessage(context, dto.getEntity());
                }
            }
        }
    }

    private boolean executeAction(IHandler handler, String methodName, SMSEntity arguments) {
        try {
            Log.d("D/CellComm", "SMSBroadcastReceiver.executeAction" );
            Method action = handler.getClass().getMethod(methodName,
                    new Class[] { SMSEntity.class } );
            action.invoke(handler, new Object[]{ arguments });
            return true;
        } catch (Exception e) {
            Log.e("E/CellComm", "SMSBroadcastReceiver.executeAction -> " +
                    e.getMessage());
            return false;
        }
    }

    private void notifyMessage(Context context, SMSEntity message) {
        try {
            Log.d("D/CellComm", "SMSBroadcastReceiver.notifyMessage");

            String name = message.getActionName() + message.getTypeName();
            Intent intent = new Intent("CELLCOM_MESSAGE_CONTACTMGMT");

            intent.putExtra( "action", name );
            intent.putExtra( "data", message );
            context.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("E/CellComm", "SMSBroadcastReceiver.notifyMessage -> "
                    + e.getMessage());
        }
    }

    private class InternalStructure {
        public InternalStructure(SmsMessage message, Context context) {
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
                CytophoneApp app = (CytophoneApp)this._context.getApplicationContext();
                String type = this._entity.getTypeName();

                return  type.toLowerCase().equals( "authorizator" ) ||
                        type.toLowerCase().equals( "suscriber")
                        ? app.getPartyHandlerDB()
                        : app.getUnlockHandlerDB();
            } catch (Exception e) {
                Log.e("E/CellComm", "SMSBroadcastReceiver.getHandler -> "
                        + e.getMessage());
                return null;
            }
        }

        public SMSEntity _entity;
        public Context _context;
    }
}