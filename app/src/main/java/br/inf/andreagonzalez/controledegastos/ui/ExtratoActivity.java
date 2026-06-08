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

public class ExtratoActivity extends AppCompatActivity {
    private RecyclerView recyclerMovimentos;
    private MovimentoAdapter adapter;
    private ArrayList<Movimento> listaMovimentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extrato);
        recyclerMovimentos = findViewById(R.id.recyclerMovimentos);

        recyclerMovimentos.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new MovimentoAdapter(listaMovimentos);

        recyclerMovimentos.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}