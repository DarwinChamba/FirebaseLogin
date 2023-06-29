package com.example.proyecto.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "proyecto")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private String hora;
    private int prioridad;
    private String fecha;
    private String description;

    public Category() {
    }

    public Category(String category, String hora, String fecha,int prioridad) {
        this.category = category;
        this.hora = hora;
        this.fecha = fecha;
        this.prioridad=prioridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
