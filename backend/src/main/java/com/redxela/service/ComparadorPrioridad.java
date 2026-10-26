package com.redxela.service;

import com.redxela.model.Pedido;
import java.util.Comparator;

// comparar por nivel de servicio y luego llegada
public class ComparadorPrioridad implements Comparator<Pedido> {

    // construir comparador
    public ComparadorPrioridad() {
    }

    /**
     * Comparar dos pedidos por prioridad.
     */
    @Override
    public int compare(Pedido primero, Pedido segundo) {
        // comparar nivel
        int nivelPrimero;
        nivelPrimero = primero.obtenerNivel().obtenerValor();
        int nivelSegundo;
        nivelSegundo = segundo.obtenerNivel().obtenerValor();
        // verificar diferencia de nivel
        if (nivelPrimero < nivelSegundo) {
            return -1;
        }
        if (nivelPrimero > nivelSegundo) {
            return 1;
        }
        // desempatar por llegada
        long llegadaPrimero;
        llegadaPrimero = primero.obtenerCreadoEn();
        long llegadaSegundo;
        llegadaSegundo = segundo.obtenerCreadoEn();
        // comparar llegada
        if (llegadaPrimero < llegadaSegundo) {
            return -1;
        }
        if (llegadaPrimero > llegadaSegundo) {
            return 1;
        }
        // declarar empate
        return 0;
    }
}
