package com.example.simodapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onResume() {
        super.onResume();
        refreshRemindersOnHome();
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
                startActivity(new Intent(this, com.example.simodapp.ui.vitals.VitalsActivity.class))
        );

        // Abre a tela de Lembretes (novo)
        View cardReminders = findViewById(R.id.cardReminders);
        if (cardReminders != null) {
            cardReminders.setOnClickListener(v ->
                    startActivity(new Intent(this, com.example.simodapp.ui.reminders.RemindersActivity.class))
            );
        }

        View rowReminder1 = findViewById(R.id.rowReminder1);
        if (rowReminder1 != null) {
            rowReminder1.setOnClickListener(v ->
                    startActivity(new Intent(this, com.example.simodapp.ui.reminders.RemindersActivity.class))
            );
        }

        // Por enquanto mantém Toast
        findViewById(R.id.cardExercises).setOnClickListener(v ->
                Toast.makeText(this, "Abrir Exercícios", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnEmergency).setOnClickListener(v ->
                startActivity(new Intent(this, com.example.simodapp.ui.emergency.EmergencyActivity.class))
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

            if (id == R.id.nav_home) return true;

            if (id == R.id.nav_signs) {
                startActivity(new Intent(this, com.example.simodapp.ui.vitals.VitalsActivity.class));
                return true;
            }

            if (id == R.id.nav_diary) {
                startActivity(new Intent(this, com.example.simodapp.ui.diary.DiaryActivity.class));
                return true;
            }

            if (id == R.id.nav_menu) {
                startActivity(new Intent(this, com.example.simodapp.ui.menu.MenuActivity.class));
                return true;
            }

            return false;
        });
    }



    // ====== Atualiza "Próximos Lembretes" na Home lendo do SharedPreferences ======
    private void refreshRemindersOnHome() {
        TextView txtReminder1 = findViewById(R.id.txtReminder1);
        TextView txtMore = findViewById(R.id.txtRemindersMore);

        // Se sua tela_home ainda não tiver esses IDs, não quebra
        if (txtReminder1 == null || txtMore == null) return;

        String raw = getSharedPreferences("SIMOD_REMINDERS", MODE_PRIVATE)
                .getString("list", "");

        ArrayList<String> list = new ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            String[] lines = raw.split("\n");
            for (String s : lines) {
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) list.add(s);
                }
            }
        }

        if (list.isEmpty()) {
            txtReminder1.setText("Nenhum lembrete. Toque para criar.");
            txtMore.setText("");
            txtMore.setVisibility(View.INVISIBLE);
            return;
        }

        // Mostra o primeiro item (ex: "08:00  —  Tomar remédios")
        // Formato da home: "+ às 08:00   Tomar remédios"
        String first = list.get(0);
        String hhmm = first.length() >= 5 ? first.substring(0, 5) : "";
        String desc = first;
        int idx = first.indexOf("—");
        if (idx >= 0 && idx + 1 < first.length()) {
            desc = first.substring(idx + 1).trim();
        } else if (first.length() > 5) {
            desc = first.substring(5).replace("-", "").trim();
        }

        if (!hhmm.isEmpty()) {
            txtReminder1.setText("+ às " + hhmm + "   " + desc);
        } else {
            txtReminder1.setText(desc);
        }

        int extra = list.size() - 1;
        if (extra > 0) {
            txtMore.setText("+" + extra);
            txtMore.setVisibility(View.VISIBLE);
        } else {
            txtMore.setText("");
            txtMore.setVisibility(View.INVISIBLE);
        }
    }
}
