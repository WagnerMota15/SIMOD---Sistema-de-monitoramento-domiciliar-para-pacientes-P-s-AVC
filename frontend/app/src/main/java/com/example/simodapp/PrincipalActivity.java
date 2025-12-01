package com.example.simodapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.simodapp.login.LoginActivity;
import com.example.simodapp.login.UserSession;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UserSession.getInstance(this).isLoggedIn()) {
            // Não está logado → volta pro login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        TextView textView = new TextView(this);
        textView.setText("BEM-VINDO!\n\nVocê logou com sucesso!");
        textView.setTextSize(28);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setPadding(50, 200, 50, 200);

        String nome = UserSession.getInstance(this).getNome();
        String token = UserSession.getInstance(this).getToken();

        setContentView(textView);
    }
}