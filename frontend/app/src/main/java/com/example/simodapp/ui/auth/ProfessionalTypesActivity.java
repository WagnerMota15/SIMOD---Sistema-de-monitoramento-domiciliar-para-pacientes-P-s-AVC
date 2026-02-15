package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.viewmodel.RoleViewModel;

public class ProfessionalTypesActivity extends AppCompatActivity {

    private RoleViewModel roleViewModel;

    private Button btnMedico;
    private Button btnFonoaudiologo;
    private Button btnFisioterapeuta;
    private Button btnNutricionista;
    private Button btnOutroPsicologo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escolha_profissional);

        // Inicializa botões
        btnMedico = findViewById(R.id.btnMedico);
        btnFonoaudiologo = findViewById(R.id.btnFono);
        btnFisioterapeuta = findViewById(R.id.btnFisio);
        btnNutricionista = findViewById(R.id.btnNutricionista);
        btnOutroPsicologo = findViewById(R.id.btnPsico);

        // Configura clique de cada botão e atribui o Role de acordo com cada botão selecionado
        btnMedico.setOnClickListener(v -> selectRoleAndGo(Role.MEDICO));
        btnFonoaudiologo.setOnClickListener(v -> selectRoleAndGo(Role.FONOAUDIOLOGO));
        btnFisioterapeuta.setOnClickListener(v -> selectRoleAndGo(Role.FISIOTERAPEUTA));
        btnNutricionista.setOnClickListener(v -> selectRoleAndGo(Role.NUTRICIONISTA));
        btnOutroPsicologo.setOnClickListener(v -> selectRoleAndGo(Role.PSICOLOGO));
    }

    private void selectRoleAndGo(Role role) {

        // Vai para a tela de registro passando a role
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("role", role.name()); // passa como string
        startActivity(intent);
        finish(); // fecha esta activity para não voltar
    }
}
