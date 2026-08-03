package br.inf.andreagonzalez.controledegastos.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.GastoAdapter;
import br.inf.andreagonzalez.controledegastos.model.AppDatabase;
import br.inf.andreagonzalez.controledegastos.model.Gasto;
import br.inf.andreagonzalez.controledegastos.model.GastoDao;
import br.inf.andreagonzalez.controledegastos.util.DateCustomUtil;

public class ListaGastosActivity extends AppCompatActivity {

    private RecyclerView recyclerGastos;
    private GastoAdapter adapter;
    private ArrayList<Gasto> listaGastos = new ArrayList<>();

    private AppDatabase db;
    private GastoDao gastoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        inicializarComponentes();
        inicializarBanco();
        recuperarListaGastos();
        configurarRecyclerView();
    }

    private void inicializarComponentes() {
        recyclerGastos = findViewById(R.id.recyclerGastos);
    }

    private void inicializarBanco() {
        db = AppDatabase.getInstance(this);
        gastoDao = db.gastoDao();
    }

    private void recuperarListaGastos() {
        listaGastos = new ArrayList<>(gastoDao.listarGastosOrdenados());
    }

    private void configurarRecyclerView() {
        recyclerGastos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GastoAdapter(listaGastos);
        recyclerGastos.setAdapter(adapter);
        adapter.setOnItemLongClickListener(position -> mostrarOpcoesGasto(position));
    }

    private void mostrarOpcoesGasto(int position) {
        String[] opcoes = {"Editar", "Excluir"};
        new AlertDialog.Builder(this)
                .setTitle("Escolha uma ação")
                .setItems(opcoes, (dialog, which) -> {
                    if (which == 0) editarGasto(position);
                    else if (which == 1) removerGasto(position);
                })
                .show();
    }

    private void editarGasto(int position) {
        Gasto gastoSelecionado = listaGastos.get(position);
        EditText editDescricaoDialog = new EditText(this);
        EditText editValorDialog = new EditText(this);
        EditText editDataDialog = new EditText(this);
        
        editDescricaoDialog.setText(gastoSelecionado.getDescricao());
        editValorDialog.setText(String.valueOf(gastoSelecionado.getValor()));
        editDataDialog.setText(DateCustomUtil.toDisplayFormat(gastoSelecionado.getData()));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editDescricaoDialog);
        layout.addView(editValorDialog);
        layout.addView(editDataDialog);

        new AlertDialog.Builder(this)
                .setTitle("Editar gasto")
                .setView(layout)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String novaDescricao = editDescricaoDialog.getText().toString();
                    String valorTexto = editValorDialog.getText().toString();
                    String novaData = editDataDialog.getText().toString();
                    
                    if (novaDescricao.isEmpty() || valorTexto.isEmpty() || novaData.isEmpty()) {
                        Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    gastoSelecionado.setDescricao(novaDescricao);
                    gastoSelecionado.setValor(Double.parseDouble(valorTexto));
                    gastoSelecionado.setData(DateCustomUtil.toStorageFormat(novaData));
                    
                    gastoDao.atualizarGasto(gastoSelecionado);
                    recuperarListaGastos();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Gasto atualizado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void removerGasto(int position) {
        Gasto gastoSelecionado = listaGastos.get(position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remover gasto")
                .setMessage("Você está prestes a excluir:\n\n" + gastoSelecionado.getDescricao() + "\nDeseja continuar?")
                .setPositiveButton("Remover", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnRemover = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnRemover.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            btnRemover.setOnClickListener(v -> {
                gastoDao.removerGasto(gastoSelecionado);
                listaGastos.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, "Gasto removido", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
        dialog.show();
    }
}