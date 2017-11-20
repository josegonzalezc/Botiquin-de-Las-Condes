package cl.uilabs.botiquindelascondes.Requests;




import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

import android.provider.SyncStateContract;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import cl.uilabs.botiquindelascondes.listeners.VolleyStringCallback;


/**
 * Created by jose on 17-11-17.
 */

public class VolleyGetElements {

    public static void volleyGetElements(final VolleyStringCallback callback,
                                        Context context) {

        final String url = "https://www.lascondes.cl/salud/destacados/el-botiquin-de-las-condes.html";

        final RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String  response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //params.put(Constants.Q_CONTENTTYPE,Constants.Q_CONTENTTYPE_JSON);
                //params.put(Constants.Q_AUTHORIZATION,userToken);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

}

