package com.redxela.model;

// representar ubicacion fisica del almacen
public class Ubicacion {

    // declarar identificador de ubicacion
    private String idUbicacion;

    // declarar pasillo
    private int pasillo;

    // declarar nivel fisico
    private int nivelFisico;

    // declarar bandera de ocupacion
    private boolean ocupada;

    // declarar producto almacenado
    private String idProducto;

    // declarar lote almacenado
    private String lote;

    // declarar cantidad almacenada
    private int cantidad;

    // construir ubicacion completa
    public Ubicacion(String idUbicacion, int pasillo, int nivelFisico) {
        // asignar identificador
        this.idUbicacion = idUbicacion;
        // asignar pasillo
        this.pasillo = pasillo;
        // asignar nivel
        this.nivelFisico = nivelFisico;
        // iniciar libre
        this.ocupada = false;
        // iniciar sin producto
        this.idProducto = "";
        // iniciar sin lote
        this.lote = "";
        // iniciar en cero
        this.cantidad = 0;
    }

    /**
     * Obtener identificador de ubicacion.
     */
    public String obtenerIdUbicacion() {
        // retornar identificador
        return idUbicacion;
    }

    /**
     * Obtener pasillo.
     */
    public int obtenerPasillo() {
        // retornar pasillo
        return pasillo;
    }

    /**
     * Obtener nivel fisico.
     */
    public int obtenerNivelFisico() {
        // retornar nivel
        return nivelFisico;
    }

    /**
     * Verificar si esta ocupada.
     */
    public boolean estaOcupada() {
        // retornar bandera
        return ocupada;
    }

    /**
     * Asignar estado de ocupacion.
     */
    public void asignarOcupada(boolean ocupada) {
        // asignar valor
        this.ocupada = ocupada;
    }

    /**
     * Obtener producto almacenado.
     */
    public String obtenerIdProducto() {
        // retornar producto
        return idProducto;
    }

    /**
     * Asignar producto almacenado.
     */
    public void asignarIdProducto(String idProducto) {
        // asignar valor
        this.idProducto = idProducto;
    }

    /**
     * Obtener lote almacenado.
     */
    public String obtenerLote() {
        // retornar lote
        return lote;
    }

    /**
     * Asignar lote almacenado.
     */
    public void asignarLote(String lote) {
        // asignar valor
        this.lote = lote;
    }

    /**
     * Obtener cantidad almacenada.
     */
    public int obtenerCantidad() {
        // retornar cantidad
        return cantidad;
    }

    /**
     * Asignar cantidad almacenada.
     */
    public void asignarCantidad(int cantidad) {
        // asignar valor
        this.cantidad = cantidad;
    }
}
