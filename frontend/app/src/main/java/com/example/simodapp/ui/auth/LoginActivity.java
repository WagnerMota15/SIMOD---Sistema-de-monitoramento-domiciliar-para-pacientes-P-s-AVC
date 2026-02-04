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
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etLogin, etPassword;
    private TextView txtCadastreSe;

    private LoginViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        sessionManager = new SessionManager(this);

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
        // Inicializa o ViewModel diretamente passando o SessionManager
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
                return (T) new LoginViewModel(sessionManager);
            }
        }).get(LoginViewModel.class);
    }

    private void observeViewModel() {
        viewModel.getLoginSucess().observe(this, response -> {
            if (response != null && response.getToken() != null) {
                // Salva token
                sessionManager.saveToken(response.getToken());

                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

//                // Ir para próxima tela
                startActivity(new Intent(this, HomeActivity.class));
                //com o finish,não permite voltar á tela anterior(login),se voltar,o usuário sai do app
                finish();
            }
        });

        viewModel.getLoginError().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionsConfig() {
        txtCadastreSe.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
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
