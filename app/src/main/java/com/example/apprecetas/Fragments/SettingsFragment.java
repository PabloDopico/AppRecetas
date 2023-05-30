package com.example.apprecetas.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private TextView nombreUsuario;
    private TextView correoElectronico;
    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        nombreUsuario = view.findViewById(R.id.nombreUsuario);
        correoElectronico = view.findViewById(R.id.correo);

        // mostramos el nombre de la cuenta y el correo en la pantalla al entrar en la pantalla
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        if (usuario != null) {
            String nombreUsuarioTexto = usuario.getDisplayName();
            String correoTexto = usuario.getEmail();

            if (nombreUsuarioTexto != null) {
                nombreUsuario.setText("Usuario: " + nombreUsuarioTexto);
            }

            if (correoTexto != null) {
                correoElectronico.setText("Correo: " + correoTexto);
            }
        }

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

        Button cambiarContraseña = view.findViewById(R.id.cambiarContraseña);
        cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarContraseña();
            }
        });

        ImageButton botonEditNombre = view.findViewById(R.id.botonEditNombre);
        botonEditNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarNombreUsuario();
            }
        });

        ImageButton botonEditCorreo = view.findViewById(R.id.botonEditCorreo);
        botonEditCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarCorreo();
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


    private void cambiarNombreUsuario() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Editar nombre de usuario");

                EditText editNombreDialog = new EditText(getActivity());
                editNombreDialog.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(editNombreDialog);

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nuevoNombre = editNombreDialog.getText().toString().trim();

                        FirebaseUser usuario = firebaseAuth.getCurrentUser();
                        if (usuario != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nuevoNombre)
                                    .build();

                            usuario.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void v) {
                                            nombreUsuario.setText("Usuario: " +nuevoNombre);
                                            Toast.makeText(getActivity(), "Nombre de usuario cambiado correctamente: "+nuevoNombre, Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Error al cambiar el nobmre de usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

    private void cambiarCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Editar correo electrónico");

        EditText editCorreoDialog = new EditText(getActivity());
        editCorreoDialog.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        builder.setView(editCorreoDialog);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoCorreo = editCorreoDialog.getText().toString().trim();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    currentUser.updateEmail(nuevoCorreo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    correoElectronico.setText("Correo: " +nuevoCorreo);
                                    Toast.makeText(getActivity(), "Correo electrónico cambiado correctamente: " + nuevoCorreo, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error al cambiar el correo", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void cambiarContraseña() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cambiar contraseña");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText editContraseñaActual = new EditText(getActivity());
        editContraseñaActual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editContraseñaActual.setHint("Contraseña actual");
        layout.addView(editContraseñaActual);

        EditText editNuevaContraseña = new EditText(getActivity());
        editNuevaContraseña.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editNuevaContraseña.setHint("Nueva contraseña");
        layout.addView(editNuevaContraseña);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contraseñaActual = editContraseñaActual.getText().toString().trim();
                String nuevaContraseña = editNuevaContraseña.getText().toString().trim();

                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(usuario.getEmail(), contraseñaActual);
                    usuario.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void v) {
                            usuario.updatePassword(nuevaContraseña).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    Toast.makeText(getActivity(), "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}