package com.example.simodapp.ui.emergency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_emergencia);

        MaterialToolbar toolbar = findViewById(R.id.toolbarEmergency);
        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_calendar) {
                Toast.makeText(this, "Calendário (depois a gente faz)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        MaterialButton btnCall = findViewById(R.id.btnCallSamu);
        MaterialButton btnCancel = findViewById(R.id.btnCancelEmergency);

        btnCancel.setOnClickListener(v -> finish());

        btnCall.setOnClickListener(v -> {
            // ✅ abre o discador (não precisa permissão)
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:192"));
            startActivity(i);
        });
    }
}
