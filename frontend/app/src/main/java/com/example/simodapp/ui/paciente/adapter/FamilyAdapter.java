package com.example.simodapp.ui.paciente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simodapp.R;
import com.example.simodapp.data.dto.FamilyRequest;

import java.util.ArrayList;
import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private List<FamilyRequest> listFamily = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public FamilyAdapter(OnItemClickListener listener){
        this.listener = listener;
    }

    public void addFamily(List<FamilyRequest> listFamily){
        this.listFamily = listFamily;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contato,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FamilyRequest familyRequest = listFamily.get(position);
        holder.name.setText(familyRequest.getName() + " (" + familyRequest.getKinship() + ")");
        holder.telephone.setText(familyRequest.getTelephone());

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));

    }

    @Override
    public int getItemCount() {
        return listFamily.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,telephone,kinship;
        ImageButton btnDelete;


        ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.tvNomeContato);
            telephone = view.findViewById(R.id.tvTelefoneContato);
            btnDelete = view.findViewById(R.id.btnDeletar);
        }
    }
}
