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
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    //public static final StrokeTypes STROKE = StrokeTypes.valueOf("HEMORRAGICO");
    //public static final Role ROLE = Role.valueOf("PACIENTE");


    private EditText name,cpf,email,telephone,password,confirmPassword;
    private Button proceed;
    private ImageButton back;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        name = findViewById(R.id.etCampoNome);
        cpf = findViewById(R.id.etCampoCpf);
        cpfWatcher(cpf);
        email = findViewById(R.id.etCampoEmail);
        telephone = findViewById(R.id.etCampoTelefone);
        password = findViewById(R.id.etCampoCriarSenha);
        confirmPassword = findViewById(R.id.etCampoConfirmarSenha);

        back = findViewById(R.id.btnVoltar);
        proceed= findViewById(R.id.btnContinuarCadastro);

        // Inicializa SessionManager
        SessionManager sessionManager = new SessionManager(this);

        // Inicializa ViewModel passando o SessionManager
        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory() {
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
                return (T) new RegisterViewModel(sessionManager);
            }
        }).get(RegisterViewModel.class);

        actionsConfig();

    }

    public void actionsConfig(){

        back.setOnClickListener(v -> {
            finish();
        });

        proceed.setOnClickListener(v -> {

            String nameComplete = name.getText().toString().toUpperCase().trim();
            String CPF = cpf.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Telephone = telephone.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String ConfirmPassword = confirmPassword.getText().toString().trim();

            if(nameComplete.isEmpty() || CPF.isEmpty() || Email.isEmpty() || Telephone.isEmpty() ||Password.isEmpty()){
                Toast.makeText(this,"PREENCHA TODOS OS CAMPOS",Toast.LENGTH_SHORT).show();
                return;
            }

            if(!Password.equals(ConfirmPassword)){
                Toast.makeText(this, "SENHAS NÃO ESTÃO IGUAIS", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(RegisterActivity.this,FinalRegisterActivity.class);

            // aqui eu consigo passar dados dessa activity para a próxima activity
            intent.putExtra("name", nameComplete);
            intent.putExtra("cpf", CPF);
            intent.putExtra("email", Email);
            intent.putExtra("telephone", Telephone);
            intent.putExtra("password", Password);

            startActivity(intent);

//            viewModel.register(nameComplete,CPF,Email,Telephone,Password,Role.valueOf(String.valueOf(ROLE)), StrokeTypes.valueOf(String.valueOf(STROKE)),null);
        });

    }

    private void cpfWatcher(EditText cpf){
        cpf.addTextChangedListener(new TextWatcher() {
            boolean isUpdating = false;
            @Override
            public void afterTextChanged(Editable s) {
                if(isUpdating) return;
                isUpdating = true;

                String digits = s.toString().replaceAll("[^\\d]", "");
                String formatted = "";
                for(int i = 0;i<digits.length() && i<11;i++){
                    if(i==3 || i ==6){
                        formatted += ".";
                    }
                    if(i==9){
                        formatted += "-";
                    }
                    formatted += digits.charAt(i);

                }
                cpf.setText(formatted);
                cpf.setSelection(formatted.length());
                isUpdating=false;
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