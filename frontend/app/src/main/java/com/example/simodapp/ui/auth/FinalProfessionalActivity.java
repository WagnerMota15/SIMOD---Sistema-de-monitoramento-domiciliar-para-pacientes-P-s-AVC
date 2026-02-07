package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.R;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.state.FinalProfessionalUiState;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.viewmodel.FinalProfessionalViewModel;
import com.example.simodapp.viewmodel.RegisterViewModel;
import com.example.simodapp.viewmodel.ViewModelFactory;

import java.util.Locale;

public class FinalProfessionalActivity extends AppCompatActivity {

    private FinalProfessionalViewModel viewModel;

    private EditText council;
    private EditText registration;
    private EditText uf;
    private Button confirm;

    private FinalProfessionalViewModel finalProfessionalViewModel;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_register_professional);

        council = findViewById(R.id.etCampoCouncil);
        registration = findViewById(R.id.etCampoRegistration);
        uf = findViewById(R.id.etCampoUf);
        confirm = findViewById(R.id.btnFinalizarCadastroProfissional);

        // dados vindos da RegisterActivity
        String name = getIntent().getStringExtra("name");
        String cpf = getIntent().getStringExtra("cpf");
        String email = getIntent().getStringExtra("email");
        String telephone = getIntent().getStringExtra("telephone");
        String password = getIntent().getStringExtra("password");
        Role role = Role.valueOf(getIntent().getStringExtra("role"));

        finalProfessionalViewModel =
                ViewModelFactory.provideFinalProfessional(this);

        registerViewModel =
                ViewModelFactory.provideRegister(this);


        finalProfessionalViewModel.getUiState().observe(this, state -> {

            if (state instanceof FinalProfessionalUiState.Loading) {
                confirm.setEnabled(false);
            }

            if (state instanceof FinalProfessionalUiState.Rejected) {
                confirm.setEnabled(true);
                Toast.makeText(
                        this,
                        ((FinalProfessionalUiState.Rejected) state).message,
                        Toast.LENGTH_LONG
                ).show();
            }

            if (state instanceof FinalProfessionalUiState.Approved) {

                registerViewModel.register(
                        name,
                        cpf,
                        email,
                        password,
                        telephone,
                        role,
                        null,// strokeType
                        registration.getText().toString().trim()
                );

                startActivity(
                        new Intent(this, HomeActivity.class)
                );
                finish();
            }
        });

        confirm.setOnClickListener(v ->
                finalProfessionalViewModel.verifyProfessional(
                        cpf,
                        council.getText().toString().trim(),
                        registration.getText().toString().trim(),
                        uf.getText().toString().trim().toUpperCase()
                )
        );
    }
}

