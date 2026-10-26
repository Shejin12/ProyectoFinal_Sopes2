package com.redxela.concurrencia;

import com.redxela.model.AvisoLlegada;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// comunicar recepcion con inventario sin acceso directo
public class CanalRecepcion {

    // declarar cola bloqueante
    private final BlockingQueue<AvisoLlegada> cola;

    // construir canal con cola ilimitada
    public CanalRecepcion() {
        // crear cola enlazada
        cola = new LinkedBlockingQueue<>();
    }

    /**
     * Publicar aviso en la cola.
     */
    public void publicar(AvisoLlegada aviso) {
        // intentar ofrecer
        try {
            // insertar aviso
            cola.put(aviso);
        } catch (InterruptedException error) {
            // restaurar bandera
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Consumir aviso de forma bloqueante.
     */
    public AvisoLlegada consumir() {
        // declarar resultado
        AvisoLlegada resultado;
        resultado = null;
        // intentar tomar
        try {
            // extraer de cola
            resultado = cola.take();
        } catch (InterruptedException error) {
            // restaurar bandera
            Thread.currentThread().interrupt();
        }
        // retornar resultado
        return resultado;
    }

    /**
     * Obtener tamano actual de la cola.
     */
    public int obtenerTamano() {
        // retornar tamano
        return cola.size();
    }
}
