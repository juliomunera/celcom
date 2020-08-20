package com.cytophone.services.handlers;

import com.cytophone.services.dao.Persistence;
import com.cytophone.services.dao.PartyDAO;
import com.cytophone.services.entities.*;
import com.cytophone.services.utilities.Utils;

import androidx.lifecycle.LiveData;
import android.annotation.*;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class PartyHandler implements IHandler {
    public PartyHandler(Persistence db) {
        _partyDAO = db.partyDAO();
    }

    private PartyEntity createParty(SMSEntity message, Integer roleID) {
        try {
            PartyEntity party = message.getPartyObject();
            party.setCodedNumber(Utils.encodeBase64(party.getNumber()));
            party.setRoleID(roleID);

            return party;
        } catch (Exception e) {
            Log.e("E/CellComm.PartyHandler", "createParty -> " + e.getMessage());
            return null;
        }
    }

    private void mergeParty(Integer action, Integer roleID, SMSEntity message) {
        try {
            PartyEntity party = createParty(message, roleID);
            if( null != party ) {
                EventEntity event = message.getEventObject(); //party.getNumber());
                new mergePartyAsyncTask(action, _partyDAO).execute(party, event);
            }
        } catch (Exception e) {
            Log.e("E/CellComm.PartyHandler", "mergeParty -> " + e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private static class mergePartyAsyncTask extends AsyncTask<IEntityBase, Void, Void> {
        mergePartyAsyncTask(Integer action, PartyDAO DAO) {
            cudAction = action;
            partyDAO = DAO;
        }

        private void execute(PartyEntity party, EventEntity event) {
            if( cudAction == 3 ) {
                partyDAO.delete(party, event);
            } else if( cudAction == 1 ) {
                partyDAO.add(party, event);
            }
        }

        @Override
        protected Void doInBackground(final IEntityBase... entities) {
            try {
            //if (null != partyDAO.getPartByNumberAndRole(message[0].getSourceNumber(), roleID)) {
                execute((PartyEntity) entities[0], (EventEntity) entities[1]);
            //}
            } catch (Exception e) {
                Log.e("E/CellComm.PartyHandler", "doInBackground -> " + e.getMessage());
            }
            return null;
        }
        private PartyDAO partyDAO;
        private Integer cudAction;
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

    private LiveData<List<PartyEntity>> _allParties;
    private PartyDAO _partyDAO;
}
