package com.example.simodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Unlink_Adapter extends RecyclerView.Adapter<Unlink_Adapter.MyViewHolder> {

    // Variável para lista de nomes
    private List<String> myList;

    // Construtor
    public Unlink_Adapter(List<String> myList) {
        this.myList = myList;
    }

    // Cria o layout de cada linha
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unlink_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    // Método para exibir as informações
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = myList.get(position);
        holder.textName.setText(name);
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
