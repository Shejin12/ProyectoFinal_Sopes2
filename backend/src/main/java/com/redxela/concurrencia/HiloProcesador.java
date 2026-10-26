package com.redxela.concurrencia;

import com.redxela.config.ConfiguracionSistema;
import com.redxela.model.DetallePedido;
import com.redxela.model.EstadoPedido;
import com.redxela.model.Pedido;
import com.redxela.model.Producto;
import com.redxela.model.RecursoTipo;
import com.redxela.model.TipoMercancia;
import com.redxela.service.ServicioPedidos;
import com.redxela.util.TiempoUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// procesar pedidos tomando recursos en orden segun mercancia
public class HiloProcesador implements Runnable {

    // declarar servicio
    private final ServicioPedidos servicio;

    // declarar gestor
    private final GestorRecursos gestor;

    // declarar detector
    private final DetectorDeadlock detector;

    // declarar bandera
    private volatile boolean activo;

    // construir hilo con dependencias
    public HiloProcesador(ServicioPedidos servicio, GestorRecursos gestor, DetectorDeadlock detector) {
        // asignar servicio
        this.servicio = servicio;
        // asignar gestor
        this.gestor = gestor;
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
     * Ejecutar ciclo de despacho continuo.
     */
    @Override
    public void run() {
        // repetir mientras activo
        while (activo) {
            // tomar siguiente
            Pedido pedido;
            pedido = null;
            try {
                // solicitar pedido
                pedido = servicio.tomarSiguiente();
            } catch (Exception error) {
                // continuar en error
                pedido = null;
            }
            // verificar si hay trabajo
            if (pedido == null) {
                // dormir breve
                TiempoUtil.dormir(500);
                continue;
            }
            // procesar pedido
            try {
                // invocar procesamiento
                procesar(pedido);
            } catch (Exception error) {
                // liberar en error
                gestor.liberarTodos(pedido.obtenerIdPedido());
            }
        }
    }

    // procesar un pedido completo
    private void procesar(Pedido pedido) {
        // determinar tipo dominante
        TipoMercancia tipo;
        tipo = determinarTipo(pedido);
        // obtener orden de recursos
        List<RecursoTipo> orden;
        orden = tipo.obtenerOrdenRecursos();
        // intentar adquirir en orden
        for (int indice = 0; indice < orden.size(); indice++) {
            // obtener recurso
            RecursoTipo recurso;
            recurso = orden.get(indice);
            // actualizar recurso actual
            pedido.asignarRecursoActual(recurso.name());
            // intentar adquirir con espera
            boolean adquirido;
            adquirido = gestor.intentarAdquirir(pedido.obtenerIdPedido(), recurso, 4000);
            // verificar si fallo
            if (adquirido == false) {
                // marcar bloqueado
                pedido.asignarEstado(EstadoPedido.BLOQUEADO);
                pedido.asignarMotivoBloqueo("Espera " + recurso.name());
                // verificar deadlock
                detector.verificar();
                // dormir breve
                TiempoUtil.dormir(1000);
                // liberar lo adquirido
                gestor.liberarTodos(pedido.obtenerIdPedido());
                // devolver a cola como espera
                devolverACola(pedido);
                // terminar procesamiento
                return;
            }
            // actualizar progreso parcial de adquisicion
            int progresoAdq;
            progresoAdq = ((indice + 1) * 40) / orden.size();
            pedido.asignarProgreso(progresoAdq);
        }
        // simular trabajo segun unidades
        int totalUnidades;
        totalUnidades = pedido.obtenerTotalArticulos();
        if (totalUnidades <= 0) {
            totalUnidades = 1;
        }
        // calcular tiempo total
        long tiempoTotal;
        tiempoTotal = (long) totalUnidades * ConfiguracionSistema.MS_POR_UNIDAD;
        // dividir en pasos
        int pasos;
        pasos = 10;
        long pasoMs;
        pasoMs = tiempoTotal / pasos;
        // ejecutar pasos
        for (int paso = 1; paso <= pasos; paso++) {
            // dormir paso
            TiempoUtil.dormir(pasoMs);
            // calcular progreso 40 a 100
            int progreso;
            progreso = 40 + (paso * 60 / pasos);
            pedido.asignarProgreso(progreso);
            // calcular restante
            long restante;
            restante = (tiempoTotal - (paso * pasoMs)) / 1000;
            pedido.asignarRestanteSegundos(restante);
        }
        // finalizar en servicio
        servicio.finalizarPedido(pedido);
    }

    // determinar tipo dominante por primer producto
    private TipoMercancia determinarTipo(Pedido pedido) {
        // crear mapa de conteo simple por primer detalle
        TipoMercancia resultado;
        resultado = TipoMercancia.TEXTIL;
        // verificar detalles
        if (pedido.obtenerDetalles().size() > 0) {
            // obtener primer detalle
            DetallePedido primero;
            primero = pedido.obtenerDetalles().get(0);
            // buscar producto en servicio
            // usar regla por prefijo para evitar acceso directo
            String idProducto;
            idProducto = primero.obtenerIdProducto();
            // clasificar por prefijo
            if (idProducto.contains("FRAGIL")) {
                resultado = TipoMercancia.FRAGIL;
            } else if (idProducto.contains("PESADO")) {
                resultado = TipoMercancia.PESADO;
            } else if (idProducto.contains("PEREC")) {
                resultado = TipoMercancia.PERECEDERO;
            } else if (idProducto.contains("ELEC")) {
                resultado = TipoMercancia.ELECTRONICO;
            } else {
                resultado = TipoMercancia.TEXTIL;
            }
        }
        // retornar resultado
        return resultado;
    }

    // devolver pedido a espera tras fallo temporal
    private void devolverACola(Pedido pedido) {
        // liberar ya se hizo fuera
        // crear detalles copia
        // usar servicio para reencolar como nuevo con mismo datos
        // simplificar: crear nuevo pedido con mismos datos
        try {
            // solicitar creacion
            servicio.crearPedido(pedido.obtenerIdCliente(), pedido.obtenerNivel().obtenerValor(), pedido.obtenerDetalles());
        } catch (Exception error) {
            // ignorar error
        }
    }
}
