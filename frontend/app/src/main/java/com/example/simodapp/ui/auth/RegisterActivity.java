package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, cpf, email, telephone, password, confirmPassword;
    private Button proceed;
    private ImageButton back;

    private Role selectedRole;

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

        //adição da máscara ao cpf
        cpfWatcher(cpf);

        // RECEBE ROLE DA UserTypesActivity e verifica se ele veio nulo ou não
        String roleString = getIntent().getStringExtra("role");
        if (roleString != null) {
            selectedRole = Role.valueOf(roleString);
        } else {
            //valor padrão caso role não tenha sido passado
            finish();
        }

        //
        back.setOnClickListener(v -> finish());

        proceed.setOnClickListener(v -> {

            if (!password.getText().toString()
                    .equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
                return;
            }

            String nameStr = name.getText().toString().trim();
            String cpfStr = cpf.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String telephoneStr = telephone.getText().toString().trim();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();

            // Validação dos campos preenchidos,caso algum não esteja,é mostrado um aviso e não avança para a próxima acitivity
            if (nameStr.isEmpty() || cpfStr.isEmpty() || emailStr.isEmpty() || telephoneStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "PREENCHA TODOS OS CAMPOS", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validação das senhas criada e confirmada iguais
            if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(this, "SENHAS NÃO ESTÃO IGUAIS", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent;

            if (selectedRole == Role.PACIENTE || selectedRole == Role.CUIDADOR) {
                intent = new Intent(this, FinalRegisterActivity.class);
            } else {
                intent = new Intent(this, ProfessionalTypesActivity.class);
            }

            intent.putExtra("name", name.getText().toString().toUpperCase());
            intent.putExtra("cpf", cpf.getText().toString());
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("telephone", telephone.getText().toString());
            intent.putExtra("password", password.getText().toString());
            intent.putExtra("role", selectedRole.name());

            startActivity(intent);
        });
    }

    //função que formata o texto do cpf para 000.000.000-00
    private void cpfWatcher(EditText cpf) {
        cpf.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;

                String digits = s.toString().replaceAll("[^\\d]", "");
                String formatted = "";
                for (int i = 0; i < digits.length() && i < 11; i++) {
                    if (i == 3 || i == 6) {
                        formatted += ".";
                    }
                    if (i == 9) {
                        formatted += "-";
                    }
                    formatted += digits.charAt(i);

                }
                cpf.setText(formatted);
                cpf.setSelection(formatted.length());
                isUpdating = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

    }
}