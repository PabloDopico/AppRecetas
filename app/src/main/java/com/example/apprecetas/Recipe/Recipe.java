package com.example.apprecetas.Recipe;

public class Recipe {
    private String titulo;
    private String imagen;

    public Recipe() {
    }

    public Recipe(String titulo, String imagen) {
        this.titulo = titulo;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
