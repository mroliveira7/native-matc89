package com.mateus.tripadvisorapi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mateus on 21/12/17.
 */

public interface AsyncResponse {
    public void processFinish(JSONObject response) throws JSONException;
}
