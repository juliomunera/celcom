package com.cytophone.services.fragments;

import com.cytophone.services.entities.IEntityBase;

public interface IFragment {
    void applyChanges(String action, IEntityBase message);
    int getID();
}
