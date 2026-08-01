package br.inf.andreagonzalez.controledegastos.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.util.DatePickerUtil;

public class EntradaActivity extends AppCompatActivity {

    private EditText editDescricaoEntrada;
    private EditText editValorEntrada;
    private Button btnAdicionarEntrada;
    private EditText editData;
    private SharedPreferences preferences;
    private ArrayList<Entrada> listaEntradas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        inicializarComponentes();
        DatePickerUtil.configurarCampoData(
                this,
                editData
        );
        inicializarPreferencias();
        recuperarListaEntradas();

        btnAdicionarEntrada.setOnClickListener(
                v -> adicionarEntrada()
        );
    }

    private void inicializarComponentes() {

        editDescricaoEntrada =
                findViewById(R.id.editDescricaoEntrada);

        editValorEntrada =
                findViewById(R.id.editValorEntrada);

        editData =
                findViewById(R.id.editData);

        btnAdicionarEntrada =
                findViewById(R.id.btnAdicionarEntrada);
    }

    private void inicializarPreferencias() {

        preferences = getSharedPreferences(
                "dados",
                MODE_PRIVATE
        );
    }

    private void adicionarEntrada() {

        String descricao =
                editDescricaoEntrada.getText().toString();

        String valorTexto =
                editValorEntrada.getText().toString();

        if (descricao.isEmpty() || valorTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double valor =
                Double.parseDouble(valorTexto);

        String data =
                editData.getText().toString().trim();

        Entrada entrada = new Entrada(
                descricao,
                valor,
                data
        );

        listaEntradas.add(entrada);

        salvarListaEntradas();

        Toast.makeText(
                this,
                "Entrada adicionada com sucesso",
                Toast.LENGTH_SHORT
        ).show();

        finish();
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

    private void salvarListaEntradas() {

        Gson gson = new Gson();

        String json =
                gson.toJson(listaEntradas);

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                "lista_entradas",
                json
        );

        editor.apply();
    }
}