package com.redxela.model;

import java.util.ArrayList;
import java.util.List;

// definir tipos de mercancia con recursos requeridos
public enum TipoMercancia {
    FRAGIL("Mercancia delicada que exige revision cuidadosa"),
    PESADO("Mercancia de gran peso que exige equipo de carga"),
    PERECEDERO("Mercancia con tiempo limite que exige control rapido"),
    ELECTRONICO("Mercancia sensible que exige escaneo y control"),
    TEXTIL("Mercancia ligera que exige bodega y empaque");

    // declarar descripcion del tipo
    private final String descripcion;

    // construir tipo con descripcion
    TipoMercancia(String descripcion) {
        // asignar descripcion
        this.descripcion = descripcion;
    }

    /**
     * Obtener descripcion del tipo.
     */
    public String obtenerDescripcion() {
        // retornar descripcion
        return descripcion;
    }

    /**
     * Obtener lista de recursos en orden de adquisicion.
     */
    public List<RecursoTipo> obtenerOrdenRecursos() {
        // crear lista vacia
        List<RecursoTipo> lista;
        lista = new ArrayList<>();
        // clasificar por tipo actual
        if (this == FRAGIL) {
            // agregar orden fragil empaque luego calidad
            lista.add(RecursoTipo.EMPAQUE);
            lista.add(RecursoTipo.CALIDAD);
            lista.add(RecursoTipo.ESCANEO);
            lista.add(RecursoTipo.BODEGA);
        } else if (this == PESADO) {
            // agregar orden pesado calidad luego empaque para contencion inversa
            lista.add(RecursoTipo.CALIDAD);
            lista.add(RecursoTipo.EMPAQUE);
            lista.add(RecursoTipo.MONTACARGAS);
            lista.add(RecursoTipo.CARGA);
            lista.add(RecursoTipo.BODEGA);
        } else if (this == PERECEDERO) {
            // agregar orden perecedero
            lista.add(RecursoTipo.CALIDAD);
            lista.add(RecursoTipo.EMPAQUE);
            lista.add(RecursoTipo.ESCANEO);
            lista.add(RecursoTipo.CARGA);
        } else if (this == ELECTRONICO) {
            // agregar orden electronico
            lista.add(RecursoTipo.ESCANEO);
            lista.add(RecursoTipo.CALIDAD);
            lista.add(RecursoTipo.EMPAQUE);
            lista.add(RecursoTipo.BODEGA);
        } else {
            // agregar orden textil por defecto
            lista.add(RecursoTipo.BODEGA);
            lista.add(RecursoTipo.EMPAQUE);
            lista.add(RecursoTipo.CARGA);
        }
        // retornar lista
        return lista;
    }
}
