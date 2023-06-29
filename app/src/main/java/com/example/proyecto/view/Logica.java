package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityLogicaBinding;
import com.example.proyecto.model.Imagenes;
import com.example.proyecto.preferences.SharedPreferencesManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Logica extends AppCompatActivity {
ActivityLogicaBinding binding;
FirebaseAuth firebase;
FirebaseStorage storage;
FirebaseDatabase database;
Intent extraI;
int id;
String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLogicaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        ventana();
        firebase=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        //iniciailizar la claseShared Preferences
        SharedPreferencesManager.init(this);
         extraI=getIntent();
          id=extraI.getIntExtra("prioridad",1);
         uid=String.valueOf(id);

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Logica.this,Arquitectura.class));
            }
        });
        //obtiene informacion de la categoria y el id
        getExtrasA();
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferencesManager.saveString(uid,binding.description.getText().toString());
                startActivity(new Intent(Logica.this,Arquitectura.class));
                Toast.makeText(Logica.this, "registro guardado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        binding.subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarImagenes();
            }
        });
        cargarPreferences();

        obtenerImagenes();
    }
    //recibe la categoria y el id de la actividad anterior
    private void getExtrasA(){

        binding.categoryName.setText(extraI.getStringExtra("categoryid"));
        binding.prioridad.setText(""+extraI.getIntExtra("resultado",1));


    }
    private void cargarPreferences(){

        if(Integer.parseInt(uid)==id){
            String selectedItemInfo = SharedPreferencesManager.getString(uid, "");
            binding.description.setText(selectedItemInfo);
        }


    }
    private void obtenerImagenes(){
        database.getReference().child("misimagenes").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String funciona=snapshot.child("imagenTrafico").getValue().toString();
                    Glide.with(Logica.this).load(funciona).into(binding.imagen3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void  cambiarImagenes(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,12);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            Uri mFile=data.getData();
            binding.imagen3.setImageURI(mFile);
            final StorageReference reference=storage.getReference().child("imagenes_logica")
                    .child(firebase.getUid());

            reference.putFile(mFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString(); // Obtener la URL de almacenamiento
                            Imagenes img=new Imagenes(id,imageUrl);
                            database.getReference().child("misimagenes").child(uid).setValue(img);
                            Glide.with(Logica.this).load(imageUrl).into(binding.imagen3);
                        }
                    });
                }
            });
        }
    }

    private void ventana(){
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.colorLogica));
    }


}