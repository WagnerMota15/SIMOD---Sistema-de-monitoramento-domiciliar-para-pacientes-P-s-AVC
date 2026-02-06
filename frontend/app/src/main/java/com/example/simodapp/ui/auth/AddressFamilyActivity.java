package com.example.simodapp.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simodapp.R;
import com.example.simodapp.data.api.AddressApi;
import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.FamilyApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.api.RetrofitConfig;
import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.dto.CepResponse;
import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.domain.enums.Kinship;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.ui.home.HomeActivity;
import com.example.simodapp.ui.paciente.adapter.FamilyAdapter;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.viewmodel.FamilyViewModel;
import com.example.simodapp.viewmodel.PatientViewModel;
import com.example.simodapp.viewmodel.RegisterViewModel;
import com.example.simodapp.viewmodel.RegisterViewModelFactory;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

public class FinalRegisterActivity extends AppCompatActivity {

    private FamilyViewModel familyViewModel;
    private FamilyAdapter familyAdapter;
    private RegisterViewModel registerViewModel;

    private MaterialAutoCompleteTextView actStrokeType;

    private EditText etCep,etPublicSpace,etCity,etState,etNeighborhood;
    private Role role;
    private StrokeTypes selectStrokeType;

    private boolean isSearchingAddress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_cadastro_paciente);

        familyViewModel = new ViewModelProvider(this).get(FamilyViewModel.class);
        SessionManager sessionManager = new SessionManager(this);

        AuthApi authApi = RetrofitClient
                .getClient(sessionManager)
                .create(AuthApi.class);

        AuthRepository repository =
                new AuthRepository(authApi, sessionManager);

        registerViewModel = new ViewModelProvider(
                this,
                new RegisterViewModelFactory(repository)
        ).get(RegisterViewModel.class);


        //views
        actStrokeType = findViewById(R.id.actStrokeType);

        etCep = findViewById(R.id.etCampoCep);
        etPublicSpace = findViewById(R.id.etCampoLogradouro);
        etNeighborhood = findViewById(R.id.etCampoBairro);
        etCity = findViewById(R.id.etCampoCidade);
        etState = findViewById(R.id.etCampoUF);

        //recupera os dados da activity anterior(RegisterActivity)
        String name = getIntent().getStringExtra("name");
        String cpf = getIntent().getStringExtra("cpf");
        String email = getIntent().getStringExtra("email");
        String telephone = getIntent().getStringExtra("telephone");
        String password = getIntent().getStringExtra("password");
        role = Role.valueOf(getIntent().getStringExtra("role"));

        setupStrokeDropdown();
        setupFamilySection();
        configSearchCep();

        findViewById(R.id.btnAddContato).setOnClickListener(v -> showDialogAdd());
        findViewById(R.id.btnFinalizar).setOnClickListener(v -> {

            if(!finishProcess()) return;

            //representação do caso de uso de registrar paciente/usuário no sistema
            registerViewModel.register(name,cpf,email,password,telephone,role,selectStrokeType,null);

            registerViewModel.getRegisterSuccess().observe(this,registerResponse -> {

                UUID patientId = UUID.fromString(registerResponse.getUserId());

                //coleto os dados de endereço e transformo em objeto address e depois envio
                AddressRequest addressRequest = buildAddressRequest();
                sendAddress(addressRequest);


                List<FamilyRequest> familyRequests = familyViewModel.getShippingList();
                sendFamilyContacts(patientId,familyRequests);

                Toast.makeText(this, "CADASTRO FINALIZADO COM SUCESSO", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(FinalRegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                //não permitindo que o usuário volte á tela de cadastro após entrar na home
                finish();

            });



        });


    }

    private void setupStrokeDropdown() {
        ArrayAdapter<StrokeTypes> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        StrokeTypes.values()
                );

        actStrokeType.setAdapter(adapter);
        actStrokeType.setOnClickListener(v->actStrokeType.showDropDown());

        actStrokeType.setOnItemClickListener((parent, view, position, id) -> {
            selectStrokeType = (StrokeTypes) parent.getItemAtPosition(position);
        });
    }




    public void setupFamilySection(){

        RecyclerView recyclerView = findViewById(R.id.rvContatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        familyAdapter =
                new FamilyAdapter(position ->
                        familyViewModel.removeContacts(position));

        recyclerView.setAdapter(familyAdapter);

        familyViewModel.contactsFamily.observe(this, list -> {
            familyAdapter.addFamily(list);
        });
    }

    private void showDialogAdd(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_contato,null);
        builder.setView(view);

        EditText etName,etTelephone;
        Spinner spKinship;

        etName= view.findViewById(R.id.etDialogNome);
        etTelephone = view.findViewById(R.id.etDialogTelefone);
        spKinship = view.findViewById(R.id.spDialogParentesco);

        spKinship.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Kinship.values()));

        builder.setPositiveButton("Salvar",(dialog, which) -> {
            String name,telephone;
            name = etName.getText().toString();
            telephone = etTelephone.getText().toString();
            Kinship kinship = (Kinship) spKinship.getSelectedItem();

            if(!name.isEmpty() && !telephone.isEmpty()){
                familyViewModel.addContacts(name,telephone,kinship);
            } else {
                Toast.makeText(this, "Preencha todos os dados", Toast.LENGTH_SHORT).show();
            }

        });

        builder.setNegativeButton("Cancelar",null);
        builder.create().show();


    }


    private void configSearchCep(){
        etCep.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void afterTextChanged(android.text.Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cep = s.toString().replace("-","");

                if(cep.length()<8){
                    isSearchingAddress=false;
                    clearData();
                    blockAll(isSearchingAddress);
                    return;
                }
                if(cep.length() == 8 && !isSearchingAddress){
                    isSearchingAddress=true;
                    loadingAddress();
                    searchAddressForCep(cep);
                }

            }
        });


    }

    private void searchAddressForCep(String cep){

        RetrofitConfig.getCepService().searchCep(cep).enqueue(new retrofit2.Callback<CepResponse>() {
            @Override
            public void onResponse(retrofit2.Call<CepResponse> call, retrofit2.Response<CepResponse> response) {
                isSearchingAddress = false;
                CepResponse res = response.body();

                if(response.isSuccessful() && response.body() != null && !response.body().isErro()){
                    etPublicSpace.setText(res.getLogradouro());
                    etCity.setText(res.getLocalidade());
                    etState.setText(res.getUf());
                    etNeighborhood.setText(res.getBairro());
                    analyzeAddress(res);

                } else {
                    clearData();
                    blockAll(isSearchingAddress);
                    Toast.makeText(FinalRegisterActivity.this, "CEP INVÁLIDO", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CepResponse> call, Throwable t) {
                isSearchingAddress = false;
                clearData();
                blockAll(isSearchingAddress);
                Toast.makeText(FinalRegisterActivity.this, "ERRO AO BUSCAR CEP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void analyzeAddress(CepResponse address){
        if(address.getLogradouro().isEmpty() && address.getBairro().isEmpty() || address.getLogradouro() == null && address.getBairro() == null){
            blockData();
        } else {
            blockAll(isSearchingAddress);
        }
    }

    private void clearData(){
        etPublicSpace.setText("");
        etCity.setText("");
        etNeighborhood.setText("");
        etState.setText("");

    }
    //função utilizada para bloquear desbloquear somente a rua e o bairro,
    //permitindo que zonas rurais nas quais essas informações não forem encontradas possa ser editadas
    public void blockData(){
        etPublicSpace.setEnabled(true);
        etNeighborhood.setEnabled(true);
        etCity.setEnabled(false);
        etState.setEnabled(false);
    }
    public void blockAll(boolean block){
        etPublicSpace.setEnabled(block);
        etCity.setEnabled(block);
        etNeighborhood.setEnabled(block);
        etState.setEnabled(block);
    }

    private void loadingAddress(){

        etPublicSpace.setText("Buscando...");
        etCity.setText("Buscando..");
        etNeighborhood.setText("Buscando...");
        etState.setText("...");

    }

    public AddressRequest buildAddressRequest(){
        return new AddressRequest(etCep.getText().toString(),
                etPublicSpace.getText().toString(),
                etNeighborhood.getText().toString(),
                etCity.getText().toString(),
                etState.getText().toString());
    }

    public void sendAddress(AddressRequest request){
        AddressApi api = RetrofitClient
                .getClient(new SessionManager(this))
                .create(AddressApi.class);

        api.saveAddress(request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                            FinalRegisterActivity.this,
                            "ERRO AO SALVAR O ENDEREÇO",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(
                        FinalRegisterActivity.this,
                        "FALHA DE CONEXÇAO",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

    }

    public void sendFamilyContacts(UUID patientId,List<FamilyRequest> requests){
        FamilyApi api = RetrofitClient
                .getClient(new SessionManager(this))
                .create(FamilyApi.class);

        api.createFamilyContacts(patientId, requests)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(FinalRegisterActivity.this,
                                    "Erro ao cadastrar contatos familiares",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(FinalRegisterActivity.this,
                                "Falha de conexão ao enviar contatos familiares",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public boolean finishProcess(){

        List<FamilyRequest> currentList = familyViewModel.getShippingList();

        if(currentList.isEmpty()){
            Toast.makeText(this, "ADICIONE AO MENOS UM CONTATO DE EMERGÊNCIA", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(selectStrokeType == null){
            Toast.makeText(this, "SELECIONE O TIPO DE AVC", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }


}
