package com.redxela.model;

import java.util.ArrayList;
import java.util.List;

// representar estado actual de un recurso compartido
public class InfoRecurso {

    // declarar tipo de recurso
    private RecursoTipo tipo;

    // declarar total de unidades
    private int total;

    // declarar unidades disponibles
    private int disponibles;

    // declarar lista de duenos actuales
    private List<String> duenos;

    // construir estado completo
    public InfoRecurso(RecursoTipo tipo, int total, int disponibles, List<String> duenos) {
        // asignar tipo
        this.tipo = tipo;
        // asignar total
        this.total = total;
        // asignar disponibles
        this.disponibles = disponibles;
        // copiar duenos
        this.duenos = new ArrayList<>(duenos);
    }

    // construir estado vacio para serializacion
    public InfoRecurso() {
        // iniciar lista vacia
        this.duenos = new ArrayList<>();
    }

    /**
     * Obtener tipo de recurso.
     */
    public RecursoTipo obtenerTipo() {
        // retornar tipo
        return tipo;
    }

    /**
     * Obtener total de unidades.
     */
    public int obtenerTotal() {
        // retornar total
        return total;
    }

    /**
     * Obtener unidades disponibles.
     */
    public int obtenerDisponibles() {
        // retornar disponibles
        return disponibles;
    }

    /**
     * Obtener lista de duenos.
     */
    public List<String> obtenerDuenos() {
        // retornar copia
        return new ArrayList<>(duenos);
    }
}
