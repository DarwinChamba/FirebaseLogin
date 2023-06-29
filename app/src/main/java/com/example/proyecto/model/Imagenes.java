package com.example.proyecto.model;

public class Imagenes {
    private int idImagen;
    private String imagenTrafico;

    public Imagenes(int idImagen, String imagenTrafico) {
        this.idImagen = idImagen;
        this.imagenTrafico = imagenTrafico;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getImagenTrafico() {
        return imagenTrafico;
    }

    public void setImagenTrafico(String imagenTrafico) {
        this.imagenTrafico = imagenTrafico;
    }
}
