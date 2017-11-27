package cl.uilabs.botiquindelascondes.db;

/**
 * Created by jose on 25-11-17.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import cl.uilabs.botiquindelascondes.models.Medicamento;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * @autho - Jose
 @versión - hoy

 */

@Dao
public interface MedicamentoDAO {

    @Query("select * from Medicamento")
    List<Medicamento> getAllMedicamentos();

    @Query("SELECT COUNT(*) FROM Medicamento")
    int medicamentoCount();

    @Insert(onConflict = REPLACE)
    void insertMedicamento(Medicamento medicamento);

    @Delete
    void deleteMedicamento(Medicamento medicamento);

    @Query("DELETE FROM Lesson")
    void nukeTable();
}
