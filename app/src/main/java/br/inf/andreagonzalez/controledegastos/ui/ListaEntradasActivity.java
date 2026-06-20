package br.inf.andreagonzalez.controledegastos.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.EntradaAdapter;
import br.inf.andreagonzalez.controledegastos.model.Entrada;

public class ListaEntradasActivity extends AppCompatActivity {

    private RecyclerView recyclerEntradas;
    private EntradaAdapter adapter;

    private ArrayList<Entrada> listaEntradas =
            new ArrayList<>();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entradas);

        inicializarComponentes();
        inicializarPreferencias();
        recuperarListaEntradas();
        configurarRecyclerView();
    }

    private void inicializarComponentes() {

        recyclerEntradas =
                findViewById(R.id.recyclerEntradas);
    }

    private void inicializarPreferencias() {

        preferences = getSharedPreferences(
                "dados",
                MODE_PRIVATE
        );
    }

    private void recuperarListaEntradas() {

        Gson gson = new Gson();

        String json =
                preferences.getString(
                        "lista_entradas",
                        null
                );

        if (json != null) {

            Type type =
                    new TypeToken<ArrayList<Entrada>>() {
                    }.getType();

            listaEntradas =
                    gson.fromJson(json, type);
        }
    }

    private void configurarRecyclerView() {

        recyclerEntradas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new EntradaAdapter(
                listaEntradas
        );

        recyclerEntradas.setAdapter(adapter);
    }
}