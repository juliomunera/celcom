package com.cytophone.services;

import com.cytophone.services.dao.Persistence;
import com.cytophone.services.entities.CodeEntity;
import com.cytophone.services.handlers.*;
import com.cytophone.services.views.ContactView;

import android.app.Application;
import android.content.Intent;

public class CellCommApp extends Application {
    // region public methods declaration
    public static CellCommApp getInstanceApp() {
        return _instanceApp;
    }

    public static Persistence getInstanceDB() {
        return _instanceDB;
    }

    public static PartyHandler getPartyHandlerDB() {
        return _partyHandlerDB;
    }

    public static CodeHandler getCodeHandlerDB() {
        return _codeHandlerDB;
    }
    // endregion

    @Override
    public void onCreate() {
        super.onCreate();
        initializeHandlers();
        initializeServices();
    }

    //Private methods declaration
    private void initializeHandlers() {
        try {
            _instanceDB = Persistence.getInstance(this);

            _partyHandlerDB = new PartyHandler(_instanceDB);
            _codeHandlerDB = new CodeHandler(_instanceDB);
            _instanceApp = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeServices() {
        Intent i = new Intent(this, CellCommAgent.class);
        startService(i);
    }

    //region fields declaration
    private static PartyHandler _partyHandlerDB;
    private static CodeHandler _codeHandlerDB;

    private static CellCommApp _instanceApp;
    private static Persistence _instanceDB;
    //endregion
}