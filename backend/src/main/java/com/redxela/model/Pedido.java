package com.redxela.model;

import java.util.ArrayList;
import java.util.List;

// representar pedido con cliente nivel detalles y estado
public class Pedido {

    // declarar identificador unico
    private String idPedido;

    // declarar identificador de cliente
    private String idCliente;

    // declarar nivel de servicio
    private NivelServicio nivel;

    // declarar lista de detalles
    private List<DetallePedido> detalles;

    // declarar estado actual
    private EstadoPedido estado;

    // declarar marca de creacion
    private long creadoEn;

    // declarar marca de inicio de proceso
    private long iniciadoEn;

    // declarar marca de fin de proceso
    private long finalizadoEn;

    // declarar progreso de 0 a 100
    private int progreso;

    // declarar tiempo restante estimado en segundos
    private long restanteSegundos;

    // declarar recurso actual en uso
    private String recursoActual;

    // declarar motivo de bloqueo
    private String motivoBloqueo;

    // construir pedido completo
    public Pedido(String idPedido, String idCliente, NivelServicio nivel, List<DetallePedido> detalles) {
        // asignar identificador
        this.idPedido = idPedido;
        // asignar cliente
        this.idCliente = idCliente;
        // asignar nivel
        this.nivel = nivel;
        // copiar detalles
        this.detalles = new ArrayList<>(detalles);
        // iniciar en espera
        this.estado = EstadoPedido.EN_ESPERA;
        // registrar creacion
        this.creadoEn = System.currentTimeMillis();
        // iniciar marcas en cero
        this.iniciadoEn = 0;
        this.finalizadoEn = 0;
        // iniciar progreso en cero
        this.progreso = 0;
        // iniciar restante en cero
        this.restanteSegundos = 0;
        // iniciar recurso vacio
        this.recursoActual = "";
        // iniciar motivo vacio
        this.motivoBloqueo = "";
    }

    // construir pedido vacio para serializacion
    public Pedido() {
        // iniciar lista vacia
        this.detalles = new ArrayList<>();
        // iniciar en espera
        this.estado = EstadoPedido.EN_ESPERA;
        // registrar creacion
        this.creadoEn = System.currentTimeMillis();
        // iniciar recurso vacio
        this.recursoActual = "";
        // iniciar motivo vacio
        this.motivoBloqueo = "";
    }

    /**
     * Obtener identificador del pedido.
     */
    public String obtenerIdPedido() {
        // retornar identificador
        return idPedido;
    }

    /**
     * Asignar identificador del pedido.
     */
    public void asignarIdPedido(String idPedido) {
        // asignar valor
        this.idPedido = idPedido;
    }

    /**
     * Obtener identificador de cliente.
     */
    public String obtenerIdCliente() {
        // retornar cliente
        return idCliente;
    }

    /**
     * Asignar identificador de cliente.
     */
    public void asignarIdCliente(String idCliente) {
        // asignar valor
        this.idCliente = idCliente;
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

    /**
     * Obtener lista de detalles.
     */
    public List<DetallePedido> obtenerDetalles() {
        // retornar detalles
        return detalles;
    }

    /**
     * Asignar lista de detalles.
     */
    public void asignarDetalles(List<DetallePedido> detalles) {
        // copiar lista
        this.detalles = new ArrayList<>(detalles);
    }

    /**
     * Obtener estado actual.
     */
    public EstadoPedido obtenerEstado() {
        // retornar estado
        return estado;
    }

    /**
     * Asignar estado actual.
     */
    public void asignarEstado(EstadoPedido estado) {
        // asignar valor
        this.estado = estado;
    }

    /**
     * Obtener marca de creacion.
     */
    public long obtenerCreadoEn() {
        // retornar marca
        return creadoEn;
    }

    /**
     * Obtener marca de inicio.
     */
    public long obtenerIniciadoEn() {
        // retornar marca
        return iniciadoEn;
    }

    /**
     * Asignar marca de inicio.
     */
    public void asignarIniciadoEn(long iniciadoEn) {
        // asignar valor
        this.iniciadoEn = iniciadoEn;
    }

    /**
     * Obtener marca de fin.
     */
    public long obtenerFinalizadoEn() {
        // retornar marca
        return finalizadoEn;
    }

    /**
     * Asignar marca de fin.
     */
    public void asignarFinalizadoEn(long finalizadoEn) {
        // asignar valor
        this.finalizadoEn = finalizadoEn;
    }

    /**
     * Obtener progreso actual.
     */
    public int obtenerProgreso() {
        // retornar progreso
        return progreso;
    }

    /**
     * Asignar progreso actual.
     */
    public void asignarProgreso(int progreso) {
        // asignar valor
        this.progreso = progreso;
    }

    /**
     * Obtener tiempo restante estimado.
     */
    public long obtenerRestanteSegundos() {
        // retornar restante
        return restanteSegundos;
    }

    /**
     * Asignar tiempo restante estimado.
     */
    public void asignarRestanteSegundos(long restanteSegundos) {
        // asignar valor
        this.restanteSegundos = restanteSegundos;
    }

    /**
     * Obtener recurso actual en uso.
     */
    public String obtenerRecursoActual() {
        // retornar recurso
        return recursoActual;
    }

    /**
     * Asignar recurso actual en uso.
     */
    public void asignarRecursoActual(String recursoActual) {
        // asignar valor
        this.recursoActual = recursoActual;
    }

    /**
     * Obtener motivo de bloqueo.
     */
    public String obtenerMotivoBloqueo() {
        // retornar motivo
        return motivoBloqueo;
    }

    /**
     * Asignar motivo de bloqueo.
     */
    public void asignarMotivoBloqueo(String motivoBloqueo) {
        // asignar valor
        this.motivoBloqueo = motivoBloqueo;
    }

    /**
     * Calcular total de articulos del pedido.
     */
    public int obtenerTotalArticulos() {
        // iniciar acumulador
        int total;
        total = 0;
        // recorrer detalles
        for (DetallePedido detalle : detalles) {
            // sumar cantidad
            total = total + detalle.obtenerCantidad();
        }
        // retornar total
        return total;
    }
}
