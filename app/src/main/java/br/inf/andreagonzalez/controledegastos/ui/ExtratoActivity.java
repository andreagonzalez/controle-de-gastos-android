package br.inf.andreagonzalez.controledegastos.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.MovimentoAdapter;
import br.inf.andreagonzalez.controledegastos.model.Movimento;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.Gasto;


public class ExtratoActivity extends AppCompatActivity {
    private RecyclerView recyclerMovimentos;
    private MovimentoAdapter adapter;
    private ArrayList<Movimento> listaMovimentos = new ArrayList<>();
    private SharedPreferences preferences;
    private ArrayList<Gasto> listaGastos = new ArrayList<>();
    private ArrayList<Entrada> listaEntradas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extrato);

        inicializarPreferencias();
        recuperarListaGastos();
        recuperarListaEntradas();
        consolidarMovimentos();

        recyclerMovimentos = findViewById(R.id.recyclerMovimentos);

        recyclerMovimentos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new MovimentoAdapter(listaMovimentos);
        adapter.notifyDataSetChanged();

        recyclerMovimentos.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                preferences.getString("lista_gastos", null);

        if (json != null) {

            Type type =
                    new TypeToken<ArrayList<Gasto>>() {}.getType();

            listaGastos.clear();

            listaGastos.addAll(
                    gson.fromJson(json, type)
            );
        }
    }

    private void recuperarListaEntradas() {

        Gson gson = new Gson();

        String json =
                preferences.getString("lista_entradas", null);

        if (json != null) {

            Type type =
                    new TypeToken<ArrayList<Entrada>>() {}.getType();

            listaEntradas.clear();

            listaEntradas.addAll(
                    gson.fromJson(json, type)
            );
        }
    }
    private void consolidarMovimentos() {

        listaMovimentos.clear();

        for (Entrada entrada : listaEntradas) {

            listaMovimentos.add(
                    new Movimento(
                            entrada.getDescricao(),
                            entrada.getValor(),
                            "ENTRADA"
                    )
            );
        }

        for (Gasto gasto : listaGastos) {

            listaMovimentos.add(
                    new Movimento(
                            gasto.getDescricao(),
                            gasto.getValor(),
                            "GASTO"
                    )
            );
        }
    }
}