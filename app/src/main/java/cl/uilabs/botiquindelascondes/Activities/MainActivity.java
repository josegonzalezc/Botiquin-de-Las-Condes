package cl.uilabs.botiquindelascondes.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import cl.uilabs.botiquindelascondes.R;
import cl.uilabs.botiquindelascondes.Requests.VolleyGetElements;
import cl.uilabs.botiquindelascondes.listeners.VolleyStringCallback;
import cl.uilabs.botiquindelascondes.models.Medicamento;

import android.view.*;
import android.widget.Toast;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    ArrayList<Medicamento> medicamentosList;
    MedicamentosAdapter medicamentosAdapter;
    private ListView medicamentos_listview_List;
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        medicamentos_listview_List = (ListView) findViewById(R.id.medicamentos_list);
        medicamentosList=new ArrayList<Medicamento>();



        updateAndroidSecurityProvider(this);

        //Toast.makeText(getApplicationContext(),"Obteniendo medicamentos...",Toast.LENGTH_SHORT).show();
        new Runnable(){
            @Override
            public void run()
            {

                Toast.makeText(MainActivity.this,"Obteniendo medicamentos...",Toast.LENGTH_SHORT);


                VolleyGetElements.volleyGetElements(new VolleyStringCallback() {
                    @Override
                    public void onSuccess(String result) {
                        //mTextView.setText("Response is: "+ response);
                        Document js = Jsoup.parse(result);
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

                        }

                        medicamentosAdapter = new MedicamentosAdapter(getApplicationContext(),medicamentosList);
                        medicamentos_listview_List.setAdapter(medicamentosAdapter);

                        Log.i("VOLLEY","TERMINE");


                    }

                    @Override
                    public void onErrorResponse(VolleyError result) {
                        Toast.makeText(getApplicationContext(),"No se pudo cargar la lista",Toast.LENGTH_LONG).show();
                        Log.i("VOLLEY","ERROR");


                    }
                },getApplicationContext());

                Toast.makeText(MainActivity.this,"Medicamentos obtenidos.",Toast.LENGTH_SHORT);

            }
        }.run();

        //Toast.makeText(getApplicationContext(),"Medicamentos obtenidos",Toast.LENGTH_SHORT).show();
        Log.i("MAIN",Integer.toString(medicamentosList.size()));


    }


    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                if (medicamentosAdapter !=null) {
                    medicamentosAdapter.filter(searchQuery.toString().trim());
                    medicamentos_listview_List.invalidate();
                    return true;
                }
                else return false;
            }
        });

        Log.i("MENU","pase por aca");
        return true;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Estás seguro que quieres salir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

}
