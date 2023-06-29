package com.example.proyecto.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.R;
import com.example.proyecto.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ClaseAdapter extends RecyclerView.Adapter<ClaseAdapter.ViewHolder> {
    List<Category> lista_category=new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new ViewHolder(view);
    }
    public ClaseAdapter(OnItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category c=lista_category.get(position);
        holder.category.setText(c.getCategory());
        holder.fecha.setText(c.getFecha());
        holder.hora.setText(c.getHora());
        holder.prioridad.setText(String.valueOf(c.getPrioridad()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(lista_category.get(position));
            }
        });
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editCategory(lista_category.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista_category.size();
    }
    public void setCategory(List<Category> lista_category){
        this.lista_category=lista_category;
        notifyDataSetChanged();
    }

    public Category getCategoryAt(int position){
        return lista_category.get(position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView category,hora,fecha,description,prioridad;
            ImageView editar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.category);
            hora=itemView.findViewById(R.id.hora);
            fecha=itemView.findViewById(R.id.fecha);
            prioridad=itemView.findViewById(R.id.prioridad);
            editar=itemView.findViewById(R.id.imgedit);

        }
    }
    public interface OnItemClickListener{
        void OnClick(Category category);
        void editCategory(Category category);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
