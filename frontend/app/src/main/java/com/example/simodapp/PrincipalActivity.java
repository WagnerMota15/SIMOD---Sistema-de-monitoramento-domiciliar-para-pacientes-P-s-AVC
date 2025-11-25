package com.example.simodapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Exemplo
        TextView textView = new TextView(this);
        textView.setText("BEM-VINDO!\n\nVocê logou com sucesso!");
        textView.setTextSize(28);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setPadding(50, 200, 50, 200);

        setContentView(textView);
    }
}