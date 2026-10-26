package com.redxela.model;

// representar aviso de llegada de mercaderia para cola bloqueante
public class AvisoLlegada {

    // declarar identificador de producto
    private String idProducto;

    // declarar cantidad recibida
    private int cantidad;

    // declarar marca de registro
    private long registradoEn;

    // construir aviso completo
    public AvisoLlegada(String idProducto, int cantidad) {
        // asignar producto
        this.idProducto = idProducto;
        // asignar cantidad
        this.cantidad = cantidad;
        // registrar marca
        this.registradoEn = System.currentTimeMillis();
    }

    // construir aviso vacio para serializacion
    public AvisoLlegada() {
        // iniciar vacio
        this.idProducto = "";
        this.cantidad = 0;
        this.registradoEn = System.currentTimeMillis();
    }

    /**
     * Obtener identificador de producto.
     */
    public String obtenerIdProducto() {
        // retornar identificador
        return idProducto;
    }

    /**
     * Obtener cantidad recibida.
     */
    public int obtenerCantidad() {
        // retornar cantidad
        return cantidad;
    }

    /**
     * Obtener marca de registro.
     */
    public long obtenerRegistradoEn() {
        // retornar marca
        return registradoEn;
    }
}
