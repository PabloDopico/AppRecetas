package com.example.apprecetas.Register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombreUsuarioEditText;
    private EditText emailEditText;
    private EditText contraseñaEditText;
    private EditText confirmarContraseñaEditText;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nombreUsuarioEditText = findViewById(R.id.nombreUsuario);
        emailEditText = findViewById(R.id.email);
        contraseñaEditText = findViewById(R.id.contraseña);
        confirmarContraseñaEditText = findViewById(R.id.confirmarContraseña);
        progressBar = findViewById(R.id.progressbar);

        Button registrarseButton = findViewById(R.id.register);
        registrarseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarUsuario();
            }
        });

        TextView textoIniciarSesion = findViewById(R.id.iniciarSesion);

        textoIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registrarUsuario() {

        String nombreUsuario = nombreUsuarioEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String contraseña = contraseñaEditText.getText().toString().trim();
        String confirmarContraseña = confirmarContraseñaEditText.getText().toString().trim();


        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(nombreUsuario)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce el nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce el email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contraseña)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce la confirmación de contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(getApplicationContext(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
            return;
        }

        // llamamos al metodo que comprueba la contraseña para asegurarnos que la contraseña cumple con los requisitos necesarios
        if (!comprobarContraseña(contraseña)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Se ha creado el usuario "+nombreUsuario+" correctamente", Toast.LENGTH_SHORT).show();

                            // Si se crea el usuario correctamente
                            FirebaseUser usuario = mAuth.getCurrentUser();
                            // Cambiamos el perfil del usuario y añadimos su  username
                            UserProfileChangeRequest modificarPerfil = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nombreUsuario)
                                    .build();
                            usuario.updateProfile(modificarPerfil);
                            // Al registrar al usuario volvemos a la pantalla principal (modificar por el HomeActivity cuando este creado)

                                    Intent intent = new Intent(RegisterActivity.this, Login_Or_Register.class);
                            startActivity(intent);
                        } else {
                            // Si hay error en la creacion del usuario mostramos mensaje de error
                            Toast.makeText(getApplicationContext(), "Se ha producido un error en la creación del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

    }

    private boolean comprobarContraseña(String contraseña) {
        if (contraseña.length() < 6) {
            Toast.makeText(getApplicationContext(), "La contraseña debe tener como minimo 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean tieneNumero = false;
        boolean tieneMayus = false;

        for (int i = 0; i < contraseña.length(); i++) {
            char c = contraseña.charAt(i);
            if (Character.isDigit(c)) {
                tieneNumero = true;
            } else if (Character.isUpperCase(c)) {
                tieneMayus = true;
            }

            if (tieneNumero && tieneMayus) {
                break;
            }
        }

        if (!tieneNumero) {
            Toast.makeText(getApplicationContext(), "La contraseña debe contener al menos un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!tieneMayus) {
            Toast.makeText(getApplicationContext(), "La contraseña debe contener al menos una letra mayúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
