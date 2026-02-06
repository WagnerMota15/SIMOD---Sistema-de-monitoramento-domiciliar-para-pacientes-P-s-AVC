package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.LoginViewModel;
import com.example.simodapp.viewmodel.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private Button btnLogin;
    private TextView txtCadastreSe;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        initViews();
        initViewModel();
        observeViewModel();
        actionsConfig();
    }

    private void initViews() {
        etLogin = findViewById(R.id.etCampoLogin);
        etPassword = findViewById(R.id.etCampoSenha);
        btnLogin = findViewById(R.id.btnLogin);
        txtCadastreSe = findViewById(R.id.txtCadastreSe);
    }

    private void initViewModel() {
        SessionManager sessionManager = new SessionManager(this);

        AuthApi authApi = RetrofitClient
                .getClient(sessionManager)
                .create(AuthApi.class);

        AuthRepository authRepository = new AuthRepository(authApi, sessionManager);

        viewModel = new ViewModelProvider(
                this,
                new LoginViewModelFactory(authRepository)
        ).get(LoginViewModel.class);
    }

    private void observeViewModel() {

        viewModel.getToken().observe(this, token -> {
            if (token != null) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        });

        viewModel.getError().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionsConfig() {
        txtCadastreSe.setOnClickListener(v ->
                startActivity(new Intent(this, UserTypesActivity.class))
        );

        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(login, password);
        });
    }
}
