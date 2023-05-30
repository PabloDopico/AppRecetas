package com.example.apprecetas.Recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.Fragments.HomeFragment;
import com.example.apprecetas.R;

import java.util.List;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> listaRecetas;
    private OnRecipeClickListener onRecipeClickListener;

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.listaRecetas = recipeList;
        this.onRecipeClickListener = listener;
    }
    public void añadirRecetas(List<Recipe> recetas) {
        listaRecetas = recetas;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        return new RecipeViewHolder(view, onRecipeClickListener);
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

    //creamos un onRecipeClick para manejar los clicks sobre la receta del recyclerview
    public interface OnRecipeClickListener {
        void onRecipeClick(int position);
    }
}










