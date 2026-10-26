package com.redxela.service;

import com.redxela.model.Pedido;
import com.redxela.model.PoliticaPlanificacion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// ordenar cola de espera segun politica seleccionada
public class Planificador {

    // declarar politica actual
    private PoliticaPlanificacion politica;

    // construir planificador con politica inicial
    public Planificador(PoliticaPlanificacion politica) {
        // asignar politica
        this.politica = politica;
    }

    // construir planificador por defecto con prioridad estricta
    public Planificador() {
        // asignar politica por defecto
        this.politica = PoliticaPlanificacion.PRIORIDAD_ESTRICTA;
    }

    /**
     * Asignar nueva politica de planificacion.
     */
    public void asignarPolitica(PoliticaPlanificacion politica) {
        // asignar valor
        this.politica = politica;
    }

    /**
     * Obtener politica actual.
     */
    public PoliticaPlanificacion obtenerPolitica() {
        // retornar politica
        return politica;
    }

    /**
     * Ordenar copia de cola segun politica.
     */
    public List<Pedido> ordenar(List<Pedido> cola) {
        // crear copia
        List<Pedido> copia;
        copia = new ArrayList<>(cola);
        // clasificar por politica
        if (politica == PoliticaPlanificacion.PRIORIDAD_ESTRICTA) {
            // ordenar por prioridad estricta
            ordenarPorPrioridad(copia);
        } else if (politica == PoliticaPlanificacion.ENVEJECIMIENTO) {
            // ordenar por envejecimiento
            ordenarPorEnvejecimiento(copia);
        } else {
            // ordenar por trabajo mas corto
            ordenarPorSjf(copia);
        }
        // retornar copia ordenada
        return copia;
    }

    // ordenar por nivel y luego por llegada
    private void ordenarPorPrioridad(List<Pedido> lista) {
        // crear comparador
        Comparator<Pedido> comparador;
        comparador = new ComparadorPrioridad();
        // ejecutar orden
        Collections.sort(lista, comparador);
    }

    // ordenar por puntaje que combina nivel y espera
    private void ordenarPorEnvejecimiento(List<Pedido> lista) {
        // crear comparador
        Comparator<Pedido> comparador;
        comparador = new ComparadorEnvejecimiento();
        // ejecutar orden
        Collections.sort(lista, comparador);
    }

    // ordenar por total de articulos ascendente
    private void ordenarPorSjf(List<Pedido> lista) {
        // crear comparador
        Comparator<Pedido> comparador;
        comparador = new ComparadorSjf();
        // ejecutar orden
        Collections.sort(lista, comparador);
    }
}
