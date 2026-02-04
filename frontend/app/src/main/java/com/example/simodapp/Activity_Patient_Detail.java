package com.example.simodapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.simodapp.databinding.ActivityPatientDetailBinding;

public class Activity_Patient_Detail extends AppCompatActivity {

    private ActivityPatientDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPatientDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recebendo os dados
        String name = getIntent().getStringExtra("NAME");

        binding.txtName.setText(name);
    }
}