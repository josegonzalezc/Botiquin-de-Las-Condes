package cl.uilabs.botiquindelascondes.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import cl.uilabs.botiquindelascondes.models.Medicamento;

/**
 * Created by jose on 27-11-17.
 */

public class GetMedicamentosTask extends AsyncTask<Void,Void,List<Medicamento>> {


    private Context context;
    private List<Medicamento> medicamentoList;


    public GetMedicamentosTask(Context context)
    {
        this.context=context;
    }


    @Override
    protected List<Medicamento> doInBackground(Void... params) {
        medicamentoList = AppDatabase.getDatabase(context.getApplicationContext()).medicamentoDAO().getAllMedicamentos();
        return medicamentoList;
    }


}
