package com.redxela.controller;

// recibir datos de llegada de mercaderia
public class PeticionRecepcion {

    // declarar producto
    private String idProducto;

    // declarar cantidad
    private int cantidad;

    // construir peticion vacia
    public PeticionRecepcion() {
    }

    /**
     * Obtener producto.
     */
    public String obtenerIdProducto() {
        // retornar producto
        return idProducto;
    }

    /**
     * Asignar producto.
     */
    public void asignarIdProducto(String idProducto) {
        // asignar valor
        this.idProducto = idProducto;
    }

    /**
     * Obtener cantidad.
     */
    public int obtenerCantidad() {
        // retornar cantidad
        return cantidad;
    }

    /**
     * Asignar cantidad.
     */
    public void asignarCantidad(int cantidad) {
        // asignar valor
        this.cantidad = cantidad;
    }
}
