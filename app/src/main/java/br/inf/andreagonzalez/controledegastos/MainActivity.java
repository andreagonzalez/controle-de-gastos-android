package br.inf.andreagonzalez.controledegastos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // =========================
    // ATRIBUTOS DA CLASSE
    // =========================

    private SharedPreferences preferences;

    private double totalGasto = 0;

    private ArrayList<Gasto> listaGastos = new ArrayList<>();
    private RecyclerView recyclerView;
    private GastoAdapter adapter;

    // =========================
    // CICLO DE VIDA
    // =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        inicializarPreferencias();
        inicializarComponentes();
        configurarRecyclerView();
        recuperarSalarioSalvo();
        configurarListeners();
    }

    // =========================
    // MÉTODOS DE INICIALIZAÇÃO
    // =========================

    private void inicializarPreferencias() {
        preferences = getSharedPreferences("dados", MODE_PRIVATE);
    }

    private EditText editSalario;
    private EditText editDescricao;
    private EditText editValorGasto;
    private TextView textTotalGasto;
    private TextView textSaldoRestante;
    private Button btnSalvar;
    private Button btnAdicionarGasto;

    private void inicializarComponentes() {
        editSalario = findViewById(R.id.editSalario);
        editDescricao = findViewById(R.id.editDescricao);
        editValorGasto = findViewById(R.id.editValorGasto);
        textTotalGasto = findViewById(R.id.textTotalGasto);
        textSaldoRestante = findViewById(R.id.textSaldoRestante);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnAdicionarGasto = findViewById(R.id.btnAdicionarGasto);
        recyclerView = findViewById(R.id.recyclerGastos);
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GastoAdapter(listaGastos);
        recyclerView.setAdapter(adapter);
    }

    private void recuperarSalarioSalvo() {
        float salarioSalvo = preferences.getFloat("salario", 0);

        if (salarioSalvo != 0) {
            editSalario.setText(String.valueOf(salarioSalvo));
        }
    }

    // =========================
    // LISTENERS
    // =========================

    private void configurarListeners() {

        // Botão salvar salário
        btnSalvar.setOnClickListener(v -> salvarSalario());

        // Botão adicionar gasto
        btnAdicionarGasto.setOnClickListener(v -> adicionarGasto());
    }

    // =========================
    // LÓGICA DE NEGÓCIO
    // =========================

    private void salvarSalario() {

        String salarioTexto = editSalario.getText().toString();

        if (salarioTexto.isEmpty()) {
            Toast.makeText(this,
                    "Informe o salário",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double salario = Double.parseDouble(salarioTexto);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("salario", (float) salario);
        editor.apply();

        Toast.makeText(this,
                "Salário salvo com sucesso!",
                Toast.LENGTH_LONG).show();
    }

    private void adicionarGasto() {

        String descricao = editDescricao.getText().toString();
        String valorTexto = editValorGasto.getText().toString();

        if (descricao.isEmpty() || valorTexto.isEmpty()) {
            Toast.makeText(this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double valorGasto = Double.parseDouble(valorTexto);

        // Cria objeto e adiciona à lista
        Gasto novoGasto = new Gasto(descricao, valorGasto);
        listaGastos.add(novoGasto);
        adapter.notifyDataSetChanged();

        // Atualiza total
        totalGasto += valorGasto;

        float salarioSalvo = preferences.getFloat("salario", 0);
        double saldo = salarioSalvo - totalGasto;

        textTotalGasto.setText("Total gasto: R$ " + totalGasto);
        textSaldoRestante.setText("Saldo restante: R$ " + saldo);

        // Limpa campos
        editDescricao.setText("");
        editValorGasto.setText("");
    }
}
