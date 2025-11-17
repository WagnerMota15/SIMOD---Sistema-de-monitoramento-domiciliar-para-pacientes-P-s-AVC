package com.example.simodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        Button btnLogin = findViewById(R.id.button2);
        TextView txtCadastreSe = findViewById(R.id.txtCadastreSe);

        btnLogin.setOnClickListener(v -> { // Abre a tela principal

        });

        txtCadastreSe.setOnClickListener(v -> { // Abre tela de cadastro
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
