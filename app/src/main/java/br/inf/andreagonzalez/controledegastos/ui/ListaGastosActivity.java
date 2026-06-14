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
import br.inf.andreagonzalez.controledegastos.adapter.GastoAdapter;
import br.inf.andreagonzalez.controledegastos.model.Gasto;

public class ListaGastosActivity extends AppCompatActivity {

    private RecyclerView recyclerGastos;
    private GastoAdapter adapter;

    private ArrayList<Gasto> listaGastos =
            new ArrayList<>();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        inicializarComponentes();
        inicializarPreferencias();
        recuperarListaGastos();
        configurarRecyclerView();
    }

    private void inicializarComponentes() {

        recyclerGastos =
                findViewById(R.id.recyclerGastos);
    }

    private void inicializarPreferencias() {

        preferences = getSharedPreferences(
                "dados",
                MODE_PRIVATE
        );
    }

    private void recuperarListaGastos() {

        Gson gson = new Gson();

        String json =
                preferences.getString(
                        "lista_gastos",
                        null
                );

        if (json != null) {

            Type type =
                    new TypeToken<ArrayList<Gasto>>() {
                    }.getType();

            listaGastos =
                    gson.fromJson(json, type);
        }
    }

    private void configurarRecyclerView() {

        recyclerGastos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new GastoAdapter(listaGastos);

        recyclerGastos.setAdapter(adapter);
    }
}