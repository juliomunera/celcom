package com.cytophone.services;

import androidx.lifecycle.AndroidViewModel;
import com.cytophone.services.entities.*;
import com.cytophone.services.dao.*;
import androidx.lifecycle.LiveData;
import android.app.Application;
import android.os.AsyncTask;
import java.util.List;

public class MessageViewHandler extends AndroidViewModel {
    public  MessageViewHandler (Application application) {
        super(application);

        Persistence db = Persistence.getInstance(application);
        unlockCodeDAO = db.unlockCodeDAO();
        partyDAO = db.partyDAO();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //LiveData<List<PartyEntity>> getAllParties() {
    //    return allParties;
    //}

    public void insertSuscriber(SMSEntity message) {
        new upsertPartyAsyncTask(partyDAO,2).execute(message);
    }

    public void insertAuthorizator(SMSEntity message) {
        new upsertPartyAsyncTask(partyDAO,1).execute(message);
    }

    public void insertUnlockCode(SMSEntity message) {
        new insertUnlockCodeAsyncTask(unlockCodeDAO).execute(message);
    }

    private static class upsertPartyAsyncTask extends AsyncTask<SMSEntity, Void, Void> {
        upsertPartyAsyncTask(PartyDAO DAO, Integer role) { roleID = role; partyDAO = DAO; };

        @Override
        protected Void doInBackground(final SMSEntity ... message) {
            try {
                //if (null != partyDAO.getPartByNumberAndRole(message[0].getSourceNumber(), roleID)) {
                    PartyEntity party = message[0].getPartyObject();
                    EventEntity event = new EventEntity(message[0].getSourceNumber(),
                            party.getNumber(),
                            "SMS",
                            message[0].getMesageDate());
                    party.setRoleID(roleID);
                    partyDAO.add(party, event);
                //}
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        private PartyDAO partyDAO;
        private Integer roleID;
    }

    private static class insertUnlockCodeAsyncTask extends AsyncTask<SMSEntity, Void, Void> {
        insertUnlockCodeAsyncTask(UnlockCodeDAO DAO) { unlockCodeDAO = DAO; };

        @Override
        protected Void doInBackground(final SMSEntity ... message) {
            try {
                //if(null != unlockCodeDAO.getPartByNumberAndRole(message[0].getSourceNumber())) {
                UnlockCodeEntity unLockCode = message[0].getUnlockCodeObject();
                EventEntity event = new EventEntity(message[0].getSourceNumber(),
                        unLockCode.getMsisdn(),
                        "SMS",
                        message[0].getMesageDate());
                unlockCodeDAO.add(unLockCode, event);
                //}
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
        private UnlockCodeDAO unlockCodeDAO;
    }

    private LiveData<List<PartyEntity>> allParties;
    private UnlockCodeDAO unlockCodeDAO;
    private PartyDAO partyDAO;
}