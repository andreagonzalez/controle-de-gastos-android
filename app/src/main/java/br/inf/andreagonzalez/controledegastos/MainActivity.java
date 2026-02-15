package br.inf.andreagonzalez.controledegastos;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;

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

    }
}