package br.inf.andreagonzalez.controledegastos.ui;

import androidx.core.content.ContextCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import androidx.appcompat.app.AlertDialog;
import java.text.NumberFormat;
import java.util.Locale;
import android.widget.LinearLayout;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.Gasto;
import br.inf.andreagonzalez.controledegastos.model.Movimento;
import br.inf.andreagonzalez.controledegastos.ui.ExtratoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.widget.Spinner;
import android.widget.ArrayAdapter;




public class MainActivity extends AppCompatActivity {

    // =========================
    // ATRIBUTOS DA CLASSE
    // =========================

    private SharedPreferences preferences;

    private double totalGasto = 0;
    private double totalEntrada = 0;

    private ArrayList<Gasto> listaGastos = new ArrayList<>();
    private ArrayList<Entrada> listaEntradas = new ArrayList<>();
    private ArrayList<Movimento> listaMovimentos = new ArrayList<>();

    private Button btnVerExtrato;
    private Button btnVerGastos;
    private Button btnCalcularSaldoPeriodo;
    private void adicionarEntrada() {

        String descricao = editDescricaoEntrada.getText().toString();
        String valorTexto = editValorEntrada.getText().toString();

        if (descricao.isEmpty() || valorTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double valor = Double.parseDouble(valorTexto);
        String data = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        ).format(new Date());

        Entrada entrada = new Entrada(
                descricao,
                valor,
                data
        );

        listaEntradas.add(entrada);
        totalEntrada += valor;
        salvarListaEntradas();
        recalcularSaldo();
        Toast.makeText(
                this,
                "Entrada adicionada com sucesso",
                Toast.LENGTH_SHORT
        ).show();

        editDescricaoEntrada.setText("");
        editValorEntrada.setText("");
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

            totalEntrada = 0;

            for (Entrada entrada : listaEntradas) {
                totalEntrada += entrada.getValor();
            }
        }
    }

    private void consolidarMovimentos() {

        listaMovimentos.clear();

        for (Entrada entrada : listaEntradas) {

            listaMovimentos.add(
                    new Movimento(
                            entrada.getDescricao(),
                            entrada.getValor(),
                            "ENTRADA",
                            entrada.getData()
                    )
            );
        }

        for (Gasto gasto : listaGastos) {

            listaMovimentos.add(
                    new Movimento(
                            gasto.getDescricao(),
                            gasto.getValor(),
                            "GASTO",
                            gasto.getData()
                    )
            );
        }
    }

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
        recuperarSalarioSalvo();
        configurarListeners();
        recuperarListaGastos();
        recuperarListaEntradas();
    }
    @Override
    protected void onResume() {
        super.onResume();

        recuperarListaEntradas();
        recuperarListaGastos();
        recalcularSaldo();
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
    private Spinner spinnerCategoria;
    private Spinner spinnerFormaPagamento;
    private EditText editDescricaoEntrada;
    private EditText editValorEntrada;
    private Button btnAdicionarEntrada;
    private Button btnVerEntradas;

    private void inicializarComponentes() {
        editSalario = findViewById(R.id.editSalario);
        textTotalGasto = findViewById(R.id.textTotalGasto);
        textSaldoRestante = findViewById(R.id.textSaldoRestante);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnAdicionarGasto = findViewById(R.id.btnAdicionarGasto);
        btnAdicionarEntrada = findViewById(R.id.btnAdicionarEntrada);
        btnVerExtrato = findViewById(R.id.btnVerExtrato);
        btnVerGastos = findViewById(R.id.btnVerGastos);
        btnVerEntradas = findViewById(R.id.btnVerEntradas);
        btnCalcularSaldoPeriodo = findViewById(R.id.btnCalcularSaldoPeriodo);

    }
  private void configurarSpinnerCategorias() {

            String[] categorias = {
                    "Alimentação",
                    "Transporte",
                    "Moradia",
                    "Saúde",
                    "Lazer",
                    "Outros"
            };

            ArrayAdapter<String> adapterCategorias =
                    new ArrayAdapter<>(
                            this,
                        android.R.layout.simple_spinner_item,
                        categorias
                );

        adapterCategorias.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategoria.setAdapter(adapterCategorias);
    }
    private void configurarSpinnerFormaPagamento() {

        String[] formasPagamento = {
                "Pix",
                "Dinheiro",
                "Cartão de Débito",
                "Cartão de Crédito"
        };

        ArrayAdapter<String> adapterFormaPagamento =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        formasPagamento
                );

        adapterFormaPagamento.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerFormaPagamento.setAdapter(adapterFormaPagamento);
    }
    private String formatarMoeda(double valor) {
        NumberFormat formatoBrasil =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasil.format(valor);
    }
    private void recalcularSaldo() {

        float salarioSalvo =
                preferences.getFloat("salario", 0);

        double saldo =
                salarioSalvo + totalEntrada - totalGasto;

        textTotalGasto.setText(
                "Total gasto: " + formatarMoeda(totalGasto)
        );

        atualizarSaldo(saldo);
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

            totalGasto = 0;
            for (Gasto gasto : listaGastos) {
                totalGasto += gasto.getValor();
            }

            recalcularSaldo();
        }
    }


    // =========================
    // LISTENERS
    // =========================

    private void configurarListeners() {

        // Botão salvar salário
        btnSalvar.setOnClickListener(v -> salvarSalario());

        btnAdicionarEntrada.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    EntradaActivity.class
            );

            startActivity(intent);
        });

        // Botão adicionar gasto
        btnAdicionarGasto.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    GastoActivity.class
            );

            startActivity(intent);
        });

        // Botão ver extrato
        btnVerExtrato.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ExtratoActivity.class
            );

            startActivity(intent);
        });

        btnVerGastos.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ListaGastosActivity.class
            );

            startActivity(intent);
        });
        btnVerEntradas.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ListaEntradasActivity.class
            );

            startActivity(intent);
        });
        btnCalcularSaldoPeriodo.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SaldoPeriodoActivity.class
            );

            startActivity(intent);
        });

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
        String categoriaSelecionada =
                spinnerCategoria.getSelectedItem().toString();
        String formaPagamentoSelecionada =
                spinnerFormaPagamento.getSelectedItem().toString();

        if (descricao.isEmpty() || valorTexto.isEmpty()) {
            Toast.makeText(this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double valorGasto = Double.parseDouble(valorTexto);

        String data = new SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
        ).format(new Date());

        // Cria objeto e adiciona à lista
        Gasto novoGasto =
                new Gasto(descricao,
                        valorGasto,
                        categoriaSelecionada,
                        formaPagamentoSelecionada,
                        data
                );



        listaGastos.add(novoGasto);
        salvarListaGastos();

        Toast.makeText(
                this,
                "Gasto adicionado com sucesso",
                Toast.LENGTH_SHORT
        ).show();

        // Atualiza total
        totalGasto += valorGasto;

        recalcularSaldo();

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
    private void salvarListaEntradas() {

        Gson gson = new Gson();
        String json = gson.toJson(listaEntradas);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lista_entradas", json);
        editor.apply();
    }

}
