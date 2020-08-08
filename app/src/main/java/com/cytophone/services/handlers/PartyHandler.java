package com.cytophone.services.handlers;

import androidx.lifecycle.LiveData;
import android.annotation.*;
import android.os.AsyncTask;

import com.cytophone.services.dao.PartyDAO;
import com.cytophone.services.dao.Persistence;
import com.cytophone.services.entities.*;

import java.util.List;

public class PartyHandler implements IHandler {
    public PartyHandler(Persistence db) {
        partyDAO = db.partyDAO();
    }

    private PartyEntity createParty(SMSEntity message, Integer roleID) {
        try {
            PartyEntity party = message.getPartyObject();
            party.setRoleID(roleID);
            return party;
        } catch (Exception e) {
            return null;
        }
    }

    private EventEntity createEvent(SMSEntity message, String number) {
        return new EventEntity(message.getSourceNumber(),
                number,
                "SMS",
                message.getMesageDate());
    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //LiveData<List<PartyEntity>> getAllParties() {
    // return allParties;
    //}

    private void mergeParty(Integer action, Integer roleID, SMSEntity message) {
        PartyEntity party = createParty(message, roleID);
        if( null != party ) {
            EventEntity event = createEvent(message, party.getNumber());
            new mergePartyAsyncTask(action, partyDAO).execute(party, event);
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
            } else if( cudAction == 2 ) {
                party.setUpdateDateToCurrentDate();
                partyDAO.update(party,event);
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
            } catch (Exception ex) {
                ex.printStackTrace();
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

    public void updateSuscriber(SMSEntity message) {
        mergeParty (2, 2, message);
    }

    public void updateAuthorizator(SMSEntity message) {
        mergeParty (2, 1, message);
    }

    public PartyEntity searchSuscriber(String number){
        return partyDAO.getPartByNumberAndRole(number,2);
    }

    public PartyEntity searchSuscriberByName(String name){
        return partyDAO.getPartyByName(name);
    }

    private LiveData<List<PartyEntity>> allParties;
    private PartyDAO partyDAO;
}
