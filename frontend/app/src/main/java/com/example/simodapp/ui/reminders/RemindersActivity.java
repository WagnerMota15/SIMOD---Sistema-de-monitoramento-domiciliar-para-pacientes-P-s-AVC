package com.example.simodapp.ui.reminders;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simodapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class RemindersActivity extends AppCompatActivity {

    private static final String PREFS = "SIMOD_REMINDERS";
    private static final String KEY_LIST = "list"; // string única com \n

    private MaterialToolbar toolbar;
    private ListView listView;
    private FloatingActionButton fab;

    private final ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_lembretes);

        toolbar = findViewById(R.id.toolbarReminders);
        listView = findViewById(R.id.listReminders);
        fab = findViewById(R.id.fabAddReminder);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        load();

        fab.setOnClickListener(v -> startAddFlow());

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String value = items.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Apagar lembrete")
                    .setMessage("Deseja apagar:\n" + value + "?")
                    .setPositiveButton("Apagar", (d, w) -> {
                        items.remove(position);
                        save();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });

        listView.setOnItemClickListener((p, v, pos, id) ->
                Toast.makeText(this, items.get(pos), Toast.LENGTH_SHORT).show()
        );
    }

    private void startAddFlow() {
        // 1) escolher hora
        TimePickerDialog tpd = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> askTextThenSave(hourOfDay, minute),
                8, 0, true
        );
        tpd.show();
    }

    private void askTextThenSave(int hour, int minute) {
        String hhmm = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        EditText et = new EditText(this);
        et.setHint("Ex: Tomar remédios");
        et.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Lembrete às " + hhmm)
                .setView(et)
                .setPositiveButton("Salvar", (d, w) -> {
                    String text = et.getText().toString().trim();
                    if (text.isEmpty()) {
                        Toast.makeText(this, "Digite uma descrição", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    items.add(hhmm + "  —  " + text);
                    sortByTime();
                    save();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void sortByTime() {
        // ordena por "HH:MM"
        Collections.sort(items, (a, b) -> {
            String ta = a.length() >= 5 ? a.substring(0, 5) : a;
            String tb = b.length() >= 5 ? b.substring(0, 5) : b;
            return ta.compareTo(tb);
        });
    }

    private void load() {
        items.clear();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String raw = prefs.getString(KEY_LIST, "");
        if (raw == null || raw.trim().isEmpty()) {
            adapter.notifyDataSetChanged();
            return;
        }
        String[] lines = raw.split("\n");
        for (String s : lines) {
            if (s != null) {
                s = s.trim();
                if (!s.isEmpty()) items.add(s);
            }
        }
        sortByTime();
        adapter.notifyDataSetChanged();
    }

    private void save() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i));
            if (i < items.size() - 1) sb.append("\n");
        }
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(KEY_LIST, sb.toString())
                .apply();
    }
}
