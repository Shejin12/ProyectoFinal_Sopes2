package com.redxela.model;

// representar linea de pedido con producto y cantidad
public class DetallePedido {

    // declarar identificador de producto
    private String idProducto;

    // declarar cantidad solicitada
    private int cantidad;

    // construir detalle completo
    public DetallePedido(String idProducto, int cantidad) {
        // asignar producto
        this.idProducto = idProducto;
        // asignar cantidad
        this.cantidad = cantidad;
    }

    /**
     * Obtener identificador de producto.
     */
    public String obtenerIdProducto() {
        // retornar identificador
        return idProducto;
    }

    /**
     * Obtener cantidad solicitada.
     */
    public int obtenerCantidad() {
        // retornar cantidad
        return cantidad;
    }

    /**
     * Asignar cantidad solicitada.
     */
    public void asignarCantidad(int cantidad) {
        // asignar valor
        this.cantidad = cantidad;
    }
}
