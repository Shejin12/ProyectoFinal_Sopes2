package com.redxela.concurrencia;

import com.redxela.model.AvisoLlegada;
import com.redxela.service.ServicioPedidos;

// consumir avisos de recepcion a ritmo propio de inventario
public class HiloInventario implements Runnable {

    // declarar canal
    private final CanalRecepcion canal;

    // declarar servicio
    private final ServicioPedidos servicio;

    // declarar bandera
    private volatile boolean activo;

    // construir hilo con canal y servicio
    public HiloInventario(CanalRecepcion canal, ServicioPedidos servicio) {
        // asignar canal
        this.canal = canal;
        // asignar servicio
        this.servicio = servicio;
        // iniciar activo
        this.activo = true;
    }

    /**
     * Detener ejecucion del hilo.
     */
    public void detener() {
        // marcar inactivo
        activo = false;
    }

    /**
     * Ejecutar consumo continuo de avisos.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // consumir aviso bloqueante
            AvisoLlegada aviso;
            aviso = canal.consumir();
            // verificar nulo por interrupcion
            if (aviso == null) {
                continue;
            }
            // procesar aviso
            try {
                // delegar a servicio
                servicio.procesarAviso(aviso);
                // simular ritmo propio con pausa
                Thread.sleep(800);
            } catch (InterruptedException error) {
                // restaurar bandera
                Thread.currentThread().interrupt();
            } catch (Exception error) {
                // continuar en error
            }
        }
    }
}
