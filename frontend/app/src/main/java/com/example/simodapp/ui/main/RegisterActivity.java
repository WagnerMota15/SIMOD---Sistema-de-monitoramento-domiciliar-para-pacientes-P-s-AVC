package com.example.simodapp.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.viewmodel.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    public static final StrokeTypes STROKE = StrokeTypes.valueOf("HEMORRAGICO");
    public static final Role ROLE = Role.valueOf("PACIENTE");


    private EditText name,cpf,email,telephone,password;
    private Button register;
    private ImageButton back;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        name = findViewById(R.id.etCampoNome);
        cpf = findViewById(R.id.etCampoCpf);
        email = findViewById(R.id.etCampoEmail);
        telephone = findViewById(R.id.etCampoTelefone);
        password = findViewById(R.id.etCampoCriarSenha);

        back = findViewById(R.id.btnVoltar);
        register = findViewById(R.id.btnCadastro);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        actionsConfig();

    }

    public void actionsConfig(){

        back.setOnClickListener(v -> {
            finish();
        });

        register.setOnClickListener(v -> {

            String nameComplete = name.getText().toString().trim();
            String CPF = cpf.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Telephone = telephone.getText().toString().trim();
            String Password = password.getText().toString().trim();

            if(nameComplete.isEmpty() || CPF.isEmpty() || Email.isEmpty() || Telephone.isEmpty() ||Password.isEmpty()){
                Toast.makeText(this,"Preencha todos os campos",Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.register(nameComplete,CPF,Email,Telephone,Password,Role.valueOf(String.valueOf(ROLE)), StrokeTypes.valueOf(String.valueOf(STROKE)),null);
        });

    }

}