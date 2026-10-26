package com.redxela.model;

import java.util.ArrayList;
import java.util.List;

// representar bloqueo activo entre dos operaciones
public class BloqueoActivo {

    // declarar identificador de operacion A
    private String operacionA;

    // declarar identificador de operacion B
    private String operacionB;

    // declarar recursos en disputa
    private List<RecursoTipo> recursos;

    // declarar motivo del bloqueo
    private String motivo;

    // declarar marca de deteccion
    private long detectadoEn;

    // construir bloqueo completo
    public BloqueoActivo(String operacionA, String operacionB, List<RecursoTipo> recursos, String motivo) {
        // asignar operacion A
        this.operacionA = operacionA;
        // asignar operacion B
        this.operacionB = operacionB;
        // copiar recursos
        this.recursos = new ArrayList<>(recursos);
        // asignar motivo
        this.motivo = motivo;
        // registrar marca
        this.detectadoEn = System.currentTimeMillis();
    }

    // construir bloqueo vacio para serializacion
    public BloqueoActivo() {
        // iniciar lista vacia
        this.recursos = new ArrayList<>();
        this.detectadoEn = System.currentTimeMillis();
    }

    /**
     * Obtener operacion A.
     */
    public String obtenerOperacionA() {
        // retornar operacion
        return operacionA;
    }

    /**
     * Obtener operacion B.
     */
    public String obtenerOperacionB() {
        // retornar operacion
        return operacionB;
    }

    /**
     * Obtener recursos en disputa.
     */
    public List<RecursoTipo> obtenerRecursos() {
        // retornar copia
        return new ArrayList<>(recursos);
    }

    /**
     * Obtener motivo del bloqueo.
     */
    public String obtenerMotivo() {
        // retornar motivo
        return motivo;
    }

    /**
     * Obtener marca de deteccion.
     */
    public long obtenerDetectadoEn() {
        // retornar marca
        return detectadoEn;
    }
}
