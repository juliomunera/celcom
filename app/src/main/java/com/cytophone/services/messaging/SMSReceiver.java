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
        try {
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                Log.d(this.TAG + ".onReceive", "processing");
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    InternalClass dto = new InternalClass(smsMessage, context);

                    if (executeAction(dto.getHandler(), dto.getAction(), dto.getEntity())) {
                        notifyMessage(context, dto.getEntity());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.TAG + ".onReceive", e.getMessage());
        }
    }

    private boolean executeAction(IHandler handler, String methodName, SMSEntity arguments) {
        try {
            Log.d(this.TAG + ".executeAction", methodName );
            Method action = handler.getClass().getMethod(methodName,
                    new Class[] { SMSEntity.class } );
            action.invoke(handler, new Object[]{ arguments });
            return true;
        } catch (Exception e) {
            Log.e(this.TAG + ".executeAction",  e.getMessage());
            return false;
        }
    }

    private void notifyMessage(Context context, SMSEntity message) {
        try {
            Log.d(this.TAG + ".notifyMessage", "");
            String name = message.getActionName() + message.getTypeName();
            String msgType = "CELLCOMM_MESSAGE_CODEMGMT";

            if( name.contains("Suscriber") || name.contains("Authorizator") ) {
                msgType = "CELLCOMM_MESSAGE_CONTACTMGMT";
            }

            context.sendBroadcast( new Intent(msgType).
                    putExtra("action", name).
                    putExtra("data", message));
        } catch (Exception e) {
            Log.e(this.TAG + ".notifyMessage", e.getMessage());
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
                        : app.getCodeHandlerDB();
            } catch (Exception e) {
                Log.e(this.TAG + ".getHandler", e.getMessage());
                return null;
            }
        }

        private final String TAG = "SMSBroadcastReceiver.InternalClass";
        private SMSEntity _entity;
        private Context _context;
    }

    private final String TAG = "SMSBroadcastReceiver";
}