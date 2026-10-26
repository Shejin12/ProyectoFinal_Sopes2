package com.redxela.concurrencia;

import com.redxela.config.ConfiguracionSistema;
import com.redxela.controller.ControladorApi;
import com.redxela.controller.ControladorSocket;
import com.redxela.util.TiempoUtil;
import java.util.Map;

// difundir estado del sistema por websocket cada segundo
public class HiloDifusor implements Runnable {

    // declarar controlador api
    private final ControladorApi api;

    // declarar controlador socket
    private final ControladorSocket socket;

    // declarar bandera
    private volatile boolean activo;

    // construir hilo con dependencias
    public HiloDifusor(ControladorApi api, ControladorSocket socket) {
        // asignar api
        this.api = api;
        // asignar socket
        this.socket = socket;
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
     * Ejecutar difusion periodica.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // dormir intervalo
            TiempoUtil.dormir(ConfiguracionSistema.MS_DIFUSION);
            // construir estado
            try {
                // obtener estado
                Map<String, Object> estado;
                estado = api.construirEstado();
                // difundir
                socket.difundir(estado);
            } catch (Exception error) {
                // continuar en error
            }
        }
    }
}
