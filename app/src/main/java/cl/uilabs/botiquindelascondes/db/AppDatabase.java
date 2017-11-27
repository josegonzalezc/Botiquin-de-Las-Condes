package cl.uilabs.botiquindelascondes.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import cl.uilabs.botiquindelascondes.models.Medicamento;

/**
 * Created by jose on 25-11-17.
 */

@Database(entities = {Medicamento.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "user_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract MedicamentoDAO medicamentoDAO();

    //TODO Add MedicamentoListViewModel with AsyncTasks to query the DB
}
