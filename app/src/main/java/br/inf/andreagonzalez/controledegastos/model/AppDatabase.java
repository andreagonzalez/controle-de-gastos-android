package br.inf.andreagonzalez.controledegastos.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Entrada.class, Gasto.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EntradaDao entradaDao();
    public abstract GastoDao gastoDao();

    private static AppDatabase instancia;

    public static AppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "controle_gastos_db"
            )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build(); // ⚠️ apenas para testes
        }
        return instancia;
    }
}
