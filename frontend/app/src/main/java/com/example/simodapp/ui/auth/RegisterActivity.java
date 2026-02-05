package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.RegisterViewModel;
import com.example.simodapp.viewmodel.RegisterViewModelFactory;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;

    private EditText name, cpf, email, telephone, password, confirmPassword;
    private Button proceed;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        name = findViewById(R.id.etCampoNome);
        cpf = findViewById(R.id.etCampoCpf);
        email = findViewById(R.id.etCampoEmail);
        telephone = findViewById(R.id.etCampoTelefone);
        password = findViewById(R.id.etCampoCriarSenha);
        confirmPassword = findViewById(R.id.etCampoConfirmarSenha);
        proceed = findViewById(R.id.btnContinuarCadastro);
        back = findViewById(R.id.btnVoltar);

        SessionManager sessionManager = new SessionManager(this);
        AuthApi api = sessionManager.createAuthApi();
        AuthRepository repository = new AuthRepository(api);

        viewModel = new ViewModelProvider(
                this,
                new RegisterViewModelFactory(repository)
        ).get(RegisterViewModel.class);

        back.setOnClickListener(v -> finish());

        proceed.setOnClickListener(v -> {
            if (!password.getText().toString()
                    .equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.register(
                    name.getText().toString().toUpperCase(),
                    cpf.getText().toString(),
                    email.getText().toString(),
                    telephone.getText().toString(),
                    password.getText().toString()
            );

            startActivity(new Intent(this, UserTypesActivity.class));
        });
    }
}
