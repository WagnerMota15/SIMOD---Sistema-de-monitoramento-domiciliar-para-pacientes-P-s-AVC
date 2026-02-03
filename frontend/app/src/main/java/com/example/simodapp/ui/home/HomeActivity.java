package com.example.simodapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.ui.auth.LoginActivity;
import com.example.simodapp.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView txtHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_home);

        toolbar = findViewById(R.id.toolbarHome);
        txtHello = findViewById(R.id.txtHello);

        setupHeader();
        setupClicks();
        setupBottomNav();
    }

    private void setupHeader() {
        // 1) Data no topo (ex: "Seg, 9")
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d", new Locale("pt", "BR"));
        String date = sdf.format(new Date()).replace(".", "");
        if (!date.isEmpty()) {
            date = date.substring(0, 1).toUpperCase() + date.substring(1);
        }
        toolbar.setTitle(date);

        // 2) Nome (sem depender de getUserName)
        String nome = null;

        try {
            SessionManager sm = new SessionManager(this);

            // Se estiver logado, mostra uma saudação genérica (sem quebrar o projeto do seu colega)
            if (sm.isLogged()) {
                nome = "usuário"; // você pode trocar por "paciente" ou deixar null
            }
        } catch (Exception ignored) {}

        if (nome == null || nome.trim().isEmpty()) {
            txtHello.setText("Olá!");
        } else {
            txtHello.setText("Olá, " + nome + "!");
        }
    }

    private void setupClicks() {

        // Abre a tela de Sinais Vitais (VitalsActivity)
        findViewById(R.id.cardVitals).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, com.example.simodapp.ui.vitals.VitalsActivity.class))
        );

        // Por enquanto mantém Toast
        findViewById(R.id.cardExercises).setOnClickListener(v ->
                Toast.makeText(this, "Abrir Exercícios", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnEmergency).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, com.example.simodapp.ui.emergency.EmergencyActivity.class))
        );

        toolbar.setNavigationOnClickListener(v ->
                Toast.makeText(this, "Voltar", Toast.LENGTH_SHORT).show()
        );

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_calendar) {
                Toast.makeText(this, "Abrir calendário", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_home);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_chat) {
                Toast.makeText(this, "Abrir Chat", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_signs) {
                Toast.makeText(this, "Abrir Sinais", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_diary) {
                Toast.makeText(this, "Abrir Diário", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_menu) {
                Toast.makeText(this, "Abrir Menu", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }
}
