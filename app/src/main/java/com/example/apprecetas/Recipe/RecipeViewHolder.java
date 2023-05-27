package com.example.apprecetas.Recipe;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecetas.R;
import com.squareup.picasso.Picasso;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titulo;
    private ImageView imagen;
    private RecipeAdapter.OnRecipeClickListener onRecipeClickListener;

    public RecipeViewHolder(@NonNull View itemView, RecipeAdapter.OnRecipeClickListener listener) {
        super(itemView);
        titulo = itemView.findViewById(R.id.titulo);
        imagen = itemView.findViewById(R.id.imagen);
        onRecipeClickListener = listener;

        itemView.setOnClickListener(this);
    }

    public void bind(Recipe receta) {
        titulo.setText(receta.getTitulo());
        Picasso.get().load(receta.getImagen()).into(imagen);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            onRecipeClickListener.onRecipeClick(position);
        }
    }
}