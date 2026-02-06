package com.example.simodapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simodapp.R;
import com.example.simodapp.data.api.CepService;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.dto.CepResponse;
import com.example.simodapp.domain.enums.Kinship;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.ui.paciente.adapter.FamilyAdapter;
import com.example.simodapp.viewmodel.AddressFamilyViewModel;
import com.example.simodapp.viewmodel.AddressFamilyViewModelFactory;
import com.google.android.material.button.MaterialButton;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFamilyActivity extends AppCompatActivity {

    // ENDEREÇO
    private EditText etCep, etLogradouro, etBairro, etCidade, etUF, etNumero;

    // CONTATOS
    private RecyclerView rvContatos;
    private FamilyAdapter familyAdapter;

    private MaterialButton btnAddContato, btnFinalizar;
    private ImageButton btnVoltar;

    private AddressFamilyViewModel viewModel;
    private UUID patientId;

    private boolean findAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_family);

        initViews();
        recoverIntent();
        setupRecycler();
        setupViewModel();
        setupActions();
        configSearchCep();  // ← Configuração da máscara e busca automática
    }

    private void initViews() {
        etCep = findViewById(R.id.etCampoCep);
        etLogradouro = findViewById(R.id.etCampoLogradouro);
        etBairro = findViewById(R.id.etCampoBairro);
        etCidade = findViewById(R.id.etCampoCidade);
        etUF = findViewById(R.id.etCampoUF);
        etNumero = findViewById(R.id.etCampoNumero);

        rvContatos = findViewById(R.id.rvContatos);
        btnAddContato = findViewById(R.id.btnAddContato);
        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnVoltar = findViewById(R.id.btnVoltar);
    }

    private void recoverIntent() {
        String id = getIntent().getStringExtra("patientId");
        if (id == null) {
            finish();
            return;
        }
        patientId = UUID.fromString(id);
    }

    private void setupRecycler() {
        familyAdapter = new FamilyAdapter(position ->
                viewModel.removeFamily(position));

        rvContatos.setLayoutManager(new LinearLayoutManager(this));
        rvContatos.setAdapter(familyAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                new AddressFamilyViewModelFactory(this)
        ).get(AddressFamilyViewModel.class);

        viewModel.getFamilyList().observe(this, familyAdapter::addFamily);

        viewModel.getSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Cadastro finalizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        });

        viewModel.getError().observe(this,
                error -> Toast.makeText(this, error, Toast.LENGTH_LONG).show());
    }

    private void setupActions() {
        btnVoltar.setOnClickListener(v -> finish());

        btnAddContato.setOnClickListener(v -> showDialogAddContact());

        btnFinalizar.setOnClickListener(v -> {
            Log.d("AddressFamily", "Clique em Finalizar - patientId: " + patientId);
            Log.d("AddressFamily", "Endereço: CEP=" + etCep.getText().toString().trim() +
                    " | Logradouro=" + etLogradouro.getText().toString().trim() +
                    " | Número=" + etNumero.getText().toString().trim());

            AddressRequest address = new AddressRequest(
                    etCep.getText().toString().trim(),
                    etLogradouro.getText().toString().trim(),
                    etBairro.getText().toString().trim(),
                    etCidade.getText().toString().trim(),
                    etUF.getText().toString().trim(),
                    etNumero.getText().toString().trim()
            );

            viewModel.finalizarCadastro(patientId, address);
        });
    }

    private void showDialogAddContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Novo Contato");

        final EditText etNome = new EditText(this);
        etNome.setHint("Nome");

        final EditText etTelefone = new EditText(this);
        etTelefone.setHint("Telefone");

        final Spinner spinner = new Spinner(this);
        spinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                Kinship.values()
        ));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);
        layout.addView(etNome);
        layout.addView(etTelefone);
        layout.addView(spinner);

        builder.setView(layout);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String nome = etNome.getText().toString().trim();
            String telefone = etTelefone.getText().toString().trim();
            Kinship kinship = (Kinship) spinner.getSelectedItem();

            if (!nome.isEmpty() && !telefone.isEmpty()) {
                viewModel.addFamily(nome, telefone, kinship);
            } else {
                Toast.makeText(this, "Preencha nome e telefone", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // Configuração da máscara e busca automática (estilo similar ao CPF)
    private void configSearchCep() {
        etCep.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (isUpdating) {
                        isUpdating = false;
                        return;
                    }

                    isUpdating = true;

                    // Limpa caracteres não numéricos
                    String str = s.toString().replaceAll("[^0-9]", "");

                    // Aplica máscara
                    String masked = "";
                    if (str.length() > 5) {
                        masked = str.substring(0, 5) + "-" + str.substring(5, Math.min(8, str.length()));
                    } else {
                        masked = str;
                    }

                    // Atualiza o texto
                    if (!masked.equals(s.toString())) {
                        s.replace(0, s.length(), masked);
                        // Adia o posicionamento do cursor
                        String finalMasked = masked;
                        etCep.post(() -> {
                            if (etCep.getText().length() >= finalMasked.length()) {
                                etCep.setSelection(finalMasked.length());
                            }
                        });
                    }

                    // Remove hífen para validação
                    String cepClean = masked.replace("-", "");

                    if (cepClean.length() < 8) {
                        findAddress = false;
                        clearData();
                        blockAll(false);
                        return;
                    }

                    if (cepClean.length() == 8 && !findAddress) {
                        findAddress = true;
                        loadingAddress();
                        searchAddressForCep(cepClean);
                    }

                    isUpdating = false;
                } catch (Exception e) {
                    Toast.makeText(AddressFamilyActivity.this, "Erro ao processar CEP: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    findAddress = false;
                    clearData();
                    blockAll(false);
                }
            }
        });
    }

    private void searchAddressForCep(String cep) {
        try {
            CepService cepService = RetrofitClient.getPublicClient().create(CepService.class);
            Call<CepResponse> call = cepService.searchCep(cep);

            call.enqueue(new Callback<CepResponse>() {
                @Override
                public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                    findAddress = false;

                    if (response.isSuccessful() && response.body() != null && !response.body().isErro()) {
                        CepResponse res = response.body();

                        etLogradouro.setText(res.getLogradouro() != null ? res.getLogradouro() : "");
                        etBairro.setText(res.getBairro() != null ? res.getBairro() : "");
                        etCidade.setText(res.getLocalidade() != null ? res.getLocalidade() : "");
                        etUF.setText(res.getUf() != null ? res.getUf() : "");

                        analyzeAddress(res);
                        etNumero.requestFocus();
                    } else {
                        clearData();
                        blockAll(false);
                        Toast.makeText(AddressFamilyActivity.this, "CEP INVÁLIDO ou não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CepResponse> call, Throwable t) {
                    findAddress = false;
                    clearData();
                    blockAll(false);
                    Toast.makeText(AddressFamilyActivity.this, "Falha ao consultar CEP: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            findAddress = false;
            clearData();
            blockAll(false);
            Toast.makeText(this, "Erro ao configurar busca de CEP: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void analyzeAddress(CepResponse address) {
        if ((address.getLogradouro() == null || address.getLogradouro().isEmpty()) &&
                (address.getBairro() == null || address.getBairro().isEmpty())) {
            blockData(); // Permite editar rua e bairro
        } else {
            blockAll(true); // Bloqueia campos preenchidos pela API
        }
    }

    private void clearData() {
        etLogradouro.setText("");
        etBairro.setText("");
        etCidade.setText("");
        etUF.setText("");
    }

    private void blockData() {
        etLogradouro.setEnabled(true);
        etBairro.setEnabled(true);
        etCidade.setEnabled(false);
        etUF.setEnabled(false);
    }

    private void blockAll(boolean block) {
        etLogradouro.setEnabled(block);
        etBairro.setEnabled(block);
        etCidade.setEnabled(block);
        etUF.setEnabled(block);
    }

    private void loadingAddress() {
        etLogradouro.setText("Buscando...");
        etBairro.setText("Buscando...");
        etCidade.setText("Buscando...");
        etUF.setText("...");
    }
}