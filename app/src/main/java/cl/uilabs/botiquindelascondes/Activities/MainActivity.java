package cl.uilabs.botiquindelascondes.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.os.Handler;

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
import cl.uilabs.botiquindelascondes.db.GetMedicamentosTask;
import cl.uilabs.botiquindelascondes.listeners.VolleyStringCallback;
import cl.uilabs.botiquindelascondes.models.Connectivity;
import cl.uilabs.botiquindelascondes.models.Medicamento;

import android.view.*;
import android.widget.Toast;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    ArrayList<Medicamento> medicamentosList;
    MedicamentosAdapter medicamentosAdapter;
    private ListView medicamentos_listview_List;
    private ProgressBar pb;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity mainActivity;
    private Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivity = this;
        mainContext = getApplicationContext();

        medicamentos_listview_List = (ListView) findViewById(R.id.medicamentos_list);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_medicamentos);
        medicamentosList=new ArrayList<Medicamento>();
        setSwipeRefreshLayout();

        updateAndroidSecurityProvider(this);

        //Toast.makeText(getApplicationContext(),"Obteniendo medicamentos...",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable(){
            @Override
            public void run()
            {

                //Toast.makeText(MainActivity.this,"Obteniendo medicamentos...",Toast.LENGTH_SHORT);
                Log.i("REQ","Obtiendo medicamentos..");

                VolleyGetElements.volleyGetElements(new VolleyStringCallback() {
                    @Override
                    public void onSuccess(String result) {
                        //mTextView.setText("Response is: "+ response);

                        final String finalResult = result;
                        VolleyGetElements.parseResponse(finalResult,medicamentosList,mainContext);
                        mainActivity.runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             medicamentosAdapter = new MedicamentosAdapter(getApplicationContext(), medicamentosList);
                                                             medicamentos_listview_List.setAdapter(medicamentosAdapter);
                                                             Toast.makeText(MainActivity.this,"Medicamentos obtenidos.",Toast.LENGTH_SHORT).show();

                                                         }
                                                     });



                        Log.i("VOLLEY","TERMINE");

                    }

                    @Override
                    public void onErrorResponse(VolleyError result) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    medicamentosList = (ArrayList<Medicamento>) new GetMedicamentosTask(mainContext).execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        medicamentosAdapter = new MedicamentosAdapter(getApplicationContext(), medicamentosList);
                                        medicamentos_listview_List.setAdapter(medicamentosAdapter);
                                        Log.i("VOLLEY", "TERMINE");
                                        Toast.makeText(MainActivity.this, "No hay conexión, usando base de datos local.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).start();


                    }
                },getApplicationContext());



            }
        }).start();

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

    public void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean is_connected = Connectivity.isConnected(getApplicationContext());
                if (is_connected) {

                            Toast.makeText(MainActivity.this, "Obteniendo medicamentos...", Toast.LENGTH_SHORT).show();


                            VolleyGetElements.volleyGetElements(new VolleyStringCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    //mTextView.setText("Response is: "+ response);
                                    final String finalResult = result;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            VolleyGetElements.parseResponse(finalResult, medicamentosList,mainContext);

                                            mainActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    medicamentosAdapter = new MedicamentosAdapter(getApplicationContext(), medicamentosList);
                                                    medicamentos_listview_List.setAdapter(medicamentosAdapter);
                                                    Log.i("VOLLEY", "TERMINE");
                                                    Toast.makeText(MainActivity.this, "Lista de medicamentos actualizada.", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }).start();
                                }

                                @Override
                                public void onErrorResponse(VolleyError result) {
                                    Toast.makeText(getApplicationContext(), "No se pudo actualizar. Revisa tu conexión a internet.", Toast.LENGTH_LONG).show();
                                    Log.i("VOLLEY", "ERROR");


                                }
                            }, getApplicationContext());
                    swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                medicamentosList = (ArrayList<Medicamento>) new GetMedicamentosTask(mainContext).execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    medicamentosAdapter = new MedicamentosAdapter(getApplicationContext(), medicamentosList);
                                    medicamentos_listview_List.setAdapter(medicamentosAdapter);
                                    Log.i("VOLLEY", "TERMINE");
                                    Toast.makeText(MainActivity.this, "No hay conexión, utilizando base de datos local.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).start();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

        });
    }

}
