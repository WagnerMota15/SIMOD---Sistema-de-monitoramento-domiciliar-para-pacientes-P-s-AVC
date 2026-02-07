package com.example.simodapp.ui.diary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.ui.menu.MenuActivity;
import com.example.simodapp.ui.vitals.VitalsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity {

    private TextView txtDiaryDate, txtChecklistTitle;
    private CheckBox cb1, cb2, cb3, cb4, cb5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_diario);

        MaterialToolbar toolbar = findViewById(R.id.toolbarDiary);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_calendar) {
                Toast.makeText(this, "Calendário (depois)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        txtDiaryDate = findViewById(R.id.txtDiaryDate);
        txtChecklistTitle = findViewById(R.id.txtChecklistTitle);

        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);

        setupDate();
        setupChecklistCounter();
        setupBottomNav();
    }

    private void setupDate() {
        // Ex: "Hoje, sexta-feira, 24 abril"
        Locale ptBR = new Locale("pt", "BR");
        SimpleDateFormat sdf = new SimpleDateFormat("'Hoje,' EEEE, d MMMM", ptBR);
        String date = sdf.format(new Date());
        // primeira letra maiúscula
        if (!date.isEmpty()) date = date.substring(0, 1).toUpperCase() + date.substring(1);
        txtDiaryDate.setText(date);
    }

    private void setupChecklistCounter() {
        updateCounter();

        CheckBox[] all = new CheckBox[]{cb1, cb2, cb3, cb4, cb5};
        for (CheckBox cb : all) {
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> updateCounter());
        }
    }

    private void updateCounter() {
        int total = 5;
        int done = 0;
        if (cb1.isChecked()) done++;
        if (cb2.isChecked()) done++;
        if (cb3.isChecked()) done++;
        if (cb4.isChecked()) done++;
        if (cb5.isChecked()) done++;

        txtChecklistTitle.setText("Checklist de Hoje (" + done + "/" + total + " concluídos)");
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNavDiary);
        nav.setSelectedItemId(R.id.nav_diary);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_diary) return true;

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_signs) {
                startActivity(new Intent(this, VitalsActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_menu) {
                startActivity(new Intent(this, MenuActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}
