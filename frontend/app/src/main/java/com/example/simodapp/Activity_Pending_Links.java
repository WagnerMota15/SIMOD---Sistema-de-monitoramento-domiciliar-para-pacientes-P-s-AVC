package com.example.simodapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.simodapp.databinding.ActivityPendingLinksBinding;
import java.util.Arrays;
import java.util.List;



public class Activity_Pending_Links extends AppCompatActivity {

    private ActivityPendingLinksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_links);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityPendingLinksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

    }

    private void initRecyclerView() {
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setAdapter(new Pending_Links_Adapter(getList()));
    }

    private List<String> getList() {
        return Arrays.asList(
                "Francisco",
                "Raimundo",
                "Brenda",
                "Analu",
                "Luana",
                "Andrea",
                "Hugo",
                "Vitor",
                "Luana",
                "Andrea",
                "Hugo",
                "Vitor",
                "Francisco",
                "Raimundo",
                "Brenda",
                "Analu",
                "Luana",
                "Andrea"
        );
    }

}