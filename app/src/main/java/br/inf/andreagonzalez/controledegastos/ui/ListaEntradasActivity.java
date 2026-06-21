package br.inf.andreagonzalez.controledegastos.ui;

import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.adapter.EntradaAdapter;
import br.inf.andreagonzalez.controledegastos.model.Entrada;

public class ListaEntradasActivity extends AppCompatActivity {

    private RecyclerView recyclerEntradas;
    private EntradaAdapter adapter;

    private ArrayList<Entrada> listaEntradas =
            new ArrayList<>();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entradas);

        inicializarComponentes();
        inicializarPreferencias();
        recuperarListaEntradas();
        configurarRecyclerView();
    }

    private void inicializarComponentes() {

        recyclerEntradas =
                findViewById(R.id.recyclerEntradas);
    }

    private void inicializarPreferencias() {

        preferences = getSharedPreferences(
                "dados",
                MODE_PRIVATE
        );
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

    private void configurarRecyclerView() {

        recyclerEntradas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new EntradaAdapter(
                listaEntradas
        );

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
        editDataDialog.setText(entradaSelecionada.getData());

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
                    entradaSelecionada.setData(novaData);
                    
                    adapter.notifyItemChanged(position);
                    salvarListaEntradas();
                    
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
                listaEntradas.remove(position);
                adapter.notifyItemRemoved(position);
                salvarListaEntradas();
                
                Toast.makeText(this, "Entrada removida com sucesso", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void salvarListaEntradas() {
        Gson gson = new Gson();
        String json = gson.toJson(listaEntradas);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lista_entradas", json);
        editor.apply();
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatoBrasil = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasil.format(valor);
    }
}