package com.redxela.controller;

import java.util.ArrayList;
import java.util.List;

// recibir datos para crear pedido
public class PeticionPedido {

    // declarar cliente
    private String idCliente;

    // declarar nivel
    private int nivel;

    // declarar detalles
    private List<DetalleEntrada> detalles;

    // construir peticion vacia
    public PeticionPedido() {
        // crear lista vacia
        detalles = new ArrayList<>();
    }

    /**
     * Obtener cliente.
     */
    public String obtenerIdCliente() {
        // retornar cliente
        return idCliente;
    }

    /**
     * Asignar cliente.
     */
    public void asignarIdCliente(String idCliente) {
        // asignar valor
        this.idCliente = idCliente;
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

    /**
     * Obtener detalles.
     */
    public List<DetalleEntrada> obtenerDetalles() {
        // retornar detalles
        return detalles;
    }

    /**
     * Asignar detalles.
     */
    public void asignarDetalles(List<DetalleEntrada> detalles) {
        // asignar valor
        this.detalles = detalles;
    }
}
