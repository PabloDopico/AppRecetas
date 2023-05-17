package com.example.apprecetas.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
import com.example.apprecetas.Register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView registerTextView = findViewById(R.id.register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.usuarioEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.contraseña)).getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Por favor introduce el correo electronico", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Por favor introduce la contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, Login_Or_Register.class);
                                    startActivity(intent);
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();}
                            }
                        });
            }
        });





    }





}