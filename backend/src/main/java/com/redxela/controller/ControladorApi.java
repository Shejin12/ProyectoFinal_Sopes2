package com.redxela.controller;

import com.redxela.almacen.Almacen;
import com.redxela.model.DetallePedido;
import com.redxela.model.PoliticaPlanificacion;
import com.redxela.model.TipoMercancia;
import com.redxela.service.ServicioPedidos;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// exponer rutas HTTP de la simulacion
public class ControladorApi {

    // declarar servicio
    private final ServicioPedidos servicio;

    // declarar almacen
    private final Almacen almacen;

    // construir controlador con dependencias
    public ControladorApi(ServicioPedidos servicio, Almacen almacen) {
        // asignar servicio
        this.servicio = servicio;
        // asignar almacen
        this.almacen = almacen;
    }

    /**
     * Registrar rutas en aplicacion Javalin.
     */
    public void registrar(Javalin app) {
        // registrar clientes
        app.get("/api/clientes", this::listarClientes);
        app.post("/api/clientes", this::crearCliente);
        // registrar productos y mercancias
        app.get("/api/productos", this::listarProductos);
        app.get("/api/mercancias", this::listarMercancias);
        // registrar estado operativo
        app.get("/api/recursos", this::listarRecursos);
        app.get("/api/cola", this::listarCola);
        app.get("/api/proceso", this::listarProceso);
        app.get("/api/completados", this::listarCompletados);
        app.get("/api/estadisticas", this::verEstadisticas);
        app.get("/api/bloqueos", this::listarBloqueos);
        app.get("/api/almacen", this::verAlmacen);
        app.get("/api/estado", this::verEstado);
        // registrar acciones
        app.post("/api/pedidos", this::crearPedido);
        app.post("/api/recepcion", this::registrarRecepcion);
        app.post("/api/politica", this::cambiarPolitica);
        app.post("/api/generador", this::cambiarGenerador);
        app.post("/api/deadlock/provocar", this::provocarDeadlock);
        app.post("/api/deadlock/resolver", this::resolverDeadlock);
    }

    /**
     * Construir mapa de estado completo.
     */
    public Map<String, Object> construirEstado() {
        // crear mapa
        Map<String, Object> estado;
        estado = new HashMap<>();
        // agregar secciones
        estado.put("recursos", servicio.obtenerRecursos());
        estado.put("cola", servicio.obtenerColaOrdenada());
        estado.put("proceso", servicio.obtenerEnProceso());
        estado.put("completados", servicio.obtenerCompletados());
        estado.put("estadisticas", servicio.obtenerResumen());
        estado.put("bloqueos", servicio.obtenerBloqueos());
        estado.put("politica", servicio.obtenerPolitica().name());
        // retornar mapa
        return estado;
    }

    // listar clientes
    private void listarClientes(Context ctx) {
        // responder con lista
        ctx.json(servicio.obtenerClientes());
    }

    // crear cliente desde peticion
    private void crearCliente(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionCliente peticion;
            peticion = ctx.bodyAsClass(PeticionCliente.class);
            // validar nombre
            String nombre;
            nombre = peticion.obtenerNombre();
            if (nombre == null) {
                nombre = "Sin nombre";
            }
            if (nombre.equals("")) {
                nombre = "Sin nombre";
            }
            // crear cliente
            ctx.json(servicio.crearCliente(nombre, peticion.obtenerNivel()));
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }

    // listar productos
    private void listarProductos(Context ctx) {
        // responder con lista
        ctx.json(servicio.obtenerProductos());
    }

    // listar tipos de mercancia con recursos
    private void listarMercancias(Context ctx) {
        // crear lista
        List<Map<String, Object>> lista;
        lista = new ArrayList<>();
        // recorrer tipos
        for (TipoMercancia tipo : TipoMercancia.values()) {
            // crear mapa
            Map<String, Object> mapa;
            mapa = new HashMap<>();
            // agregar datos
            mapa.put("nombre", tipo.name());
            mapa.put("descripcion", tipo.obtenerDescripcion());
            mapa.put("recursos", tipo.obtenerOrdenRecursos());
            // agregar a lista
            lista.add(mapa);
        }
        // responder
        ctx.json(lista);
    }

    // listar recursos
    private void listarRecursos(Context ctx) {
        // responder con estado
        ctx.json(servicio.obtenerRecursos());
    }

    // listar cola ordenada
    private void listarCola(Context ctx) {
        // responder con cola
        ctx.json(servicio.obtenerColaOrdenada());
    }

    // listar en proceso
    private void listarProceso(Context ctx) {
        // responder con proceso
        ctx.json(servicio.obtenerEnProceso());
    }

    // listar completados
    private void listarCompletados(Context ctx) {
        // responder con completados
        ctx.json(servicio.obtenerCompletados());
    }

    // ver estadisticas
    private void verEstadisticas(Context ctx) {
        // responder con resumen
        ctx.json(servicio.obtenerResumen());
    }

    // listar bloqueos
    private void listarBloqueos(Context ctx) {
        // responder con bloqueos
        ctx.json(servicio.obtenerBloqueos());
    }

    // ver almacen paginado
    private void verAlmacen(Context ctx) {
        // leer pagina
        String paginaTexto;
        paginaTexto = ctx.queryParam("pagina");
        String tamanoTexto;
        tamanoTexto = ctx.queryParam("tamano");
        // definir valores por defecto
        int pagina;
        pagina = 0;
        int tamano;
        tamano = 20;
        // convertir pagina
        try {
            if (paginaTexto != null) {
                pagina = Integer.parseInt(paginaTexto);
            }
        } catch (Exception error) {
            pagina = 0;
        }
        // convertir tamano
        try {
            if (tamanoTexto != null) {
                tamano = Integer.parseInt(tamanoTexto);
            }
        } catch (Exception error) {
            tamano = 20;
        }
        // crear respuesta
        Map<String, Object> respuesta;
        respuesta = new HashMap<>();
        // agregar datos
        respuesta.put("pagina", pagina);
        respuesta.put("tamano", tamano);
        respuesta.put("capacidad", almacen.obtenerCapacidad());
        respuesta.put("libres", almacen.contarLibres());
        respuesta.put("ocupadas", almacen.contarOcupadas());
        respuesta.put("ubicaciones", almacen.obtenerPagina(pagina, tamano));
        // responder
        ctx.json(respuesta);
    }

    // ver estado completo
    private void verEstado(Context ctx) {
        // responder con estado
        ctx.json(construirEstado());
    }

    // crear pedido manual
    private void crearPedido(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionPedido peticion;
            peticion = ctx.bodyAsClass(PeticionPedido.class);
            // convertir detalles
            List<DetallePedido> detalles;
            detalles = new ArrayList<>();
            // recorrer entradas
            if (peticion.obtenerDetalles() != null) {
                for (DetalleEntrada entrada : peticion.obtenerDetalles()) {
                    // crear detalle
                    DetallePedido detalle;
                    detalle = new DetallePedido(entrada.obtenerIdProducto(), entrada.obtenerCantidad());
                    // agregar
                    detalles.add(detalle);
                }
            }
            // verificar no vacio
            if (detalles.size() == 0) {
                ctx.status(400).json("Detalles vacios");
                return;
            }
            // crear pedido
            ctx.json(servicio.crearPedido(peticion.obtenerIdCliente(), peticion.obtenerNivel(), detalles));
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }

    // registrar llegada de mercaderia
    private void registrarRecepcion(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionRecepcion peticion;
            peticion = ctx.bodyAsClass(PeticionRecepcion.class);
            // registrar en servicio
            servicio.registrarLlegada(peticion.obtenerIdProducto(), peticion.obtenerCantidad());
            // responder ok
            Map<String, String> respuesta;
            respuesta = new HashMap<>();
            respuesta.put("resultado", "registrado");
            ctx.json(respuesta);
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }

    // cambiar politica
    private void cambiarPolitica(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionPolitica peticion;
            peticion = ctx.bodyAsClass(PeticionPolitica.class);
            // convertir a enum
            PoliticaPlanificacion nueva;
            nueva = PoliticaPlanificacion.valueOf(peticion.obtenerPolitica());
            // asignar
            servicio.cambiarPolitica(nueva);
            // responder
            ctx.json(servicio.obtenerResumen());
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }

    // cambiar generador
    private void cambiarGenerador(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionGenerador peticion;
            peticion = ctx.bodyAsClass(PeticionGenerador.class);
            // asignar
            servicio.asignarGenerador(peticion.estaActivo());
            // responder
            ctx.json(servicio.obtenerResumen());
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }

    // provocar deadlock
    private void provocarDeadlock(Context ctx) {
        // invocar servicio
        try {
            // provocar
            ctx.json(servicio.provocarDeadlock());
        } catch (Exception error) {
            // responder error
            ctx.status(500).json(error.getMessage());
        }
    }

    // resolver deadlock
    private void resolverDeadlock(Context ctx) {
        // leer cuerpo
        try {
            // convertir cuerpo
            PeticionResolver peticion;
            peticion = ctx.bodyAsClass(PeticionResolver.class);
            // resolver
            servicio.resolverDeadlock(peticion.obtenerGanadora());
            // responder
            ctx.json(servicio.obtenerBloqueos());
        } catch (Exception error) {
            // responder error
            ctx.status(400).json(error.getMessage());
        }
    }
}
