package com.example.proyecto.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.R;
import com.example.proyecto.databinding.ActivityInicioBinding;
import com.example.proyecto.model.Persona;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class Inicio extends AppCompatActivity {
    ActivityInicioBinding binding;
    ProgressDialog dialog;
    public static final int INTENT_GOOGLE=1;
    GoogleSignInClient gsc;
    FirebaseAuth firebase;
    FirebaseUser user;
    Dialog dialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInicioBinding.inflate(getLayoutInflater());
        firebase=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        dialog=new ProgressDialog(this);
        dialog.setTitle("Ingresando");
        getSupportActionBar().hide();
        dialogo=new Dialog(this);
        changeColor();
        loadRequest();
        mOnClick();
        binding.google1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoogle();
            }
        });
        binding.ingresar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datos();
            }
        });
    }
    private void datos(){
        dialog.setMessage("Validando Informacion");

        if(TextUtils.isEmpty(binding.email1.getText().toString())){
            mensaje(binding.email1,"email no valido");
        }else if(TextUtils.isEmpty(binding.password1.getText().toString())){
            mensaje(binding.password1,"password no valida");
        }else{
            dialog.show();
            firebase.signInWithEmailAndPassword(binding.email1.getText().toString().trim(),binding.password1.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                startActivity(new Intent(Inicio.this,Principal.class));
                                finish();
                                Toast.makeText(Inicio.this, "Binvenido", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(Inicio.this, "datos Incorrectos", Toast.LENGTH_SHORT).show();
                                buttonPersonalizado();
                            }
                        }
                    });
        }
    }
    private void changeColor(){
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.white));
    }
    private  void mOnClick(){
        binding.noCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this,RegistrarUsuario.class));
            }
        });
    }
    private void loadRequest(){
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        gsc=GoogleSignIn.getClient(this,gso);;
    }
    private void btnGoogle(){
        Intent intent =gsc.getSignInIntent();
        startActivityForResult(intent ,1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Task<GoogleSignInAccount> taskt=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account= taskt.getResult(ApiException.class);
                Credential(account);
            }catch(ApiException e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void Credential(GoogleSignInAccount account){
        AuthCredential credential=GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebase.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user= firebase.getCurrentUser();
                    Persona p=new Persona();
                    p.setUiduser(user.getUid());
                    p.setNombre(user.getDisplayName());
                    p.setEmail(user.getEmail());
                    p.setImagen(user.getPhotoUrl().toString());

                    FirebaseDatabase.getInstance().getReference("base").child(user.getUid()).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Inicio.this, "Bienvenido "+user.getDisplayName().toUpperCase(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Inicio.this,Principal.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void mensaje(EditText a,String m){
        a.setError(m);
        a.requestFocus();
    }
    private void buttonPersonalizado(){
        Button button;
        dialog.setContentView(R.layout.dialogo);
        button=dialog.findViewById(R.id.entendido);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
    public static final int TIME_INTERVAL=2000;
    public long mBackPressed;

    @Override
    public void onBackPressed() {
        if(TIME_INTERVAL+mBackPressed>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this, "presione una vez mas para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed=System.currentTimeMillis();

    }
}