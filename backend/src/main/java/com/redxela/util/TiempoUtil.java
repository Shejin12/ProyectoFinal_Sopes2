package com.redxela.util;

// proveer utilidades de tiempo para la simulacion
public class TiempoUtil {

    // declarar constructor privado para evitar instanciacion
    private TiempoUtil() {
    }

    /**
     * Obtener marca de tiempo actual en milisegundos.
     */
    public static long ahora() {
        // retornar tiempo del sistema
        return System.currentTimeMillis();
    }

    /**
     * Calcular diferencia en segundos entre dos marcas.
     */
    public static long segundosEntre(long inicio, long fin) {
        // calcular diferencia
        long diferencia;
        diferencia = fin - inicio;
        // convertir a segundos
        return diferencia / 1000;
    }

    /**
     * Dormir hilo por milisegundos indicados.
     */
    public static void dormir(long milisegundos) {
        // intentar pausa
        try {
            // ejecutar pausa
            Thread.sleep(milisegundos);
        } catch (InterruptedException error) {
            // restaurar bandera de interrupcion
            Thread.currentThread().interrupt();
        }
    }
}
