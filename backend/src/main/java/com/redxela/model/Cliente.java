package com.redxela.model;

// representar cliente con cuenta y nivel de servicio
public class Cliente {

    // declarar identificador del cliente
    private String idCliente;

    // declarar nombre del cliente
    private String nombre;

    // declarar nivel de servicio contratado
    private NivelServicio nivel;

    // construir cliente con datos basicos
    public Cliente(String idCliente, String nombre, NivelServicio nivel) {
        // asignar identificador
        this.idCliente = idCliente;
        // asignar nombre
        this.nombre = nombre;
        // asignar nivel
        this.nivel = nivel;
    }

    // construir cliente sin nivel con nivel estandar por defecto
    public Cliente(String idCliente, String nombre) {
        // asignar identificador
        this.idCliente = idCliente;
        // asignar nombre
        this.nombre = nombre;
        // asignar nivel por defecto
        this.nivel = NivelServicio.NIVEL_3;
    }

    /**
     * Obtener identificador del cliente.
     */
    public String obtenerIdCliente() {
        // retornar identificador
        return idCliente;
    }

    /**
     * Asignar identificador del cliente.
     */
    public void asignarIdCliente(String idCliente) {
        // asignar valor
        this.idCliente = idCliente;
    }

    /**
     * Obtener nombre del cliente.
     */
    public String obtenerNombre() {
        // retornar nombre
        return nombre;
    }

    /**
     * Asignar nombre del cliente.
     */
    public void asignarNombre(String nombre) {
        // asignar valor
        this.nombre = nombre;
    }

    /**
     * Obtener nivel de servicio.
     */
    public NivelServicio obtenerNivel() {
        // retornar nivel
        return nivel;
    }

    /**
     * Asignar nivel de servicio.
     */
    public void asignarNivel(NivelServicio nivel) {
        // asignar valor
        this.nivel = nivel;
    }
}
