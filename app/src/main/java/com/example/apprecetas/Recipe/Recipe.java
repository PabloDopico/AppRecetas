package com.example.apprecetas.Recipe;

import java.util.ArrayList;
import java.util.Map;

public class Recipe {
    private String imagen;
    private String titulo;
    private String descripcion;
    private ArrayList<String> ingredientes;
    private ArrayList<String> tutorial;
    private ArrayList<String> caracteristicas;
    private Map<String, String> valores_nutricionales;

    public Recipe() {
    }

    public Recipe(String titulo, String imagen) {
        this.imagen = imagen;
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ArrayList<String> getTutorial() {
        return tutorial;
    }

    public void setTutorial(ArrayList<String> tutorial) {
        this.tutorial = tutorial;
    }

    public ArrayList<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public Map<String, String> getValores_nutricionales() {
        return valores_nutricionales;
    }

    public void setValores_nutricionales(Map<String, String> valores_nutricionales) {
        this.valores_nutricionales = valores_nutricionales;
    }
}