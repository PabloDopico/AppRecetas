package com.example.apprecetas.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.MainActivity;
import com.example.apprecetas.R;
import com.example.apprecetas.Register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private SignInButton loginGoogle;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean estaLogeado = sharedPref.getBoolean("estaLogeado", false);

        if (estaLogeado) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        emailEditText = findViewById(R.id.usuarioEmail);
        passwordEditText = findViewById(R.id.contraseña);
        loginButton = findViewById(R.id.login);
        registerTextView = findViewById(R.id.register);
        loginGoogle = findViewById(R.id.loginGoogle);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginConGoogle();
            }
        });
    }

    private void loginUser() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Por favor introduce el correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Por favor introduce la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();

                            //guardamos si esta logeado en las sharedpreferences
                            SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("estaLogeado", true);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginConGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        Intent loginIntent = GoogleSignIn.getClient(this, googleSignInOptions).getSignInIntent();
        startActivityForResult(loginIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(cuenta.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "No se ha podido iniciar  sesion con Google. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //guardamos si esta logeado en las sharedpreferences
                            SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("estaLogeado", true);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "Se ha iniciado sesion con Google", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "No se ha podido iniciar  sesion con Google. Error: "  + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void restablecerContraseña(View view) {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Por favor introduce el correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Se ha enviado un mensaje a tu correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Ha ocurrido un error al restablecer la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
