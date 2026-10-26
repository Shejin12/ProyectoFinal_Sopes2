package com.redxela.controller;

// recibir datos para crear cliente
public class PeticionCliente {

    // declarar nombre
    private String nombre;

    // declarar nivel
    private int nivel;

    // construir peticion vacia
    public PeticionCliente() {
    }

    /**
     * Obtener nombre.
     */
    public String obtenerNombre() {
        // retornar nombre
        return nombre;
    }

    /**
     * Asignar nombre.
     */
    public void asignarNombre(String nombre) {
        // asignar valor
        this.nombre = nombre;
    }

    /**
     * Obtener nivel.
     */
    public int obtenerNivel() {
        // retornar nivel
        return nivel;
    }

    /**
     * Asignar nivel.
     */
    public void asignarNivel(int nivel) {
        // asignar valor
        this.nivel = nivel;
    }
}
