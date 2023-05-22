package com.example.apprecetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Register.RegisterActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login_Or_Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_register_activity);


        // mediante las sharedpreferences obtenemos el estado del logeo
        SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean estaLogeado = sharedPref.getBoolean("estaLogeado", false);

        //si estaLogeado es true, mandamos al usuario al MainActivity
        if (estaLogeado) {
            Intent intent = new Intent(Login_Or_Register.this, MainActivity.class);
            startActivity(intent);
            finish();

            //si no esta logeado comenzamos con login_or_register
        }else{

            Button loginButton = findViewById(R.id.login);
            Button registerButton = findViewById(R.id.register);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_Or_Register.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_Or_Register.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
