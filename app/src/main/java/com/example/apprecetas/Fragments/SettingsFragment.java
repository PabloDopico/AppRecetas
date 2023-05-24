package com.example.apprecetas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
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
    }

    private void borrarCuenta() {
        FirebaseUser usuario = firebaseAuth.getCurrentUser();

        if (usuario != null) {
            usuario.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "El usuario se ha borrado correctamente", Toast.LENGTH_SHORT).show();

                            //guardamos el estado en las sharedpreferences
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("estaLogeado", false);
                            editor.apply();

                            // al cerrar sesión volvemos a la pantalla de login o registro
                            Intent intent = new Intent(getActivity(), Login_Or_Register.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al borrar la cuenta" + usuario, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}