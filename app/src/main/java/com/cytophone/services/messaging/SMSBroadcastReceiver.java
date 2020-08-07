package com.cytophone.services.messaging;

import com.cytophone.services.CytophoneApp;
import com.cytophone.services.entities.SMSEntity;
import com.cytophone.services.handlers.*;

import android.content.BroadcastReceiver;
import android.telephony.SmsMessage;
import android.provider.Telephony;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("D/ClosedComm", "Receiving SMS...");

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                InternalStructure dto = new InternalStructure(smsMessage, context);
                executeAction(dto.getHandler(),dto.getAction(),dto.getEntity());

                /*
                Log.d(Constants.SMSBCR_TAG, "SMS From: " + msg.getSourceNumber() + "\n");
                Log.d(Constants.SMSBCR_TAG, "SMS Sent Date: " +
                android.text.format.DateFormat.
                getDateFormat(context).format(msg.getMesageDate()) +
                "\n");
                Log.d(Constants.SMSBCR_TAG, "Message: " + msg.getDecodeMessage() + "\n");
                */
            }
        }
    }

    private void executeAction(IHandler handler, String methodName, SMSEntity arguments) {
        try {
            Method action = handler.getClass().getMethod(methodName,
                    new Class[]{SMSEntity.class});
            action.invoke(handler, new Object[]{arguments});
        } catch (NoSuchMethodException e) {
            Log.d("D/ClosedComm", "ExecuteAction Error ->" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("D/ClosedComm", "ExecuteAction Error ->" + e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.d("D/ClosedComm", "ExecuteAction Error ->" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("D/ClosedComm", "ExecuteAction Error ->" + e.getMessage());
            e.printStackTrace();
        }
    }

    private class InternalStructure {
        public InternalStructure(SmsMessage message, Context context) {
            this.entity = new SMSEntity(message);
            this.context = context;
        }

        public SMSEntity getEntity() {
            return entity;
        }

        public String getAction() {
            return entity.getActionName() + entity.getTypeName();
        }

        public IHandler getHandler() {
            try {
                CytophoneApp app = ((CytophoneApp)
                        this.context.getApplicationContext());
                String type = entity.getTypeName();
                return type.toLowerCase().equals( "authorizator" ) ||
                        type.toLowerCase().equals( "suscriber") ?
                        app.getPartyHandlerDB() :
                        app.getUnlockHandlerDB();
            } catch (Exception ex) {
                return null;
            }
        }

        public SMSEntity entity;
        public Context context;
    }
}