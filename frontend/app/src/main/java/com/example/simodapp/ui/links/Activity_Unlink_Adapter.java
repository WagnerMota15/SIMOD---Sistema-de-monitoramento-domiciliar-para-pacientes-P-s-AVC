package com.example.simodapp.ui.links;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.simodapp.R;
import com.example.simodapp.databinding.ActivityUnlinkAdapterBinding;
import java.util.Arrays;
import java.util.List;



public class Activity_Unlink_Adapter extends AppCompatActivity {

    private ActivityUnlinkAdapterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unlink_adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityUnlinkAdapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

    }

    private void initRecyclerView() {
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setAdapter(new Unlink_Adapter(getList()));
    }

    private List<String> getList() {
        return Arrays.asList(
                "Francisco Victor Ferreira",
                "Raimundo Rodrigo Barros",
                "Brenda Eloá Hadassa da Costa",
                "Analu Isadora Valentina Nogueira",
                "Luana Tânia Sabrina da Silva",
                "Andrea Vitória Yasmin Ferreira",
                "Hugo Sérgio Heitor Nascimento",
                "Vitor Diogo Eduardo Pereira",
                "Luana Tânia Sabrina da Silva",
                "Andrea Vitória Yasmin Ferreira",
                "Hugo Sérgio Heitor Nascimento",
                "Vitor Diogo Eduardo Pereira",
                "Francisco Victor Ferreira",
                "Raimundo Rodrigo Barros",
                "Brenda Eloá Hadassa da Costa",
                "Analu Isadora Valentina Nogueira",
                "Luana Tânia Sabrina da Silva",
                "Andrea Vitória Yasmin Ferreira"
        );
    }

}