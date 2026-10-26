package com.redxela.concurrencia;

import com.redxela.util.TiempoUtil;

// vigilar bloqueos de forma periodica
public class HiloDetector implements Runnable {

    // declarar detector
    private final DetectorDeadlock detector;

    // declarar bandera
    private volatile boolean activo;

    // construir hilo con detector
    public HiloDetector(DetectorDeadlock detector) {
        // asignar detector
        this.detector = detector;
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
     * Ejecutar verificacion periodica.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // verificar bloqueos
            try {
                // invocar verificacion
                detector.verificar();
                // limpiar resueltos
                detector.limpiarResueltos();
            } catch (Exception error) {
                // continuar en error
            }
            // dormir un segundo
            TiempoUtil.dormir(1000);
        }
    }
}
