package com.example.apprecetas.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {
    private Button cerrarSesionButton;
    private Button borrarCuentaButton;
    private FirebaseAuth firebaseAuth;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        Button cerrarSesionButton = view.findViewById(R.id.cerrarSesion);

        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        Button borrarCuentaButton = view.findViewById(R.id.borrarCuenta);


        borrarCuentaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarCuenta();
            }
        });

        return view;
    }

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Estás segur@ de que deseas cerrar sesión?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            firebaseAuth.signOut();

        //guardamos el estado en las sharedpreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("estaLogeado", false);
        editor.apply();
        // al cerrar sesión volvemos a la pantalla de login o registro
        Intent intent = new Intent(getActivity(), Login_Or_Register.class);
        startActivity(intent);
        getActivity().finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show();
    }

    private void borrarCuenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Borrar cuenta");
        builder.setMessage("¿Estás segur@?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            AlertDialog.Builder contraseñaDialogBuilder = new AlertDialog.Builder(getActivity());
            contraseñaDialogBuilder.setTitle("Introduce la contraseña");

            EditText contraseñaEditText = new EditText(getActivity());
            contraseñaEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            contraseñaDialogBuilder.setView(contraseñaEditText);

            contraseñaDialogBuilder.setPositiveButton("Eliminar", (dialogContraseña, contraseña1) -> {
                String contraseña = contraseñaEditText.getText().toString();

                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(usuario.getEmail(), contraseña);
                    usuario.reauthenticate(credential).addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    usuario.delete().addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Toast.makeText(getActivity(), "El usuario se ha borrado correctamente", Toast.LENGTH_SHORT).show();

                                            // Guardamos el estado en las SharedPreferences
                                            SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("estaLogeado", false);
                                            editor.apply();

                                            // Al cerrar sesión volvemos a la pantalla de login o registro
                                            Intent intent = new Intent(getActivity(), Login_Or_Register.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getActivity(), "Ha ocurrido un error al borrar la cuenta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            contraseñaDialogBuilder.setNegativeButton("Cancelar", (dialogContraseña, contraseña1) -> {
            });
            contraseñaDialogBuilder.show();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show();
    }

}