package com.redxela.concurrencia;

import com.redxela.service.ServicioPedidos;
import com.redxela.util.TiempoUtil;
import java.util.Random;

// generar recepcion automatica para sostener stock y almacen
public class HiloRecepcionAuto implements Runnable {

    // declarar servicio
    private final ServicioPedidos servicio;

    // declarar azar
    private final Random azar;

    // declarar bandera
    private volatile boolean activo;

    // construir hilo con servicio
    public HiloRecepcionAuto(ServicioPedidos servicio) {
        // asignar servicio
        this.servicio = servicio;
        // crear azar
        this.azar = new Random();
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
     * Ejecutar recepcion periodica automatica.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // dormir intervalo base
            TiempoUtil.dormir(9000);
            // verificar si sigue activo
            if (activo == false) {
                break;
            }
            // generar recepcion solo si generador esta activo
            try {
                // verificar generador
                if (servicio.estaGeneradorActivo()) {
                    // invocar generacion
                    generarRecepcion();
                }
            } catch (Exception error) {
                // continuar en error
            }
        }
    }

    // generar una recepcion aleatoria
    private void generarRecepcion() {
        // definir catalogo
        String[] catalogo;
        catalogo = new String[]{"P-FRAGIL-01", "P-FRAGIL-02", "P-PESADO-01", "P-PEREC-01", "P-ELEC-01", "P-ELEC-02", "P-TEXTIL-01", "P-TEXTIL-02"};
        // elegir indice
        int indice;
        indice = azar.nextInt(catalogo.length);
        // elegir cantidad de 10 a 25
        int cantidad;
        cantidad = 10 + azar.nextInt(16);
        // registrar llegada
        servicio.registrarLlegada(catalogo[indice], cantidad);
    }
}
