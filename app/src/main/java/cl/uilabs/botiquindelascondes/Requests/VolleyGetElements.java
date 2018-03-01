package cl.uilabs.botiquindelascondes.Requests;




import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.provider.SyncStateContract;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cl.uilabs.botiquindelascondes.listeners.VolleyStringCallback;
import cl.uilabs.botiquindelascondes.models.Medicamento;
import cl.uilabs.botiquindelascondes.db.InsertMedicamentosTask;


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

    public static void parseResponse(String response,ArrayList<Medicamento> medicamentoArrayList,Context context)
    {

        List<Medicamento> medicamentosList = new ArrayList<Medicamento>();
        Document js = Jsoup.parse(response);
        Elements child = js.select("table#medicamentos");

        ArrayList<String> downServers = new ArrayList<>();
        Element table = js.select("table#medicamentos").first();
        child = null;//select the first table.
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            Medicamento m = new Medicamento();
            m.setLaboratorio(cols.get(0).text());
            m.setNombre(cols.get(1).text());
            m.setPrecio_normal(cols.get(2).text());
            m.setDescuento(cols.get(3).text());
            m.setPrecio_rebajado(cols.get(4).text());
            medicamentosList.add(m);

            try {
                new InsertMedicamentosTask (m,context).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("MED","INSERT");
        }

        medicamentoArrayList.clear();
        medicamentoArrayList.addAll(medicamentosList);
    }

}

