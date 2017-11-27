package cl.uilabs.botiquindelascondes.db;

import android.content.Context;
import android.os.AsyncTask;

import cl.uilabs.botiquindelascondes.models.Medicamento;

/**
 * Created by jose on 27-11-17.
 */

public class InsertMedicamentosTask extends AsyncTask<Void,Void,Boolean> {

    private Medicamento medicamento;
    private Context context;

    public InsertMedicamentosTask(Medicamento medicamento,Context context)
    {
        this.medicamento=medicamento;
        this.context=context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        AppDatabase.getDatabase(context.getApplicationContext()).medicamentoDAO().insertMedicamento(medicamento);
        return true;
    }

}