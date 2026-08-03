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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.EntradaAdapter;
import br.inf.andreagonzalez.controledegastos.model.AppDatabase;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.model.EntradaDao;
import br.inf.andreagonzalez.controledegastos.util.DateCustomUtil;

public class ListaEntradasActivity extends AppCompatActivity {

    private RecyclerView recyclerEntradas;
    private EntradaAdapter adapter;
    private ArrayList<Entrada> listaEntradas = new ArrayList<>();

    private AppDatabase db;
    private EntradaDao entradaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entradas);

        inicializarComponentes();
        inicializarBanco();
        recuperarListaEntradas();
        configurarRecyclerView();
    }

    private void inicializarComponentes() {
        recyclerEntradas = findViewById(R.id.recyclerEntradas);
    }

    private void inicializarBanco() {
        db = AppDatabase.getInstance(this);
        entradaDao = db.entradaDao();
    }

    private void recuperarListaEntradas() {
        listaEntradas = new ArrayList<>(entradaDao.listarEntradasOrdenadas());
    }

    private void salvarEntrada(Entrada entrada) {
        entradaDao.inserirEntrada(entrada);
        listaEntradas = new ArrayList<>(entradaDao.listarEntradasOrdenadas());
        adapter.notifyDataSetChanged();
    }

    private void configurarRecyclerView() {
        recyclerEntradas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EntradaAdapter(listaEntradas);
        recyclerEntradas.setAdapter(adapter);

        adapter.setOnItemLongClickListener(position -> mostrarOpcoesEntrada(position));
    }

    private void mostrarOpcoesEntrada(int position) {
        String[] opcoes = {"Editar", "Excluir"};

        new AlertDialog.Builder(this)
                .setTitle("Escolha uma ação")
                .setItems(opcoes, (dialog, which) -> {
                    if (which == 0) {
                        editarEntrada(position);
                    }
                    if (which == 1) {
                        removerEntrada(position);
                    }
                })
                .show();
    }

    private void editarEntrada(int position) {
        Entrada entradaSelecionada = listaEntradas.get(position);

        EditText editDescricaoDialog = new EditText(this);
        EditText editValorDialog = new EditText(this);
        EditText editDataDialog = new EditText(this);

        editDescricaoDialog.setText(entradaSelecionada.getDescricao());
        editValorDialog.setText(String.valueOf(entradaSelecionada.getValor()));
        editDataDialog.setText(DateCustomUtil.toDisplayFormat(entradaSelecionada.getData()));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editDescricaoDialog);
        layout.addView(editValorDialog);
        layout.addView(editDataDialog);

        new AlertDialog.Builder(this)
                .setTitle("Editar entrada")
                .setView(layout)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String novaDescricao = editDescricaoDialog.getText().toString();
                    String valorTexto = editValorDialog.getText().toString();
                    String novaData = editDataDialog.getText().toString();

                    if (novaDescricao.isEmpty() || valorTexto.isEmpty() || novaData.isEmpty()) {
                        Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double novoValor = Double.parseDouble(valorTexto);
                    entradaSelecionada.setDescricao(novaDescricao);
                    entradaSelecionada.setValor(novoValor);
                    entradaSelecionada.setData(DateCustomUtil.toStorageFormat(novaData));

                    entradaDao.atualizarEntrada(entradaSelecionada); // ✅ persiste no Room
                    listaEntradas = new ArrayList<>(entradaDao.listarEntradasOrdenadas());
                    adapter.notifyDataSetChanged();

                    Toast.makeText(this, "Entrada atualizada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void removerEntrada(int position) {
        Entrada entradaSelecionada = listaEntradas.get(position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remover entrada")
                .setMessage("Você está prestes a excluir:\n\n"
                        + entradaSelecionada.getDescricao()
                        + "\nValor: " + formatarMoeda(entradaSelecionada.getValor())
                        + "\nData: " + entradaSelecionada.getData()
                        + "\n\nDeseja continuar?")
                .setPositiveButton("Remover", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnRemover = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnCancelar = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            btnRemover.setTextColor(
                    ContextCompat.getColor(ListaEntradasActivity.this, android.R.color.holo_red_dark)
            );

            btnRemover.setOnClickListener(v -> {
                entradaDao.removerEntrada(entradaSelecionada); // ✅ remove do Room
                listaEntradas.remove(position);
                adapter.notifyItemRemoved(position);

                Toast.makeText(this, "Entrada removida com sucesso", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatoBrasil = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasil.format(valor);
    }
}
