package com.example.simodapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText nomeCompleto,cpf,email,senha,telefone;
    private Button botaoCadastro;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nomeCompleto = findViewById(R.id.editTextText);
        cpf = findViewById(R.id.editTextText2);
        email = findViewById(R.id.editTextText3);
        telefone = findViewById(R.id.editTextText4);
        senha = findViewById(R.id.editTextText5);
        botaoCadastro = findViewById(R.id.button);

        apiService = APIBackend.getClient().create(APIService.class);

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerCadastro();
            }
        });



    }

    private void fazerCadastro() {
        //Ler os dados dos EditTexts
        String nome = nomeCompleto.getText().toString().trim();
        String CPF = cpf.getText().toString().trim();
        String EMAIL = email.getText().toString().trim();
        String password = senha.getText().toString().trim();
        String telephone = telefone.getText().toString().trim();

        //Cria o objeto UserRequest
        UserRequest userRequest = new UserRequest(nome, CPF, EMAIL, password, telephone);

        Call<User> call = apiService.criarUsuario(userRequest);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // 7. Tratar a resposta
                if (response.isSuccessful() && response.body() != null) {
                    User usuarioCriado = response.body();
                    Toast.makeText(MainActivity.this, "Usuário criado com ID: " + usuarioCriado.getId(), Toast.LENGTH_LONG).show();



                } else {
                    // Falha (ex: CPF já existe, erro de servidor)
                    Log.e("APIError", "Erro ao cadastrar. Código: " + response.code() + " | Mensagem: " + response.message());
                    Toast.makeText(MainActivity.this, "Erro ao cadastrar. Verifique os dados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // 8. Tratar falha de rede (ex: sem internet, backend offline)
                Log.e("NetworkError", "Falha na conexão: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}