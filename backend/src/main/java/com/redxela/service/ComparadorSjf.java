package com.redxela.service;

import com.redxela.model.Pedido;
import java.util.Comparator;

// comparar por menor cantidad de articulos primero
public class ComparadorSjf implements Comparator<Pedido> {

    // construir comparador
    public ComparadorSjf() {
    }

    /**
     * Comparar dos pedidos por trabajo mas corto.
     */
    @Override
    public int compare(Pedido primero, Pedido segundo) {
        // obtener totales
        int totalPrimero;
        totalPrimero = primero.obtenerTotalArticulos();
        int totalSegundo;
        totalSegundo = segundo.obtenerTotalArticulos();
        // comparar totales
        if (totalPrimero < totalSegundo) {
            return -1;
        }
        if (totalPrimero > totalSegundo) {
            return 1;
        }
        // desempatar por llegada
        long llegadaPrimero;
        llegadaPrimero = primero.obtenerCreadoEn();
        long llegadaSegundo;
        llegadaSegundo = segundo.obtenerCreadoEn();
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
