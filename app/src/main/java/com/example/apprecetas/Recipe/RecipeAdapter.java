package com.example.apprecetas.Recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> listaRecetas;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.listaRecetas = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int pos) {
        Recipe receta = listaRecetas.get(pos);
        holder.bind(receta);
    }

    @Override
    public int getItemCount() {
        return listaRecetas.size();
    }
}






