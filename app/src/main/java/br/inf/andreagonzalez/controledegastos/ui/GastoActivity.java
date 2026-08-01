package br.inf.andreagonzalez.controledegastos.ui;

import br.inf.andreagonzalez.controledegastos.util.DatePickerUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.Gasto;


public class GastoActivity extends AppCompatActivity {

    private EditText editDescricao;
    private EditText editValorGasto;
    private Spinner spinnerCategoria;
    private Spinner spinnerFormaPagamento;
    private Button btnAdicionarGasto;
    private EditText editData;
    private SharedPreferences preferences;
    private ArrayList<Gasto> listaGastos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        inicializarComponentes();
        DatePickerUtil.configurarCampoData(
                this,
                editData
        );

        inicializarPreferencias();
        configurarSpinnerCategorias();
        configurarSpinnerFormaPagamento();
        recuperarListaGastos();


        btnAdicionarGasto.setOnClickListener(
                v -> adicionarGasto()
        );
    }

    private void inicializarComponentes() {

        editDescricao =
                findViewById(R.id.editDescricao);

        editValorGasto =
                findViewById(R.id.editValorGasto);

        spinnerCategoria =
                findViewById(R.id.spinnerCategoria);

        spinnerFormaPagamento =
                findViewById(R.id.spinnerFormaPagamento);

        btnAdicionarGasto =
                findViewById(R.id.btnAdicionarGasto);
        editData =
                findViewById(R.id.editData);
    }

    private void inicializarPreferencias() {

        preferences = getSharedPreferences(
                "dados",
                MODE_PRIVATE
        );
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

        spinnerFormaPagamento.setAdapter(
                adapterFormaPagamento
        );
    }

    private void adicionarGasto() {

        String descricao =
                editDescricao.getText().toString();

        String valorTexto =
                editValorGasto.getText().toString();

        String categoriaSelecionada =
                spinnerCategoria.getSelectedItem().toString();

        String formaPagamentoSelecionada =
                spinnerFormaPagamento.getSelectedItem().toString();

        if (descricao.isEmpty() || valorTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double valorGasto =
                Double.parseDouble(valorTexto);

        String data = editData.getText().toString().trim();

        Gasto novoGasto =
                new Gasto(
                        descricao,
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

        finish();
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

    private void salvarListaGastos() {

        Gson gson = new Gson();

        String json =
                gson.toJson(listaGastos);

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                "lista_gastos",
                json
        );

        editor.apply();
    }
}