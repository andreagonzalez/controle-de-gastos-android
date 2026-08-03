package br.inf.andreagonzalez.controledegastos.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.AppDatabase;
import br.inf.andreagonzalez.controledegastos.model.Gasto;
import br.inf.andreagonzalez.controledegastos.model.GastoDao;
import br.inf.andreagonzalez.controledegastos.util.DatePickerUtil;

public class GastoActivity extends AppCompatActivity {

    private EditText editDescricao;
    private EditText editValorGasto;
    private Spinner spinnerCategoria;
    private Spinner spinnerFormaPagamento;
    private Button btnAdicionarGasto;
    private EditText editData;
    
    private AppDatabase db;
    private GastoDao gastoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        inicializarComponentes();
        DatePickerUtil.configurarCampoData(this, editData);
        inicializarBanco();
        configurarSpinnerCategorias();
        configurarSpinnerFormaPagamento();

        btnAdicionarGasto.setOnClickListener(v -> adicionarGasto());
    }

    private void inicializarComponentes() {
        editDescricao = findViewById(R.id.editDescricao);
        editValorGasto = findViewById(R.id.editValorGasto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerFormaPagamento = findViewById(R.id.spinnerFormaPagamento);
        btnAdicionarGasto = findViewById(R.id.btnAdicionarGasto);
        editData = findViewById(R.id.editData);
    }

    private void inicializarBanco() {
        db = AppDatabase.getInstance(this);
        gastoDao = db.gastoDao();
    }

    private void configurarSpinnerCategorias() {
        String[] categorias = {"Alimentação", "Transporte", "Moradia", "Saúde", "Lazer", "Outros"};
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategorias);
    }

    private void configurarSpinnerFormaPagamento() {
        String[] formasPagamento = {"Pix", "Dinheiro", "Cartão de Débito", "Cartão de Crédito"};
        ArrayAdapter<String> adapterFormaPagamento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formasPagamento);
        adapterFormaPagamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPagamento.setAdapter(adapterFormaPagamento);
    }

    private void adicionarGasto() {
        String descricao = editDescricao.getText().toString();
        String valorTexto = editValorGasto.getText().toString();
        String categoriaSelecionada = spinnerCategoria.getSelectedItem().toString();
        String formaPagamentoSelecionada = spinnerFormaPagamento.getSelectedItem().toString();
        String data = editData.getText().toString().trim();

        if (descricao.isEmpty() || valorTexto.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double valorGasto = Double.parseDouble(valorTexto);
            Gasto novoGasto = new Gasto(descricao, valorGasto, categoriaSelecionada, formaPagamentoSelecionada, data);
            gastoDao.inserirGasto(novoGasto);
            Toast.makeText(this, "Gasto adicionado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
        }
    }
}