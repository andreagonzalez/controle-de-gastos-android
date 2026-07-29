package br.inf.andreagonzalez.controledegastos.ui;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.Gasto;

public class SaldoPeriodoActivity extends AppCompatActivity {
    private EditText editDataInicial;
    private EditText editDataFinal;
    private Button btnCalcular;
    private TextView textTotalEntradas;
    private TextView textTotalGastos;
    private TextView textSaldoPeriodo;
    private SharedPreferences preferences;

    private ArrayList<Entrada> listaEntradas = new ArrayList<>();
    private ArrayList<Gasto> listaGastos = new ArrayList<>();
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_periodo);

        inicializarComponentes();
        configurarDatePickers();
        inicializarPreferencias();
        
        btnCalcular.setOnClickListener(v -> calcularSaldoPeriodo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        recuperarListaEntradas();
        recuperarListaGastos();
    }

    private void configurarDatePickers() {
        editDataInicial.setFocusable(false);
        editDataInicial.setOnClickListener(v -> exibirDatePicker(editDataInicial));

        editDataFinal.setFocusable(false);
        editDataFinal.setOnClickListener(v -> exibirDatePicker(editDataFinal));
    }

    private void exibirDatePicker(EditText campo) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String dataFormatada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            campo.setText(dataFormatada);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void inicializarComponentes() {
        editDataInicial = findViewById(R.id.editDataInicial);
        editDataFinal = findViewById(R.id.editDataFinal);
        btnCalcular = findViewById(R.id.btnCalcular);
        textTotalEntradas = findViewById(R.id.textTotalEntradas);
        textTotalGastos = findViewById(R.id.textTotalGastos);
        textSaldoPeriodo = findViewById(R.id.textSaldoPeriodo);
    }

    private void inicializarPreferencias() {
        preferences = getSharedPreferences("dados", MODE_PRIVATE);
    }

    private void recuperarListaEntradas() {
        String json = preferences.getString("lista_entradas", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Entrada>>() {}.getType();
            listaEntradas = new Gson().fromJson(json, type);
            if (listaEntradas == null) listaEntradas = new ArrayList<>();
            Log.d("SaldoPeriodo", "Entradas carregadas: " + listaEntradas.size());
        }
    }

    private void recuperarListaGastos() {
        String json = preferences.getString("lista_gastos", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Gasto>>() {}.getType();
            listaGastos = new Gson().fromJson(json, type);
            if (listaGastos == null) listaGastos = new ArrayList<>();
            Log.d("SaldoPeriodo", "Gastos carregados: " + listaGastos.size());
        }
    }

    private void calcularSaldoPeriodo() {
        String strInicio = editDataInicial.getText().toString().trim();
        String strFim = editDataFinal.getText().toString().trim();

        if (strInicio.isEmpty() || strFim.isEmpty()) {
            Toast.makeText(this, "Selecione as duas datas", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date dataInicio = formato.parse(strInicio);
            Date dataFim = formato.parse(strFim);

            if (dataInicio == null || dataFim == null) return;
            if (dataInicio.after(dataFim)) {
                Toast.makeText(
                        this,
                        "A data inicial não pode ser maior que a data final",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            double totalEntradas = 0;
            double totalGastos = 0;

            // Filtro de Entradas com proteção contra nulos
            for (Entrada e : listaEntradas) {
                if (e.getData() != null && !e.getData().isEmpty()) {
                    try {
                        Date dataMovimento = formato.parse(e.getData());
                        if (dataMovimento != null && isDataNoPeriodo(dataMovimento, dataInicio, dataFim)) {
                            totalEntradas += e.getValor();
                        }
                    } catch (ParseException ex) {
                        Log.e("SaldoPeriodo", "Data inválida na entrada: " + e.getData());
                    }
                }
            }

            // Filtro de Gastos com proteção contra nulos
            for (Gasto g : listaGastos) {
                if (g.getData() != null && !g.getData().isEmpty()) {
                    try {
                        Date dataMovimento = formato.parse(g.getData());
                        if (dataMovimento != null && isDataNoPeriodo(dataMovimento, dataInicio, dataFim)) {
                            totalGastos += g.getValor();
                        }
                    } catch (ParseException ex) {
                        Log.e("SaldoPeriodo", "Data inválida no gasto: " + g.getData());
                    }
                }
            }

            double saldo = totalEntradas - totalGastos;

            textTotalEntradas.setText(String.format(Locale.getDefault(), "Total de entradas: R$ %.2f", totalEntradas));
            textTotalGastos.setText(String.format(Locale.getDefault(), "Total de gastos: R$ %.2f", totalGastos));
            textSaldoPeriodo.setText(String.format(Locale.getDefault(), "Saldo do período: R$ %.2f", saldo));

        } catch (ParseException e) {
            Log.e("SaldoPeriodo", "Erro nas datas do formulário", e);
            Toast.makeText(this, "Datas informadas são inválidas", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDataNoPeriodo(Date data, Date inicio, Date fim) {
        if (data == null || inicio == null || fim == null) return false;
        long time = data.getTime();
        return time >= inicio.getTime() && time <= fim.getTime();
    }
}
