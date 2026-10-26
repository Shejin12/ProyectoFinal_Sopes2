package com.redxela.controller;

// recibir cambio de politica
public class PeticionPolitica {

    // declarar politica
    private String politica;

    // construir peticion vacia
    public PeticionPolitica() {
    }

    /**
     * Obtener politica.
     */
    public String obtenerPolitica() {
        // retornar politica
        return politica;
    }

    /**
     * Asignar politica.
     */
    public void asignarPolitica(String politica) {
        // asignar valor
        this.politica = politica;
    }
}
