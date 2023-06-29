package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.proyecto.adapter.ClaseAdapter;
import com.example.proyecto.model.Category;
import com.example.proyecto.view.Inicio;
import com.example.proyecto.view.Principal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {
    FirebaseAuth firebase;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase=FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        ventana();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               comprobarInicio();
            }
        },1500);

    }
    private void comprobarInicio(){
        user=firebase.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(MainActivity.this,Principal.class));
            finish();
        }else{
            startActivity(new Intent(MainActivity.this,Inicio.class));
            finish();
        }
    }
    private void ventana(){
        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getColor(R.color.colorInicio));
    }

}