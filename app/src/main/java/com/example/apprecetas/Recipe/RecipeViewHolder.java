package com.example.apprecetas.Recipe;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.R;
import com.squareup.picasso.Picasso;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView titleTextView;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imagen);
        titleTextView = itemView.findViewById(R.id.titulo);
    }

    public void bind(Recipe recipe) {
        titleTextView.setText(recipe.getTitulo());
        Picasso.get().load(recipe.getImagen()).into(imageView);
    }
}