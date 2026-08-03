package br.inf.andreagonzalez.controledegastos.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EntradaDao {
    @Query("SELECT * FROM entrada ORDER BY data ASC")
    List<Entrada> listarEntradasOrdenadas();

    @Insert
    void inserirEntrada(Entrada entrada);

    @Update
    void atualizarEntrada(Entrada entrada);

    @Delete
    void removerEntrada(Entrada entrada);
}
