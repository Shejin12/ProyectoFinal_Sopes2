package com.redxela.concurrencia;

import com.redxela.config.ConfiguracionSistema;
import com.redxela.model.DetallePedido;
import com.redxela.model.NivelServicio;
import com.redxela.service.ServicioPedidos;
import com.redxela.util.TiempoUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// generar pedidos automaticos sin limite de cantidad
public class HiloGenerador implements Runnable {

    // declarar servicio de pedidos
    private final ServicioPedidos servicio;

    // declarar generador aleatorio
    private final Random azar;

    // declarar bandera de ejecucion
    private volatile boolean activo;

    // construir hilo con servicio
    public HiloGenerador(ServicioPedidos servicio) {
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
     * Ejecutar ciclo de generacion continua.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // verificar si generador esta habilitado
            if (servicio.estaGeneradorActivo()) {
                // generar pedido
                try {
                    // invocar generacion
                    generarUno();
                } catch (Exception error) {
                    // ignorar error y continuar
                }
            }
            // calcular pausa aleatoria
            int rango;
            rango = ConfiguracionSistema.MS_GENERADOR_MAX - ConfiguracionSistema.MS_GENERADOR_MIN;
            int pausa;
            pausa = ConfiguracionSistema.MS_GENERADOR_MIN + azar.nextInt(rango + 1);
            // dormir
            TiempoUtil.dormir(pausa);
        }
    }

    // generar un pedido aleatorio
    private void generarUno() {
        // elegir cliente aleatorio entre C-1 y C-5
        int numeroCliente;
        numeroCliente = azar.nextInt(5) + 1;
        String idCliente;
        idCliente = "C-" + numeroCliente;
        // elegir nivel 1 a 5
        int nivelValor;
        nivelValor = azar.nextInt(5) + 1;
        // elegir 1 o 2 detalles
        int cantidadDetalles;
        cantidadDetalles = azar.nextInt(2) + 1;
        // crear lista
        List<DetallePedido> detalles;
        detalles = new ArrayList<>();
        // crear catalogo base
        String[] catalogo;
        catalogo = new String[]{"P-FRAGIL-01", "P-FRAGIL-02", "P-PESADO-01", "P-PEREC-01", "P-ELEC-01", "P-ELEC-02", "P-TEXTIL-01", "P-TEXTIL-02"};
        // generar detalles
        for (int i = 0; i < cantidadDetalles; i++) {
            // elegir producto
            int indice;
            indice = azar.nextInt(catalogo.length);
            String idProducto;
            idProducto = catalogo[indice];
            // elegir cantidad 1 a 10
            int cantidad;
            cantidad = azar.nextInt(10) + 1;
            // crear detalle
            DetallePedido detalle;
            detalle = new DetallePedido(idProducto, cantidad);
            // agregar
            detalles.add(detalle);
        }
        // crear pedido en servicio
        servicio.crearPedido(idCliente, nivelValor, detalles);
    }
}
