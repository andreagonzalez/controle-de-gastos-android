package br.inf.andreagonzalez.controledegastos.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerUtil {

    public static void configurarCampoData(
            Context context,
            EditText editData
    ) {

        Calendar calendar = Calendar.getInstance();

        String dataAtual =
                String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR)
                );

        editData.setText(dataAtual);

        editData.setFocusable(false);
        editData.setClickable(true);

        editData.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            context,
                            (view, year, month, dayOfMonth) -> {

                                String dataSelecionada =
                                        String.format(
                                                Locale.getDefault(),
                                                "%02d/%02d/%04d",
                                                dayOfMonth,
                                                month + 1,
                                                year
                                        );

                                editData.setText(dataSelecionada);

                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );

            datePickerDialog.show();

        });
    }
}