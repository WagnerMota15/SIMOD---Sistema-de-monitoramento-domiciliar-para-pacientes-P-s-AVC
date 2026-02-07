package com.example.simodapp.ui.common;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;

public class PlaceholderActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title == null || title.trim().isEmpty()) title = "Tela em construção";

        TextView tv = findViewById(R.id.txtPlaceholderTitle);
        if (tv != null) tv.setText(title);
    }
}
