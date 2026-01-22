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
import com.example.simodapp.ui.main.RegisterActivity;
import com.example.simodapp.viewmodel.LoginViewModel;

import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etLogin, etPassword;
    private TextView txtCadastreSe;
    private LoginViewModel viewModel;


    //private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        etLogin = findViewById(R.id.etCampoLogin);
        etPassword= findViewById(R.id.etCampoSenha);
        btnLogin = findViewById(R.id.btnLogin);

        //txtEsqueceuSenha = findViewById(R.id.textView2);
        txtCadastreSe = findViewById(R.id.txtCadastreSe);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        actionsConfig();


//        authApi = RetrofitClient.getClient().create(AuthApi.class);


    }


    public void actionsConfig(){
        txtCadastreSe.setOnClickListener(v -> {
            //ir para tela de cadastro ao clicar no Cadastrar-se
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(login.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(login,password);

        });




    }

}
