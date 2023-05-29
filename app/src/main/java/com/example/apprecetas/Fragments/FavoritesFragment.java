package com.example.apprecetas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.R;
import com.example.apprecetas.Recipe.Recipe;
import com.example.apprecetas.Recipe.RecipeActivity;
import com.example.apprecetas.Recipe.RecipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoritesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> listaRecetas;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private SharedPreferences sharedPref;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaRecetas = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(listaRecetas, this);
        recyclerView.setAdapter(recipeAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPref = getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        //guardamos en la variable idUsuario la id del usuario de firebase
        String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //obtenemos todas las recetas de Firestore
        firebaseFirestore.collection("recetas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaRecetas.clear();

                // recorremos los documentos y verificamos si cada receta está marcada como favorita
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String titulo = document.getString("titulo");
                    String imagen = document.getString("imagen");

                    //comprobamos si la receta esta guardada como favorita en las sharedpreferences
                    boolean esFavorito = comprobarFavorita(titulo, idUsuario);
                    if (esFavorito) {
                        Recipe receta = new Recipe(titulo, imagen);
                        listaRecetas.add(receta);
                    }
                }
                recipeAdapter.notifyDataSetChanged();
            }
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });
    }

    private boolean comprobarFavorita(String titulo, String idUsuario) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("preferencias_" +idUsuario, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(titulo, false);
    }
    //metodo para gestionar los clicks en las recetas
    @Override
    public void onRecipeClick(int position) {
        //obtenemos la receta en la posicion clickada
        Recipe receta = listaRecetas.get(position);
        String tituloReceta = receta.getTitulo();

        // obtenemos y mandamos el titulo de la receta a la clase RecipeActivity
        Intent intent = new Intent(getContext(), RecipeActivity.class);
        intent.putExtra("titulo", tituloReceta);
        startActivity(intent);
    }
}
