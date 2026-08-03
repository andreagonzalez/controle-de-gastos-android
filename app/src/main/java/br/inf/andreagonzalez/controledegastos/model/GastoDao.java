package br.inf.andreagonzalez.controledegastos.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GastoDao {

    @Query("SELECT * FROM gasto ORDER BY data ASC")
    List<Gasto> listarGastosOrdenados();

    @Insert
    void inserirGasto(Gasto gasto);

    @Update
    void atualizarGasto(Gasto gasto);

    @Delete
    void removerGasto(Gasto gasto);
}
