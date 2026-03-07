package br.inf.andreagonzalez.controledegastos;

import androidx.core.content.ContextCompat;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import androidx.appcompat.app.AlertDialog;
import java.text.NumberFormat;
import java.util.Locale;

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
        recuperarListaGastos();
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

        adapter.setOnItemLongClickListener(position -> removerGasto(position));
    }
    private String formatarMoeda(double valor) {
        NumberFormat formatoBrasil =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasil.format(valor);
    }

    private void atualizarSaldo(double saldo) {

        textSaldoRestante.setText("Saldo restante: " + formatarMoeda(saldo));

        if (saldo < 0) {

            textSaldoRestante.setTextColor(
                    ContextCompat.getColor(this, android.R.color.holo_red_dark)
            );

        } else if (saldo > 0) {

            textSaldoRestante.setTextColor(
                    ContextCompat.getColor(this, android.R.color.holo_green_dark)
            );

        } else {

            textSaldoRestante.setTextColor(
                    ContextCompat.getColor(this, android.R.color.darker_gray)
            );
        }
    }

    private void recuperarSalarioSalvo() {
        float salarioSalvo = preferences.getFloat("salario", 0);

        if (salarioSalvo != 0) {
            editSalario.setText(String.valueOf(salarioSalvo));
        }
    }

    private void recuperarListaGastos() {

        Gson gson = new Gson();
        String json = preferences.getString("lista_gastos", null);

        if (json != null) {

            Type type = new TypeToken<ArrayList<Gasto>>() {}.getType();
            listaGastos.clear();
            listaGastos.addAll(gson.fromJson(json, type));

            adapter.notifyDataSetChanged();

            totalGasto = 0;
            for (Gasto gasto : listaGastos) {
                totalGasto += gasto.getValor();
            }

            float salarioSalvo = preferences.getFloat("salario", 0);
            double saldo = salarioSalvo - totalGasto;

            textTotalGasto.setText("Total gasto: " + formatarMoeda(totalGasto));
            atualizarSaldo(saldo);
        }
    }
    private void removerGasto(int position) {

        Gasto gastoSelecionado = listaGastos.get(position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remover gasto")
                .setMessage("Você está prestes a excluir:\n\n"
                        + gastoSelecionado.getDescricao()
                        + "\nValor: " + formatarMoeda(gastoSelecionado.getValor())
                        + "\n\nDeseja continuar?")
                .setPositiveButton("Remover", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {

            Button btnRemover = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnCancelar = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Deixa botão remover vermelho
            btnRemover.setTextColor(
                    ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark)
            );

            btnRemover.setOnClickListener(v -> {

                totalGasto -= gastoSelecionado.getValor();

                listaGastos.remove(position);
                adapter.notifyItemRemoved(position);

                float salarioSalvo = preferences.getFloat("salario", 0);
                double saldo = salarioSalvo - totalGasto;

                textTotalGasto.setText("Total gasto: " + formatarMoeda(totalGasto));
                atualizarSaldo(saldo);

                salvarListaGastos();

                Toast.makeText(this,
                        "Gasto removido com sucesso",
                        Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });
        });

        dialog.show();
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

        String valorLimpo = salarioTexto
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();

        double salario = Double.parseDouble(valorLimpo);

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
        adapter.notifyItemInserted(listaGastos.size() - 1);
        salvarListaGastos();



        // Atualiza total
        totalGasto += valorGasto;

        float salarioSalvo = preferences.getFloat("salario", 0);
        double saldo = salarioSalvo - totalGasto;

        textTotalGasto.setText("Total gasto: " + formatarMoeda(totalGasto));
        atualizarSaldo(saldo);

        // Limpa campos
        editDescricao.setText("");
        editValorGasto.setText("");
    }


    private void salvarListaGastos() {

        Gson gson = new Gson();
        String json = gson.toJson(listaGastos);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lista_gastos", json);
        editor.apply();
    }

}
