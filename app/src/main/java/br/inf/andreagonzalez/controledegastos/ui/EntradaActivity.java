package br.inf.andreagonzalez.controledegastos.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.AppDatabase;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.EntradaDao;
import br.inf.andreagonzalez.controledegastos.util.DatePickerUtil;

public class EntradaActivity extends AppCompatActivity {

    private EditText editDescricaoEntrada;
    private EditText editValorEntrada;
    private Button btnAdicionarEntrada;
    private EditText editData;
    
    private AppDatabase db;
    private EntradaDao entradaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        inicializarComponentes();
        DatePickerUtil.configurarCampoData(
                this,
                editData
        );
        inicializarBanco();

        btnAdicionarEntrada.setOnClickListener(
                v -> adicionarEntrada()
        );
    }

    private void inicializarComponentes() {
        editDescricaoEntrada = findViewById(R.id.editDescricaoEntrada);
        editValorEntrada = findViewById(R.id.editValorEntrada);
        editData = findViewById(R.id.editData);
        btnAdicionarEntrada = findViewById(R.id.btnAdicionarEntrada);
    }

    private void inicializarBanco() {
        db = AppDatabase.getInstance(this);
        entradaDao = db.entradaDao();
    }

    private void adicionarEntrada() {
        String descricao = editDescricaoEntrada.getText().toString();
        String valorTexto = editValorEntrada.getText().toString();
        String data = editData.getText().toString().trim();

        if (descricao.isEmpty() || valorTexto.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valor = Double.parseDouble(valorTexto);

            Entrada entrada = new Entrada(descricao, valor, data);
            entradaDao.inserirEntrada(entrada);

            Toast.makeText(this, "Entrada adicionada com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
        }
    }
}