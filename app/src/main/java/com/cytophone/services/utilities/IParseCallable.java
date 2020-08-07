package com.cytophone.services.utilities;

import org.json.JSONException;
import org.json.JSONArray;

interface IParseCallable<T> {
    T call(JSONArray jsonArray, int index) throws JSONException;
}
