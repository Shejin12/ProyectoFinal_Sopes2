package com.redxela.controller;

// recibir estado del generador
public class PeticionGenerador {

    // declarar bandera
    private boolean activo;

    // construir peticion vacia
    public PeticionGenerador() {
    }

    /**
     * Verificar si activo.
     */
    public boolean estaActivo() {
        // retornar bandera
        return activo;
    }

    /**
     * Asignar estado.
     */
    public void asignarActivo(boolean activo) {
        // asignar valor
        this.activo = activo;
    }
}
