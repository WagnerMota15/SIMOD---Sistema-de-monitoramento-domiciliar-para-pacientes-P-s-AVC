package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;

public class UserTypesActivity extends AppCompatActivity {

    private Button btnPatient;
    private Button btnCaregiver;
    private Button btnProfessional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_types);

        // Inicializa Views
        btnPatient = findViewById(R.id.button2);
        btnCaregiver = findViewById(R.id.button4);
        btnProfessional = findViewById(R.id.button5);

        // Botão PACIENTE quando acionado,seta o role para Paciente
        btnPatient.setOnClickListener(v -> goToRegisterActivity(Role.PACIENTE));

        // Botão CUIDADOR faz exatamente a mesma coisa que o botão do PACIENTE
        btnCaregiver.setOnClickListener(v -> goToRegisterActivity(Role.CUIDADOR));

        // Botão PROFISSIONAL -> tela de escolha de profissional
        btnProfessional.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfessionalTypesActivity.class);
            startActivity(intent);
        });
    }

    private void goToRegisterActivity(Role role) {
        Intent intent = new Intent(this, RegisterActivity.class);
         // Passa o Role como string para a próxima activity
        intent.putExtra("role", role.name());
        startActivity(intent);
        finish();
    }
}
