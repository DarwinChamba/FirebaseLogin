package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyecto.MainActivity;
import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityPrincipalBinding;
import com.example.proyecto.model.Persona;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Principal extends AppCompatActivity {
    ActivityPrincipalBinding binding;
    FirebaseAuth firebase;
    FirebaseUser user;
    GoogleSignInClient gsc;
    FirebaseDatabase database;
    FirebaseStorage storage;
    GoogleSignInAccount gsa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebase=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        createRequest();
        botonSalir();
       getDatosImagen();
       ventana();
       getDatos();
        binding.horaFecha.setText(getHora()+"    "+getDate());
        cuentaEliminada();
        acercaDeMetodo();
        binding.cambiarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarImagenes();
            }
        });
        binding.controlTrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this,Arquitectura.class));
                finish();
            }
        });

    }
    private void getDatosGoogle(){
        gsa= GoogleSignIn.getLastSignedInAccount(this);
        if(gsa!=null){
            Uri uri= gsa.getPhotoUrl();
            Glide.with(this).load(uri).into(binding.imagen);
            String nombre=gsa.getDisplayName();
            binding.nombrePortada.setText(nombre.toUpperCase());
            binding.emailPortada.setText(gsa.getEmail());
        }
    }
    private void getDatos(){
        user= firebase.getCurrentUser();
        database.getReference().child("base").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String nombre=snapshot.child("nombre").getValue().toString();
                    binding.nombrePortada.setText(nombre.toUpperCase());
                    String email=snapshot.child("email").getValue().toString();
                    binding.emailPortada.setText(email);
                    String imagen=snapshot.child("imagen").getValue().toString();

/*
                    if (imagen!=null) {
                        Glide.with(Principal.this).load(imagen).placeholder(R.drawable
                                .usuariosperfil).error(R.drawable.usuariosperfil).into(binding.imagen);
                    }


 */

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
            binding.imagen.setImageURI(mFile);
            final StorageReference reference=storage.getReference().child("imagenes_pick")
                    .child(firebase.getUid());

            reference.putFile(mFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            user=firebase.getCurrentUser();
                            String imageUrl = uri.toString(); // Obtener la URL de almacenamiento
                            database.getReference().child("base").child(firebase.getUid())
                                    .child("imagen").setValue(imageUrl); // Guardar la URL de almacenamiento en la base de datos

                        }
                    });
                }
            });
        }
    }
    private  void getDatosImagen(){
        StorageReference ref = storage.getReference().child("imagenes_pick").child(firebase.getUid());

        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // La imagen se descargó exitosamente en bytes[]
                // Puedes convertir los bytes en un objeto Bitmap u otro formato de imagen según tus necesidades
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Establece el objeto Bitmap en el ImageView
                binding.imagen.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al descargar la imagen
                // Maneja el error de acuerdo a tus necesidades
                //Toast.makeText(Principal.this, "error en la imagen", Toast.LENGTH_SHORT).show();
            }
        });

    }





    private void cuentaEliminada(){
        binding.eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Principal.this);
                builder.setTitle("Eliminar Cuenta").setIcon(R.drawable.baseline_delete_forever_24)
                        .setMessage("¿Estas seguro que deseas eliminar la cuenta?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminaCuenta();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }
        });
    }
    private void botonSalir(){
        binding.salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this,Inicio.class));
                finish();

            }
        });

    }
    private  void eliminaCuenta(){
        user=firebase.getCurrentUser();
        gsa=GoogleSignIn.getLastSignedInAccount(Principal.this);
        if(gsa!=null){
            AuthCredential credential= GoogleAuthProvider.getCredential(gsa.getIdToken(),null);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Principal.this, "cuenta eliminada", Toast.LENGTH_SHORT).show();
                                    signOut();
                                }
                            }
                        });
                    }

                }
            });
        }
    }
    private  void signOut(){

        firebase.signOut();
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Principal.this,Inicio.class));
                    finish();
                }
            }
        });
    }
    private void createRequest(){
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc=GoogleSignIn.getClient(this,gso);
    }

    private String getDate(){
        Date fecha = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
       String fechae= simpleDateFormat.format(fecha);
        return fechae;
    }

    private String getHora(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String hora=sdf.format(new Date());
        return hora;
    }
    private void acercaDeMetodo(){
        binding.acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus plus= DialogPlus.newDialog(Principal.this)
                        .setExpanded(true,1150)
                        .setContentHolder(new ViewHolder(R.layout.acercade))
                        .create();
                plus.show();

            }
        });
    }
    private void ventana(){
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.colorPrincipal));
    }




}