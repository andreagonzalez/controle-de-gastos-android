package br.inf.andreagonzalez.controledegastos;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    double totalGasto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1️⃣ Inicializa o SharedPreferences
        preferences = getSharedPreferences("dados", MODE_PRIVATE);

        // 2️⃣ Conecta os componentes da tela
        EditText editSalario = findViewById(R.id.editSalario);
        Button btnSalvar = findViewById(R.id.btnSalvar);
        EditText editDescricao = findViewById(R.id.editDescricao);
        EditText editValorGasto = findViewById(R.id.editValorGasto);
        Button btnAdicionarGasto = findViewById(R.id.btnAdicionarGasto);
        TextView textTotalGasto = findViewById(R.id.textTotalGasto);
        TextView textSaldoRestante = findViewById(R.id.textSaldoRestante);

        // 3️⃣ Recupera salário salvo
        float salarioSalvo = preferences.getFloat("salario", 0);

        if (salarioSalvo != 0) {
            editSalario.setText(String.valueOf(salarioSalvo));
        }

        // 4️⃣ Evento do botão
        btnSalvar.setOnClickListener(v -> {

            String salarioTexto = editSalario.getText().toString();

            if (salarioTexto.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        "Informe o salário",
                        Toast.LENGTH_SHORT).show();
            } else {
                double salario = Double.parseDouble(salarioTexto);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("salario", (float) salario);
                editor.apply();

                Toast.makeText(MainActivity.this,
                        "Salário salvo com sucesso!!",
                        Toast.LENGTH_LONG).show();
            }

        });

        // 4️⃣ Evento do botão Adicionar Gasto
        btnAdicionarGasto.setOnClickListener(v -> {

            String descricao = editDescricao.getText().toString();
            String valorTexto = editValorGasto.getText().toString();

            if (descricao.isEmpty() || valorTexto.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        "Preencha todos os campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            double valorGasto = Double.parseDouble(valorTexto);
            totalGasto += valorGasto;

            float salarioSalvoAtual = preferences.getFloat("salario", 0);
            double saldo = salarioSalvoAtual - totalGasto;

            textTotalGasto.setText("Total gasto: R$ " + totalGasto);
            textSaldoRestante.setText("Saldo restante: R$ " + saldo);

            editDescricao.setText("");
            editValorGasto.setText("");

        });


    }
}