package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.RegisterViewModel;
import com.example.simodapp.viewmodel.RegisterViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.UUID;

public class FinalRegisterActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView actStrokeType;
    private MaterialButton btnContinuar;
    private ImageButton btnVoltar;

    private StrokeTypes selectedStrokeType;

    private RegisterViewModel registerViewModel;

    // Dados vindos do RegisterActivity
    private String name, cpf, email, telephone, password;
    private Role role;

    // Flag para evitar múltiplos disparos do LiveData
    private boolean handled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroke_type_activity);

        bindViews();
        recoverIntentData();
        setupViewModel();
        setupStrokeDropdown();

        btnVoltar.setOnClickListener(v -> finish());

        btnContinuar.setOnClickListener(v -> {
            if (!validate()) return;

            registerViewModel.register(
                    name,
                    cpf,
                    email,
                    password,
                    telephone,
                    role,
                    selectedStrokeType,
                    null
            );
        });

        observeRegister();
    }

    private void bindViews() {
        actStrokeType = findViewById(R.id.actStrokeType);
        btnContinuar = findViewById(R.id.btnContinuarStroke);
        btnVoltar = findViewById(R.id.btnVoltarStroke);
    }

    private void recoverIntentData() {
        name = getIntent().getStringExtra("name");
        cpf = getIntent().getStringExtra("cpf");
        email = getIntent().getStringExtra("email");
        telephone = getIntent().getStringExtra("telephone");
        password = getIntent().getStringExtra("password");
        role = Role.valueOf(getIntent().getStringExtra("role"));
    }

    private void setupViewModel() {
        SessionManager sessionManager = new SessionManager(this);

        AuthApi authApi = com.example.simodapp.data.api.RetrofitClient
                .getClient(sessionManager)
                .create(AuthApi.class);

        AuthRepository repository =
                new AuthRepository(authApi, sessionManager);

        registerViewModel = new ViewModelProvider(
                this,
                new RegisterViewModelFactory(repository)
        ).get(RegisterViewModel.class);
    }

    private void setupStrokeDropdown() {
        ArrayAdapter<StrokeTypes> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        StrokeTypes.values()
                );

        actStrokeType.setAdapter(adapter);
        actStrokeType.setOnClickListener(v -> actStrokeType.showDropDown());

        actStrokeType.setOnItemClickListener((parent, view, position, id) ->
                selectedStrokeType = (StrokeTypes) parent.getItemAtPosition(position)
        );
    }

    private boolean validate() {
        if (selectedStrokeType == null) {
            Toast.makeText(this, "Selecione o tipo de AVC", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void observeRegister() {
        registerViewModel.getRegisterSuccess().observe(this, new Observer<com.example.simodapp.data.dto.RegisterResponse>() {
            @Override
            public void onChanged(com.example.simodapp.data.dto.RegisterResponse response) {
                if (handled || response == null) return;
                handled = true;

                Log.d("FinalRegisterActivity", "Cadastro realizado com sucesso: " + response.getId());

                try {
                    UUID patientId = UUID.fromString(response.getId());
                    Intent intent = new Intent(FinalRegisterActivity.this, AddressFamilyActivity.class);
                    intent.putExtra("patientId", patientId.toString());
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("FinalRegisterActivity", "Erro ao abrir AddressFamilyActivity", e);
                    Toast.makeText(FinalRegisterActivity.this, "Erro ao abrir próxima tela", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Log.e("FinalRegisterActivity", "Erro no cadastro: " + error);
                Toast.makeText(FinalRegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
