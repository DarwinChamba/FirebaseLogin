package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityRegistrarUsuarioBinding;
import com.example.proyecto.model.Persona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarUsuario extends AppCompatActivity {
    ActivityRegistrarUsuarioBinding binding;
    FirebaseAuth firebase;
    FirebaseUser user;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Ingresando Informacion");
        changeColor();
        initComponents();
        tengoCuenta();
        registrarse();
    }
    private  void registrarse(){
        binding.registrate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.nombre2.getText().toString())){
                    mensaje(binding.nombre2,"campo requerido");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.email2.getText().toString().trim()).matches()){
                    mensaje(binding.email2,"email no valido");
                }else if(TextUtils.isEmpty(binding.password2.getText().toString())||binding.password2.length()<=5){
                    mensaje(binding.password2,"contraseña no valida");
                }else if (TextUtils.isEmpty(binding.confirmarPassword2.getText().toString())){
                    mensaje(binding.confirmarPassword2,"campo requerido");
                }else if(!binding.password2.getText().toString().equals(binding.confirmarPassword2.getText().toString())){
                    mensaje(binding.confirmarPassword2,"la constraseña no coincide");
                }else{
                    dialog.setMessage("espere por favor...");
                    dialog.show();
                    firebase.createUserWithEmailAndPassword(binding.email2.getText().toString().trim(),binding.password2.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dialog.dismiss();
                                    if(task.isSuccessful()){
                                        String uid =task.getResult().getUser().getUid();
                                            Persona p=new Persona(binding.nombre2.getText().toString(),binding.email2.getText().toString(),binding.password2.getText().toString(),"");

;                                            FirebaseDatabase.getInstance().getReference().child("base").child(uid).setValue(p);
                                            startActivity(new Intent(RegistrarUsuario.this,Principal.class));
                                            finish();
                                            Toast.makeText(RegistrarUsuario.this, "bienvenido", Toast.LENGTH_SHORT).show();


                                    }else{
                                        Toast.makeText(RegistrarUsuario.this, "error al registrar ", Toast.LENGTH_SHORT).show();
                                        Log.i("error","email ya registrado");
                                    }
                                }
                            });
                }
            }
        });

    }
    private  void tengoCuenta(){
        binding.tengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarUsuario.this,Inicio.class));
            }
        });
    }
private void initComponents(){
        firebase=FirebaseAuth.getInstance();

}
    private void changeColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.white));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
    private void mensaje(EditText a,String m){
        a.setError(m);
        a.requestFocus();
    }

}