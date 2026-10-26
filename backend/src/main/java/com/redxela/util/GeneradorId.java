package com.redxela.util;

// generar identificadores unicos de forma sincronizada
public class GeneradorId {

    // declarar contador global
    private static long contador = 0;

    // declarar bloqueo para exclusion mutua
    private static final Object cerrojo = new Object();

    // declarar constructor privado para evitar instanciacion
    private GeneradorId() {
    }

    /**
     * Obtener siguiente identificador unico.
     */
    public static long siguiente() {
        // sincronizar acceso al contador
        synchronized (cerrojo) {
            // incrementar contador
            contador = contador + 1;
            // retornar valor actual
            return contador;
        }
    }

    /**
     * Obtener prefijo mas identificador.
     */
    public static String conPrefijo(String prefijo) {
        // solicitar numero nuevo
        long numero;
        numero = siguiente();
        // concatenar prefijo con numero
        return prefijo + numero;
    }
}
