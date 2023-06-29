package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.adapter.ClaseAdapter;
import com.example.proyecto.databinding.ActivityArquitecturaBinding;
import com.example.proyecto.model.Category;
import com.example.proyecto.preferences.SharedPreferencesManager;
import com.example.proyecto.viewmodel.CategoryViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Arquitectura extends AppCompatActivity implements ClaseAdapter.OnItemClickListener {
    ActivityArquitecturaBinding binding;
    CategoryViewModel model;

    Category  celdaCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityArquitecturaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        ClaseAdapter adapter=new ClaseAdapter(this);
        binding.recycler.setAdapter(adapter);
        ventana();
        model=new ViewModelProvider(this).get(CategoryViewModel.class);
        model.getAllNotes().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                adapter.setCategory(categories);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                model.delete(adapter.getCategoryAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Arquitectura.this, "Category delete", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.recycler);
        binding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addCategoryAndEdit(false);
            }
        });


    }
    private void addCategoryAndEdit(boolean edit){
        AlertDialog dialog=new AlertDialog.Builder(this).create();
        View view=getLayoutInflater().inflate(R.layout.add,null);
        EditText category=view.findViewById(R.id.categoryADD);
        ImageView cancel=view.findViewById(R.id.imgcancel);
        ImageView save=view.findViewById(R.id.imgsave);
        NumberPicker number=view.findViewById(R.id.numberPick);
        number.setMinValue(1);
        number.setMaxValue(10);

        if(edit){
            category.setText(celdaCategory.getCategory());
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCatgory=category.getText().toString();
                if(TextUtils.isEmpty(nombreCatgory)){
                    mensaje(category,"campo requerido");
                    return;
                }
                if(edit){
                    int var =number.getValue();
                    celdaCategory.setCategory(nombreCatgory);
                    celdaCategory.setPrioridad(var);
                    model.update(celdaCategory);
                }else{
                    int var =number.getValue();
                    Category p=new Category(nombreCatgory,getHora(),getDate(),var);
                    model.insertar(p);
                }

                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();

    }
    private String getDate(){
        Date fecha=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy/MM/yyyy");
        String fech= simpleDateFormat.format(fecha);
        return fech;
    }
    private String getHora(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String hora=sdf.format(new Date());
        return hora;
    }
    @Override
    public void OnClick(Category category) {
        Intent intent=new Intent(Arquitectura.this,Logica.class);
        intent.putExtra("categoryid",category.getCategory());
        intent.putExtra("prioridad",category.getId());
        intent.putExtra("resultado",category.getPrioridad());


        startActivity(intent);
    }

    @Override
    public void editCategory(Category category) {
        celdaCategory=category;
        addCategoryAndEdit(true);
    }
    private void mensaje(EditText a,String m){
        a.setError(m);
        a.requestFocus();
    }
    private void ventana(){
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.white));
    }


}