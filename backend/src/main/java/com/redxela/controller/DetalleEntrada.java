package com.redxela.controller;

// recibir detalle de pedido en peticion
public class DetalleEntrada {

    // declarar producto
    private String idProducto;

    // declarar cantidad
    private int cantidad;

    // construir entrada vacia
    public DetalleEntrada() {
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
