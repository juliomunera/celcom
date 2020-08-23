package com.cytophone.services.handlers;

import com.cytophone.services.entities.*;
import com.cytophone.services.dao.*;
import com.cytophone.services.utilities.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

public class UnlockCodeHandler implements IHandler {
    public UnlockCodeHandler(Persistence db) {
        this._unlockCodeDAO = db.unlockCodeDAO();
    }

    private UnlockCodeEntity createUnlockCode(SMSEntity message) {
        try {
            UnlockCodeEntity unLockCode = message.getUnlockCodeObject();
            unLockCode.setMsisdn(Utils.encodeBase64(unLockCode.getMsisdn()));
            return unLockCode;
        } catch (Exception e) {
            Log.e(this.TAG + ".createUnlockCode", "error: " + e.getMessage());
            return null;
        }
    }

    private static class insertUnlockCodeAsyncTask extends AsyncTask<IEntityBase, Void, Void> {
        insertUnlockCodeAsyncTask(UnlockCodeDAO DAO) {
            this._unlockCodeDAO = DAO;
        }

        @Override
        protected Void doInBackground(final IEntityBase... entities) {
            try {
            //if(null != unlockCodeDAO.getPartByNumberAndRole(message[0].getSourceNumber())) {
                this._unlockCodeDAO.add((UnlockCodeEntity) entities[0]
                        ,(EventEntity) entities[1]);
            //}
            } catch (Exception e) {
                Log.e(this.TAG + ".doInBackground", "error: " + e.getMessage());
            }
            return null;
        }

        final String TAG = "insertUnlockCodeAsyncTask";
        UnlockCodeDAO _unlockCodeDAO;
    }

    public void insertUnlockCode(SMSEntity message) {
        try {
            UnlockCodeEntity unLockCode = createUnlockCode(message);
            if (null != unLockCode) {
                EventEntity event = message.getEventObject(); // unLockCode.getMsisdn()
                event.setAPartyNumber(Utils.encodeBase64(event.getAPartyNumber()));
                event.setBPartyNumber(Utils.encodeBase64(event.getBPartyNumber()));

                new insertUnlockCodeAsyncTask(this._unlockCodeDAO).execute(unLockCode, event);
            }
        } catch (Exception e) {
            Log.e( this.TAG + ".insertUnlockCode","error: " + e.getMessage());
        }
    }

    //region fields declaration
    final String TAG = "UnlockCodeHandler";
    UnlockCodeDAO _unlockCodeDAO;
    //endregion
}