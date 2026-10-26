package com.redxela.controller;

import io.javalin.websocket.WsContext;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// mantener sesiones websocket para difusion de estado
public class ControladorSocket {

    // declarar sesiones activas
    private final Map<String, WsContext> sesiones;

    // construir controlador
    public ControladorSocket() {
        // crear mapa concurrente
        sesiones = new ConcurrentHashMap<>();
    }

    /**
     * Agregar sesion nueva.
     */
    public void agregar(WsContext ctx) {
        // guardar sesion
        sesiones.put(ctx.sessionId(), ctx);
    }

    /**
     * Remover sesion cerrada.
     */
    public void remover(WsContext ctx) {
        // quitar sesion
        sesiones.remove(ctx.sessionId());
    }

    /**
     * Difundir objeto a todas las sesiones.
     */
    public void difundir(Object mensaje) {
        // recorrer sesiones
        for (WsContext ctx : sesiones.values()) {
            // intentar enviar
            try {
                // verificar abierta
                if (ctx.session.isOpen()) {
                    // enviar mensaje
                    ctx.send(mensaje);
                }
            } catch (Exception error) {
                // ignorar error de envio
            }
        }
    }
}
