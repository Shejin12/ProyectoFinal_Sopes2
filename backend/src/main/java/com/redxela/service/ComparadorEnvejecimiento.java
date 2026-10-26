package com.redxela.service;

import com.redxela.model.Pedido;
import java.util.Comparator;

// comparar por puntaje que evita inanicion
public class ComparadorEnvejecimiento implements Comparator<Pedido> {

    // construir comparador
    public ComparadorEnvejecimiento() {
    }

    /**
     * Comparar dos pedidos por envejecimiento.
     */
    @Override
    public int compare(Pedido primero, Pedido segundo) {
        // calcular puntaje primero
        long puntajePrimero;
        puntajePrimero = calcularPuntaje(primero);
        // calcular puntaje segundo
        long puntajeSegundo;
        puntajeSegundo = calcularPuntaje(segundo);
        // comparar puntaje
        if (puntajePrimero < puntajeSegundo) {
            return -1;
        }
        if (puntajePrimero > puntajeSegundo) {
            return 1;
        }
        // declarar empate
        return 0;
    }

    // calcular puntaje menor es mas prioritario
    private long calcularPuntaje(Pedido pedido) {
        // obtener espera en segundos
        long ahora;
        ahora = System.currentTimeMillis();
        long esperaSeg;
        esperaSeg = (ahora - pedido.obtenerCreadoEn()) / 1000;
        // obtener nivel
        int nivel;
        nivel = pedido.obtenerNivel().obtenerValor();
        // calcular base por nivel
        long base;
        base = nivel * 600L;
        // restar espera para dar oportunidad
        long puntaje;
        puntaje = base - esperaSeg;
        // retornar puntaje
        return puntaje;
    }
}
