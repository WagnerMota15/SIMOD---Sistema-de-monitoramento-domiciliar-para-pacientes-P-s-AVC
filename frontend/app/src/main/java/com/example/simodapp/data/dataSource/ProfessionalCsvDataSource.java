package com.example.simodapp.data.dataSource;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProfessionalCsvDataSource {

    private static final String FILE_NAME = "professionals.csv";
    private final Context context;

    public ProfessionalCsvDataSource(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean verify(
            String cpf,
            String council,
            String registration,
            String uf
    ) {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                context.getAssets().open(FILE_NAME)
                        )
                )
        ) {

            Log.d("FORM_INPUT",
                    normalize(cpf) + " | " +
                            normalize(council) + " | " +
                            normalize(registration) + " | " +
                            normalize(uf)
            );

            String line;
            reader.readLine(); // header

            while ((line = reader.readLine()) != null) {

                String[] columns = line.split(";");

                String csvCpf = normalize(columns[0]);
                String csvCouncil = normalize(columns[3]);
                String csvRegistration = normalize(columns[4]);
                String csvUf = normalize(columns[5]);
                String csvStatus = normalize(columns[6]);
                // 🔎 LOG DO CSV
                Log.d("CSV_CHECK",
                        csvCpf + " | " +
                                csvCouncil + " | " +
                                csvRegistration + " | " +
                                csvUf + " | " +
                                csvStatus
                );

                if (
                        csvCpf.equals(normalize(cpf)) &&
                                csvCouncil.equals(normalize(council)) &&
                                csvRegistration.equals(normalize(registration)) &&
                                csvUf.equals(normalize(uf)) &&
                                csvStatus.equals("ACTIVE")
                ) {
                    return true;
                }
            }

        } catch (Exception e) {
            Log.e("CSV_ERROR", "Erro ao ler CSV", e);
        }

        return false;
    }

    private String normalize(String value) {
        if (value == null) return "";

        return value
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");
    }

}
