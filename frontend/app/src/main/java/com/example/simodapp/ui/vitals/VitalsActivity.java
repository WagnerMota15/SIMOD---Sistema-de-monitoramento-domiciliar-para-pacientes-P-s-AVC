package com.example.simodapp.ui.vitals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.ui.home.HomeActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class VitalsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private EditText etSystolic, etDiastolic, etBpNote;
    private EditText etHeartRate, etSpo2, etGlucose;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_vitals);

        toolbar = findViewById(R.id.toolbarVitals);

        etSystolic = findViewById(R.id.etSystolic);
        etDiastolic = findViewById(R.id.etDiastolic);
        etBpNote = findViewById(R.id.etBpNote);

        etHeartRate = findViewById(R.id.etHeartRate);
        etSpo2 = findViewById(R.id.etSpo2);
        etGlucose = findViewById(R.id.etGlucose);

        btnSave = findViewById(R.id.btnSaveVitals);

        setupToolbar();
        setupSave();
        setupBottomNav();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_calendar) {
                Toast.makeText(this, "Calendário (em breve)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        if (nav == null) return;

        nav.setSelectedItemId(R.id.nav_signs);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_signs) {
                return true; // já está aqui
            }

            if (id == R.id.nav_diary) {
                // Tenta abrir se existir (sem quebrar compilação)
                Intent i = new Intent();
                i.setClassName(getPackageName(), "com.example.simodapp.ui.diary.DiaryActivity");
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Diário (em construção)", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            if (id == R.id.nav_menu) {
                Intent i = new Intent();
                i.setClassName(getPackageName(), "com.example.simodapp.ui.menu.MenuActivity");
                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "Menu (em construção)", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            return false;
        });
    }

    private void setupSave() {
        btnSave.setOnClickListener(v -> {
            Integer sys = parseInt(etSystolic);
            Integer dia = parseInt(etDiastolic);
            Integer hr = parseInt(etHeartRate);
            Integer spo2 = parseInt(etSpo2);
            Integer glu = parseInt(etGlucose);

            if (sys == null || dia == null || hr == null || spo2 == null || glu == null) {
                Toast.makeText(this, "Preencha todos os campos numéricos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!inRange(sys, 60, 250)) { toast("Pressão sistólica inválida"); return; }
            if (!inRange(dia, 30, 150)) { toast("Pressão diastólica inválida"); return; }
            if (!inRange(hr, 30, 220)) { toast("Frequência cardíaca inválida"); return; }
            if (!inRange(spo2, 50, 100)) { toast("Saturação inválida"); return; }
            if (!inRange(glu, 40, 500)) { toast("Glicemia inválida"); return; }

            String note = safeText(etBpNote);

            long now = System.currentTimeMillis();
            SharedPreferences prefs = getSharedPreferences("SIMOD_VITALS", MODE_PRIVATE);
            prefs.edit()
                    .putLong("last_timestamp", now)
                    .putInt("bp_sys", sys)
                    .putInt("bp_dia", dia)
                    .putString("bp_note", note)
                    .putInt("heart_rate", hr)
                    .putInt("spo2", spo2)
                    .putInt("glucose", glu)
                    .apply();

            Toast.makeText(this, "Sinais vitais salvos!", Toast.LENGTH_SHORT).show();
        });
    }

    private Integer parseInt(EditText et) {
        try {
            String s = safeText(et);
            if (s.isEmpty()) return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String safeText(EditText et) {
        if (et == null || et.getText() == null) return "";
        return et.getText().toString().trim();
    }

    private boolean inRange(int v, int min, int max) {
        return v >= min && v <= max;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
