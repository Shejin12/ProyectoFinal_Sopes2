package com.redxela.model;

// representar resumen de estadisticas del sistema
public class ResumenEstadisticas {

    // declarar total completados
    private int completados;

    // declarar espera promedio en segundos
    private double esperaPromedioSeg;

    // declarar proceso promedio en segundos
    private double procesoPromedioSeg;

    // declarar total en espera
    private int enEspera;

    // declarar total en proceso
    private int enProceso;

    // declarar total bloqueados
    private int bloqueados;

    // declarar politica actual
    private String politicaActual;

    // declarar estado del generador
    private boolean generadorActivo;

    // construir resumen vacio
    public ResumenEstadisticas() {
        // iniciar en cero
        this.completados = 0;
        this.esperaPromedioSeg = 0;
        this.procesoPromedioSeg = 0;
        this.enEspera = 0;
        this.enProceso = 0;
        this.bloqueados = 0;
        this.politicaActual = "PRIORIDAD_ESTRICTA";
        this.generadorActivo = true;
    }

    /**
     * Obtener total completados.
     */
    public int obtenerCompletados() {
        // retornar valor
        return completados;
    }

    /**
     * Asignar total completados.
     */
    public void asignarCompletados(int completados) {
        // asignar valor
        this.completados = completados;
    }

    /**
     * Obtener espera promedio.
     */
    public double obtenerEsperaPromedioSeg() {
        // retornar valor
        return esperaPromedioSeg;
    }

    /**
     * Asignar espera promedio.
     */
    public void asignarEsperaPromedioSeg(double esperaPromedioSeg) {
        // asignar valor
        this.esperaPromedioSeg = esperaPromedioSeg;
    }

    /**
     * Obtener proceso promedio.
     */
    public double obtenerProcesoPromedioSeg() {
        // retornar valor
        return procesoPromedioSeg;
    }

    /**
     * Asignar proceso promedio.
     */
    public void asignarProcesoPromedioSeg(double procesoPromedioSeg) {
        // asignar valor
        this.procesoPromedioSeg = procesoPromedioSeg;
    }

    /**
     * Obtener total en espera.
     */
    public int obtenerEnEspera() {
        // retornar valor
        return enEspera;
    }

    /**
     * Asignar total en espera.
     */
    public void asignarEnEspera(int enEspera) {
        // asignar valor
        this.enEspera = enEspera;
    }

    /**
     * Obtener total en proceso.
     */
    public int obtenerEnProceso() {
        // retornar valor
        return enProceso;
    }

    /**
     * Asignar total en proceso.
     */
    public void asignarEnProceso(int enProceso) {
        // asignar valor
        this.enProceso = enProceso;
    }

    /**
     * Obtener total bloqueados.
     */
    public int obtenerBloqueados() {
        // retornar valor
        return bloqueados;
    }

    /**
     * Asignar total bloqueados.
     */
    public void asignarBloqueados(int bloqueados) {
        // asignar valor
        this.bloqueados = bloqueados;
    }

    /**
     * Obtener politica actual.
     */
    public String obtenerPoliticaActual() {
        // retornar valor
        return politicaActual;
    }

    /**
     * Asignar politica actual.
     */
    public void asignarPoliticaActual(String politicaActual) {
        // asignar valor
        this.politicaActual = politicaActual;
    }

    /**
     * Verificar si generador esta activo.
     */
    public boolean estaGeneradorActivo() {
        // retornar bandera
        return generadorActivo;
    }

    /**
     * Asignar estado del generador.
     */
    public void asignarGeneradorActivo(boolean generadorActivo) {
        // asignar valor
        this.generadorActivo = generadorActivo;
    }
}
