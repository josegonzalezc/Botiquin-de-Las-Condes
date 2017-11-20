package cl.uilabs.botiquindelascondes.listeners;

import com.android.volley.VolleyError;

/**
 * Created by jose on 17-11-17.
 */

public interface VolleyStringCallback {
    void onSuccess(String result);
    void onErrorResponse(VolleyError result);
}
