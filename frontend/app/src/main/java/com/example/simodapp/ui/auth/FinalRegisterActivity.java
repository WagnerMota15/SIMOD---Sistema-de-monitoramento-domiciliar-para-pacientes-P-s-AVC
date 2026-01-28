package com.example.simodapp.ui.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.domain.enums.Kinship;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.ui.paciente.adapter.FamilyAdapter;
import com.example.simodapp.viewmodel.FamilyViewModel;
import com.example.simodapp.viewmodel.PatientViewModel;
import com.example.simodapp.viewmodel.RegisterViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class FinalRegisterActivity extends AppCompatActivity {

    private FamilyViewModel familyViewModel;
    private FamilyAdapter familyAdapter;

    private PatientViewModel patientViewModel;

    private MaterialAutoCompleteTextView actStrokeType;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_cadastro_paciente);

        familyViewModel = new ViewModelProvider(this).get(FamilyViewModel.class);

        patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);

        actStrokeType = findViewById(R.id.actStrokeType);

        setupStrokeDropdown();
        setupFamilySection();

        findViewById(R.id.btnAddContato).setOnClickListener(v -> showDialogAdd());
        findViewById(R.id.actStrokeType).setOnClickListener(v -> actStrokeType.showDropDown());
        findViewById(R.id.btnFinalizar).setOnClickListener(v -> finishProcess());



    }

    private void setupStrokeDropdown() {
        ArrayAdapter<StrokeTypes> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        StrokeTypes.values()
                );

        actStrokeType.setAdapter(adapter);

        actStrokeType.setOnItemClickListener((parent, view, position, id) -> {
            StrokeTypes selected = (StrokeTypes) parent.getItemAtPosition(position);
            patientViewModel.setStrokeType(selected);
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


    public void finishProcess(){

        List<FamilyRequest> currentList = familyViewModel.getShippingList();
        StrokeTypes strokeTypes = patientViewModel.getStrokeType();

        if(currentList.isEmpty()){
            Toast.makeText(this, "ADICIONE AO MENOS UM CONTATO DE EMERGÊNCIA", Toast.LENGTH_SHORT).show();
            return;
        }

        if(patientViewModel.getStrokeType() == null){
            Toast.makeText(this, "SELECIONE O TIPO DE AVC", Toast.LENGTH_SHORT).show();
            return;
        }


    }


}
