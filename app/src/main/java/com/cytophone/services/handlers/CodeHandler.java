package com.cytophone.services.handlers;

import com.cytophone.services.entities.*;
import com.cytophone.services.dao.*;
import com.cytophone.services.utilities.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

public class CodeHandler implements IHandler {
    public CodeHandler(Persistence db) {
        this._codeDAO = db.codeDAO();
    }

    private CodeEntity createCode(SMSEntity message) {
        try {
            CodeEntity code = message.getCodeObject();
            code.setMsisdn(Utils.encodeBase64(code.getMsisdn()));
            return code;
        } catch (Exception e) {
            Log.e(this.TAG + ".createCode", "error: " + e.getMessage());
            return null;
        }
    }

    private static class insertCodeAsyncTask extends AsyncTask<IEntityBase, Void, Void> {
        insertCodeAsyncTask(CodeDAO DAO) {
            this._codeDAO = DAO;
        }

        @Override
        protected Void doInBackground(final IEntityBase... entities) {
            try {
            //if(null != unlockCodeDAO.getPartByNumberAndRole(message[0].getSourceNumber())) {
                this._codeDAO.add((CodeEntity) entities[0]
                        ,(EventEntity) entities[1]);
            //}
            } catch (Exception e) {
                Log.e(this.TAG + ".doInBackground", "error: " + e.getMessage());
            }
            return null;
        }

        final String TAG = "insertCodeAsyncTask";
        CodeDAO _codeDAO;
    }

    private void insertCode(SMSEntity message) {
        try {
            CodeEntity code = createCode(message);
            if (null != code) {
                EventEntity event = message.getEventObject(); // unLockCode.getMsisdn()
                event.setAPartyNumber(Utils.encodeBase64(event.getAPartyNumber()));
                event.setBPartyNumber(Utils.encodeBase64(event.getBPartyNumber()));
                new insertCodeAsyncTask(this._codeDAO).execute(code, event);
            }
        } catch (Exception e) {
            Log.e( this.TAG + ".insertUnlockCode","error: " + e.getMessage());
        }
    }

    public void insertActivationCode(SMSEntity message) { insertCode(message); }
    public void insertDeactivationCode(SMSEntity message) { insertCode(message); }
    public void insertSuspendCode(SMSEntity message) { insertCode(message); }
    public void insertUnlockCode(SMSEntity message) { insertCode(message); }

    //region fields declaration
    final String TAG = "CodeHandler";
    CodeDAO _codeDAO;
    //endregion
}