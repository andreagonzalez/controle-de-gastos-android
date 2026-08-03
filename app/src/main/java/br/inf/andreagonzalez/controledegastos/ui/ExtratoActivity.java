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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.MovimentoAdapter;
import br.inf.andreagonzalez.controledegastos.model.AppDatabase;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.Gasto;
import br.inf.andreagonzalez.controledegastos.model.Movimento;

public class ExtratoActivity extends AppCompatActivity {
    private RecyclerView recyclerMovimentos;
    private MovimentoAdapter adapter;
    private ArrayList<Movimento> listaMovimentos = new ArrayList<>();
    
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extrato);

        db = AppDatabase.getInstance(this);

        recuperarDadosEConsolidar();

        recyclerMovimentos = findViewById(R.id.recyclerMovimentos);
        recyclerMovimentos.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new MovimentoAdapter(listaMovimentos);
        recyclerMovimentos.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void recuperarDadosEConsolidar() {
        List<Entrada> listaEntradas = db.entradaDao().listarEntradasOrdenadas();
        List<Gasto> listaGastos = db.gastoDao().listarGastosOrdenados();

        listaMovimentos.clear();

        for (Entrada entrada : listaEntradas) {
            listaMovimentos.add(new Movimento(
                    entrada.getDescricao(),
                    entrada.getValor(),
                    "ENTRADA",
                    entrada.getData()
            ));
        }

        for (Gasto gasto : listaGastos) {
            listaMovimentos.add(new Movimento(
                    gasto.getDescricao(),
                    gasto.getValor(),
                    "GASTO",
                    gasto.getData()
            ));
        }

        // Ordenação cronológica consolidada
        Collections.sort(listaMovimentos, new Comparator<Movimento>() {
            @Override
            public int compare(Movimento m1, Movimento m2) {
                return m1.getData().compareTo(m2.getData());
            }
        });
    }
}