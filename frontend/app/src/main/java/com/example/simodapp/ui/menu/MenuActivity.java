package com.example.simodapp.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.ui.links.Links_Menu;
import com.example.simodapp.ui.vitals.VitalsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu);

        MaterialToolbar toolbar = findViewById(R.id.toolbarMenu);
        toolbar.setNavigationOnClickListener(v -> finish());

        findViewById(R.id.rowLinks).setOnClickListener(v ->
                startActivity(new Intent(this, Links_Menu.class))
        );

        findViewById(R.id.rowAbout).setOnClickListener(v ->
                Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show()
        );

        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNavMenu);
        nav.setSelectedItemId(R.id.nav_menu);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_menu) return true;

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

            if (id == R.id.nav_diary) {
                startActivity(new Intent(this, com.example.simodapp.ui.diary.DiaryActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}
