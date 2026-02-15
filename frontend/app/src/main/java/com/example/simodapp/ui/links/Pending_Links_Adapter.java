package com.example.simodapp.ui.links;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simodapp.R;

import java.util.List;

public class Pending_Links_Adapter extends RecyclerView.Adapter<Pending_Links_Adapter.MyViewHolder> {

    // Variável para lista de nomes
    private List<String> myList;

    // Construtor
    public Pending_Links_Adapter(List<String> myList) {
        this.myList = myList;
    }

    // Cria o layout de cada linha
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_links_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    // Método para exibir as informações
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = myList.get(position);
        holder.textName.setText(name);

        //quando clickar vai para a tela que mostra os dados do paciente
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Activity_Patient_Detail.class);
            intent.putExtra("NAME", name);
            v.getContext().startActivity(intent);
        });
    }

    // Retorna o tamanho da lista
    @Override
    public int getItemCount() {
        return myList.size();
    }

    // ViewHolder
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
