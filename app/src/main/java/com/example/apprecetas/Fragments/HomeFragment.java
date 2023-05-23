package com.example.apprecetas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.R;
import com.example.apprecetas.Recipe.Recipe;
import com.example.apprecetas.Recipe.RecipeAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> listaRecetas;
    private FirebaseFirestore firebaseFirestore;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaRecetas = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(listaRecetas);
        recyclerView.setAdapter(recipeAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore.collection("recetas").get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String titulo = document.getString("titulo");
                            String imagen = document.getString("imagen");
                            Recipe receta = new Recipe(titulo, imagen);
                            listaRecetas.add(receta);
                        }
                    }
                });
    }

}

