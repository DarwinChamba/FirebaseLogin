package com.example.proyecto.model;

public class Persona {
    private String nombre;
    private String email;
    private String password;
    private String imagen;
    private String uiduser;

    public Persona() {
    }

    public Persona(String nombre, String email, String password,String imagen) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.imagen=imagen;
    }

    public Persona(String nombre, String email, String password, String imagen, String uiduser) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.imagen = imagen;
        this.uiduser = uiduser;
    }




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUiduser() {
        return uiduser;
    }

    public void setUiduser(String uiduser) {
        this.uiduser = uiduser;
    }
}
