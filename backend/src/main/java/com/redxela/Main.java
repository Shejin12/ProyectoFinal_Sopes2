package com.redxela;

import com.redxela.almacen.Almacen;
import com.redxela.concurrencia.CanalRecepcion;
import com.redxela.concurrencia.DetectorDeadlock;
import com.redxela.concurrencia.GestorRecursos;
import com.redxela.concurrencia.HiloDetector;
import com.redxela.concurrencia.HiloDifusor;
import com.redxela.concurrencia.HiloGenerador;
import com.redxela.concurrencia.HiloInventario;
import com.redxela.concurrencia.HiloProcesador;
import com.redxela.concurrencia.HiloRecepcionAuto;
import com.redxela.config.ConfiguracionSistema;
import com.redxela.controller.ControladorApi;
import com.redxela.controller.ControladorSocket;
import com.redxela.service.ServicioPedidos;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

// iniciar servidor Javalin y todos los hilos de simulacion
public class Main {

    // declarar metodo principal
    public static void main(String[] args) {
        // crear gestor de recursos
        GestorRecursos gestor;
        gestor = new GestorRecursos();
        // crear detector
        DetectorDeadlock detector;
        detector = new DetectorDeadlock(gestor);
        // crear almacen
        Almacen almacen;
        almacen = new Almacen();
        // crear canal
        CanalRecepcion canal;
        canal = new CanalRecepcion();
        // crear servicio
        ServicioPedidos servicio;
        servicio = new ServicioPedidos(gestor, detector, almacen, canal);
        // crear controladores
        ControladorApi controlador;
        controlador = new ControladorApi(servicio, almacen);
        ControladorSocket socket;
        socket = new ControladorSocket();
        // crear e iniciar hilos
        HiloGenerador generador;
        generador = new HiloGenerador(servicio);
        Thread hiloGenerador;
        hiloGenerador = new Thread(generador);
        hiloGenerador.setDaemon(true);
        hiloGenerador.start();
        // crear hilo de inventario
        HiloInventario inventario;
        inventario = new HiloInventario(canal, servicio);
        Thread hiloInventario;
        hiloInventario = new Thread(inventario);
        hiloInventario.setDaemon(true);
        hiloInventario.start();
        // crear procesadores
        HiloProcesador procesadorUno;
        procesadorUno = new HiloProcesador(servicio, gestor, detector);
        Thread hiloUno;
        hiloUno = new Thread(procesadorUno);
        hiloUno.setDaemon(true);
        hiloUno.start();
        // crear segundo procesador
        HiloProcesador procesadorDos;
        procesadorDos = new HiloProcesador(servicio, gestor, detector);
        Thread hiloDos;
        hiloDos = new Thread(procesadorDos);
        hiloDos.setDaemon(true);
        hiloDos.start();
        // crear tercer procesador
        HiloProcesador procesadorTres;
        procesadorTres = new HiloProcesador(servicio, gestor, detector);
        Thread hiloTres;
        hiloTres = new Thread(procesadorTres);
        hiloTres.setDaemon(true);
        hiloTres.start();
        // crear hilo detector
        HiloDetector vigilante;
        vigilante = new HiloDetector(detector);
        Thread hiloDetector;
        hiloDetector = new Thread(vigilante);
        hiloDetector.setDaemon(true);
        hiloDetector.start();
        // crear hilo recepcion automatica
        HiloRecepcionAuto recepcionAuto;
        recepcionAuto = new HiloRecepcionAuto(servicio);
        Thread hiloRecepcion;
        hiloRecepcion = new Thread(recepcionAuto);
        hiloRecepcion.setDaemon(true);
        hiloRecepcion.start();
        // crear hilo difusor
        HiloDifusor difusor;
        difusor = new HiloDifusor(controlador, socket);
        Thread hiloDifusor;
        hiloDifusor = new Thread(difusor);
        hiloDifusor.setDaemon(true);
        hiloDifusor.start();
        // crear aplicacion Javalin
        Javalin app;
        // configurar mapeador para serializar campos privados con nombres en espanol
        // ObjectMapper mapeador = new ObjectMapper();
        // mapeador.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // JavalinJackson jsonMapper = new JavalinJackson(mapeador, true);
        ObjectMapper mapeador;
        mapeador = new ObjectMapper();
        // permitir serializar campos privados
        mapeador.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // crear adaptador json
        JavalinJackson jsonMapper;
        jsonMapper = new JavalinJackson(mapeador, true);
        // app = Javalin.create(configurar -> {
        //     // habilitar CORS
        //     configurar.bundledPlugins.enableCors(cors -> {
        //         // permitir cualquier origen
        //         cors.addRule(regla -> {
        //             regla.anyHost();
        //         });
        //     });
        // });
        app = Javalin.create(configurar -> {
            // asignar mapeador personalizado
            configurar.jsonMapper(jsonMapper);
            // habilitar CORS
            configurar.bundledPlugins.enableCors(cors -> {
                // permitir cualquier origen
                cors.addRule(regla -> {
                    regla.anyHost();
                });
            });
        });
        // registrar rutas
        controlador.registrar(app);
        // registrar websocket de estado
        app.ws("/ws/estado", ws -> {
            // agregar al conectar
            ws.onConnect(socket::agregar);
            // remover al cerrar
            ws.onClose(socket::remover);
        });
        // iniciar servidor
        app.start(ConfiguracionSistema.PUERTO_SERVIDOR);
        // imprimir mensaje
        System.out.println("REDXela iniciado en puerto " + ConfiguracionSistema.PUERTO_SERVIDOR);
    }
}
