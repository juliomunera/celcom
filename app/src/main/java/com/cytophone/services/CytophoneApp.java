package com.cytophone.services;

import com.cytophone.services.dao.Persistence;
import com.cytophone.services.handlers.PartyHandler;
import com.cytophone.services.handlers.UnlockCodeHandler;

import android.app.Application;

public class CytophoneApp extends Application {
    //Public methods declaration
    private static Persistence persistence;

    public static CytophoneApp getInstanceApp() {
        return _instanceApp;
    }

    public static PartyHandler getPartyHandlerDB() {
        return _partyHandlerDB;
    }

    public static UnlockCodeHandler getUnlockHandlerDB() {
        return _unlockHandlerDB;
    }

    public static Persistence getPersistenceDB() { return persistence; }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeHandlers();
        initializeServices();

        setSMSDefaultApp();
    }

    //Private methods declaration
    private void initializeHandlers() {
        try {
            _instanceApp = this;

            // Persistence instanceDB = Persistence.getInstance(_instanceApp);
            persistence = Persistence.getInstance(_instanceApp);
            _unlockHandlerDB = new UnlockCodeHandler(persistence);
            _partyHandlerDB = new PartyHandler(persistence);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeServices() {
        //Intent i = new Intent(this, CallService.class);
        //startService(i);
    }

    private void setSMSDefaultApp(){

    }

    //Fields declaration
    private static CytophoneApp _instanceApp;
    private static UnlockCodeHandler _unlockHandlerDB;
    private static PartyHandler _partyHandlerDB;
}