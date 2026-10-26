package com.redxela.model;

// representar producto del catalogo con stock y mercancia
public class Producto {

    // declarar identificador del producto
    private String idProducto;

    // declarar nombre del producto
    private String nombre;

    // declarar tipo de mercancia asociado
    private TipoMercancia tipo;

    // declarar existencias disponibles
    private int existencias;

    // construir producto completo
    public Producto(String idProducto, String nombre, TipoMercancia tipo, int existencias) {
        // asignar identificador
        this.idProducto = idProducto;
        // asignar nombre
        this.nombre = nombre;
        // asignar tipo
        this.tipo = tipo;
        // asignar existencias
        this.existencias = existencias;
    }

    // construir producto sin existencias iniciales
    public Producto(String idProducto, String nombre, TipoMercancia tipo) {
        // asignar identificador
        this.idProducto = idProducto;
        // asignar nombre
        this.nombre = nombre;
        // asignar tipo
        this.tipo = tipo;
        // iniciar en cero
        this.existencias = 0;
    }

    /**
     * Obtener identificador del producto.
     */
    public String obtenerIdProducto() {
        // retornar identificador
        return idProducto;
    }

    /**
     * Obtener nombre del producto.
     */
    public String obtenerNombre() {
        // retornar nombre
        return nombre;
    }

    /**
     * Obtener tipo de mercancia.
     */
    public TipoMercancia obtenerTipo() {
        // retornar tipo
        return tipo;
    }

    /**
     * Obtener existencias disponibles.
     */
    public int obtenerExistencias() {
        // retornar existencias
        return existencias;
    }

    /**
     * Asignar existencias disponibles.
     */
    public void asignarExistencias(int existencias) {
        // asignar valor
        this.existencias = existencias;
    }
}
