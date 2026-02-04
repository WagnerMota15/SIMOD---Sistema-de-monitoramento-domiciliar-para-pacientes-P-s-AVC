package com.example.simodapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.simodapp.databinding.ActivityActiveLinksBinding;
import java.util.Arrays;
import java.util.List;



public class Activity_Active_Links extends AppCompatActivity {

    private ActivityActiveLinksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_active_links);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityActiveLinksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

    }

    private void initRecyclerView() {
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setAdapter(new Active_Links_Adapter(getList()));
    }

    private List<String> getList() {
        return Arrays.asList(
                "Francisco Victor",
                "Raimundo Rodrigo",
                "Brenda Eloá",
                "Analu Isadora",
                "Luana Tânia",
                "Andrea Vitória",
                "Hugo Sérgio",
                "Vitor Diogo",
                "Luana Tânia",
                "Andrea Vitória",
                "Hugo Sérgio",
                "Vitor Diogo",
                "Francisco Victor",
                "Raimundo Rodrigo",
                "Brenda Eloá",
                "Analu Isadora",
                "Luana Tânia",
                "Andrea Vitória"
        );
    }

}