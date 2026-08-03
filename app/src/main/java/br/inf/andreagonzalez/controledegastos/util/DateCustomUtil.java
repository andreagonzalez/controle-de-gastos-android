package br.inf.andreagonzalez.controledegastos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateCustomUtil {

    private static final String DISPLAY_FORMAT = "dd/MM/yyyy";
    private static final String STORAGE_FORMAT = "yyyy-MM-dd";

    public static String toStorageFormat(String displayDate) {
        try {
            SimpleDateFormat displaySdf = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
            SimpleDateFormat storageSdf = new SimpleDateFormat(STORAGE_FORMAT, Locale.getDefault());
            Date date = displaySdf.parse(displayDate);
            return storageSdf.format(date);
        } catch (ParseException | NullPointerException e) {
            return displayDate; // Retorna original em caso de erro
        }
    }

    public static String toDisplayFormat(String storageDate) {
        try {
            SimpleDateFormat storageSdf = new SimpleDateFormat(STORAGE_FORMAT, Locale.getDefault());
            SimpleDateFormat displaySdf = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault());
            Date date = storageSdf.parse(storageDate);
            return displaySdf.format(date);
        } catch (ParseException | NullPointerException e) {
            return storageDate; // Retorna original em caso de erro
        }
    }
}
