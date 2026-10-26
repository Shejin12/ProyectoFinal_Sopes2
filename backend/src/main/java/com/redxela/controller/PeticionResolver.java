package com.redxela.controller;

// recibir operacion ganadora para resolver deadlock
public class PeticionResolver {

    // declarar ganadora
    private String ganadora;

    // construir peticion vacia
    public PeticionResolver() {
    }

    /**
     * Obtener ganadora.
     */
    public String obtenerGanadora() {
        // retornar ganadora
        return ganadora;
    }

    /**
     * Asignar ganadora.
     */
    public void asignarGanadora(String ganadora) {
        // asignar valor
        this.ganadora = ganadora;
    }
}
