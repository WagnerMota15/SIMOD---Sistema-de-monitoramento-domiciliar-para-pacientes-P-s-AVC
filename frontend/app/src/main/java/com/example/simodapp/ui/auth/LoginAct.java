//package com.example.simodapp.ui.auth;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.simodapp.data.api.RetrofitClient;
//import com.example.simodapp.data.api.APIService;
//import com.example.simodapp.data.api.AuthApi;
//import com.example.simodapp.ui.main.MainActivity;
//import com.example.simodapp.ui.main.PrincipalActivity;
//import com.example.simodapp.R;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginAct extends AppCompatActivity {
//    private EditText cpf, senha;
//    private AuthApi authApi;
//    private Button btnLogin;
//    private TextView txtCadastreSe;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tela_login);
//
//        cpf = findViewById(R.id.editTextText2);
//        senha = findViewById(R.id.editTextText5);
//        btnLogin = findViewById(R.id.button2);
//        txtCadastreSe = findViewById(R.id.txtCadastreSe);
//
//        apiService = RetrofitClient.getClient().create(APIService.class);
//
//        btnLogin.setOnClickListener(v -> fazerLogin());
//
//        txtCadastreSe.setOnClickListener(v -> {
//            startActivity(new Intent(LoginAct.this, MainActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        });
//    }
//
//    private void fazerLogin() {
//        String cpf = this.cpf.getText().toString().trim();
//        String senha = this.senha.getText().toString().trim();
//
//        if (cpf.isEmpty() || senha.isEmpty()) {
//            Toast.makeText(this, "Preencha CPF e senha", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        btnLogin.setEnabled(false);
//        btnLogin.setText("Entrando...");
//
//        LoginRequest loginRequest = new LoginRequest(cpf, senha);
//
//        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                btnLogin.setEnabled(true);
//                btnLogin.setText("Fazer login");
//
//                if (response.isSuccessful() && response.body() != null) {
//                    LoginResponse resp = response.body();
//                    UserSession.getInstance(LoginAct.this).saveUser(
//                            resp.getId(),
//                            resp.getNomeComplete(),
//                            resp.getEmail(),
//                            resp.getToken(),
//                            resp.getCPF(),
//                            resp.getTelephone()
//                    );
//
//                    // Toast.makeText(LoginActivity.this, "Bem-vindo(a), " + resp.getNomeComplete() + "!", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(LoginAct.this, PrincipalActivity.class));
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                    finish();
//                } else {
//                    Toast.makeText(LoginAct.this, "CPF ou senha incorretos", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                btnLogin.setEnabled(true);
//                btnLogin.setText("Fazer login");
//                Toast.makeText(LoginAct.this, "Erro de conexão: verifique sua internet", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//   /*
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tela_login);
//
//        TextView txtCadastreSe = findViewById(R.id.txtCadastreSe);
//
//        btnLogin.setOnClickListener(v -> { // Abre a tela principal
//            startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        });
//
//        txtCadastreSe.setOnClickListener(v -> { // Abre tela de cadastro
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        });
//    }
//  */
