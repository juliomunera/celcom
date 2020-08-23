package com.cytophone.services.handlers;

import com.cytophone.services.utilities.Utils;
import com.cytophone.services.dao.Persistence;
import com.cytophone.services.dao.PartyDAO;
import com.cytophone.services.entities.*;

import androidx.lifecycle.LiveData;

import android.annotation.*;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class PartyHandler implements IHandler {
    public PartyHandler(Persistence db) {
        this._partyDAO = db.partyDAO();
    }

    private PartyEntity createParty(SMSEntity message, Integer roleID) {
        try {
            PartyEntity party = message.getPartyObject();
            party.setCodedNumber(Utils.encodeBase64(party.getNumber()));
            party.setRoleID(roleID);

            return party;
        } catch (Exception e) {
            Log.e( this.TAG + ".createParty", "error: " + e.getMessage());
            return null;
        }
    }

    private void mergeParty(Integer action, Integer roleID, SMSEntity message) {
        try {
            PartyEntity party = createParty(message, roleID);
            if( null != party ) {
                EventEntity event = message.getEventObject(); //party.getNumber());
                event.setAPartyNumber(Utils.encodeBase64(event.getAPartyNumber()));
                event.setBPartyNumber(Utils.encodeBase64(event.getBPartyNumber()));

                new mergePartyAsyncTask(action, this._partyDAO).execute(party, event);
            }
        } catch (Exception e) {
            Log.e( this.TAG + ".mergeParty", "error: " + e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private static class mergePartyAsyncTask extends AsyncTask<IEntityBase, Void, Void> {
        mergePartyAsyncTask(Integer action, PartyDAO DAO) {
            this._cudAction = action;
            this._partyDAO = DAO;
        }

        private void execute(PartyEntity party, EventEntity event) {
            if( 3 == this._cudAction )  this._partyDAO.delete(party, event);
            else if( 1 == this._cudAction )  this._partyDAO.add(party, event);
        }

        @Override
        protected Void doInBackground(final IEntityBase... entities) {
            try {
            //if (null != partyDAO.getPartByNumberAndRole(message[0].getSourceNumber(), roleID)) {
                execute((PartyEntity) entities[0], (EventEntity) entities[1]);
            //}
            } catch (Exception e) {
                Log.e(this.TAG + ".doInBackground", "error: " + e.getMessage());
            }
            return null;
        }

        final String TAG = "mergePartyAsyncTask";
        PartyDAO _partyDAO;
        Integer _cudAction;
    }

    public void deleteAuthorizator(SMSEntity message) {
        mergeParty(3, 1, message);
    }

    public void deleteSuscriber(SMSEntity message) {
        mergeParty(3, 2, message);
    }

    public void insertSuscriber(SMSEntity message) {
        mergeParty (1, 2, message);
    }

    public void insertAuthorizator(SMSEntity message) {
        mergeParty (1, 1, message);
    }

    public PartyEntity searchSuscriber(String number) {
        String codedNumber = Utils.encodeBase64(number);
        return _partyDAO.getPartByNumberAndRole(codedNumber,2);
    }

    public PartyEntity searchSuscriberByName(String name) {
        return _partyDAO.getPartyByName(name);
    }

    //region fields declaration
    final String TAG = "PartyHandler";

    LiveData<List<PartyEntity>> _allParties;
    PartyDAO _partyDAO;
    //endregion
}
