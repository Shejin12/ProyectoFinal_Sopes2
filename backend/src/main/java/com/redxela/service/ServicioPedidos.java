package com.redxela.service;

import com.redxela.almacen.Almacen;
import com.redxela.concurrencia.CanalRecepcion;
import com.redxela.concurrencia.DetectorDeadlock;
import com.redxela.concurrencia.GestorRecursos;
import com.redxela.model.AvisoLlegada;
import com.redxela.model.BloqueoActivo;
import com.redxela.model.Cliente;
import com.redxela.model.DetallePedido;
import com.redxela.model.EstadoPedido;
import com.redxela.model.InfoRecurso;
import com.redxela.model.NivelServicio;
import com.redxela.model.Pedido;
import com.redxela.model.PoliticaPlanificacion;
import com.redxela.model.Producto;
import com.redxela.model.RecursoTipo;
import com.redxela.model.ResumenEstadisticas;
import com.redxela.model.TipoMercancia;
import com.redxela.util.GeneradorId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

// administrar clientes productos pedidos y colas del sistema
public class ServicioPedidos {

    // declarar mapa de clientes
    private final Map<String, Cliente> clientes;

    // declarar mapa de productos
    private final Map<String, Producto> productos;

    // declarar cola de espera
    private final List<Pedido> colaEspera;

    // declarar mapa de pedidos en proceso
    private final Map<String, Pedido> enProceso;

    // declarar lista de completados recientes
    private final List<Pedido> completados;

    // declarar planificador
    private final Planificador planificador;

    // declarar gestor de recursos
    private final GestorRecursos gestor;

    // declarar detector de deadlock
    private final DetectorDeadlock detector;

    // declarar almacen
    private final Almacen almacen;

    // declarar canal de recepcion
    private final CanalRecepcion canal;

    // declarar cerrojo para exclusion mutua
    private final ReentrantLock cerrojo;

    // declarar bandera de generador
    private boolean generadorActivo;

    // construir servicio con dependencias
    public ServicioPedidos(GestorRecursos gestor, DetectorDeadlock detector, Almacen almacen, CanalRecepcion canal) {
        // asignar gestor
        this.gestor = gestor;
        // asignar detector
        this.detector = detector;
        // asignar almacen
        this.almacen = almacen;
        // asignar canal
        this.canal = canal;
        // crear mapas
        clientes = new HashMap<>();
        productos = new HashMap<>();
        colaEspera = new ArrayList<>();
        enProceso = new HashMap<>();
        completados = new ArrayList<>();
        // crear planificador
        planificador = new Planificador(PoliticaPlanificacion.PRIORIDAD_ESTRICTA);
        // crear cerrojo
        cerrojo = new ReentrantLock();
        // activar generador
        generadorActivo = true;
        // cargar datos iniciales
        cargarClientesIniciales();
        cargarCatalogoInicial();
    }

    // cargar clientes de ejemplo
    private void cargarClientesIniciales() {
        // crear cliente expres
        Cliente primero;
        primero = new Cliente("C-1", "Cliente Expres", NivelServicio.NIVEL_1);
        clientes.put(primero.obtenerIdCliente(), primero);
        // crear cliente prioritario
        Cliente segundo;
        segundo = new Cliente("C-2", "Cliente Prioritario", NivelServicio.NIVEL_2);
        clientes.put(segundo.obtenerIdCliente(), segundo);
        // crear cliente estandar
        Cliente tercero;
        tercero = new Cliente("C-3", "Cliente Estandar", NivelServicio.NIVEL_3);
        clientes.put(tercero.obtenerIdCliente(), tercero);
        // crear cliente programado
        Cliente cuarto;
        cuarto = new Cliente("C-4", "Cliente Programado", NivelServicio.NIVEL_4);
        clientes.put(cuarto.obtenerIdCliente(), cuarto);
        // crear cliente economico
        Cliente quinto;
        quinto = new Cliente("C-5", "Cliente Economico", NivelServicio.NIVEL_5);
        clientes.put(quinto.obtenerIdCliente(), quinto);
    }

    // cargar catalogo de productos predefinido
    private void cargarCatalogoInicial() {
        // agregar vidrio fragil
        Producto vidrio;
        vidrio = new Producto("P-FRAGIL-01", "Vajilla de vidrio", TipoMercancia.FRAGIL, 50);
        productos.put(vidrio.obtenerIdProducto(), vidrio);
        // agregar espejo fragil
        Producto espejo;
        espejo = new Producto("P-FRAGIL-02", "Espejo decorativo", TipoMercancia.FRAGIL, 30);
        productos.put(espejo.obtenerIdProducto(), espejo);
        // agregar cemento pesado
        Producto cemento;
        cemento = new Producto("P-PESADO-01", "Saco de cemento", TipoMercancia.PESADO, 100);
        productos.put(cemento.obtenerIdProducto(), cemento);
        // agregar leche perecedera
        Producto leche;
        leche = new Producto("P-PEREC-01", "Caja de leche", TipoMercancia.PERECEDERO, 80);
        productos.put(leche.obtenerIdProducto(), leche);
        // agregar laptop electronica
        Producto laptop;
        laptop = new Producto("P-ELEC-01", "Laptop basica", TipoMercancia.ELECTRONICO, 25);
        productos.put(laptop.obtenerIdProducto(), laptop);
        // agregar celular electronico
        Producto celular;
        celular = new Producto("P-ELEC-02", "Celular gama media", TipoMercancia.ELECTRONICO, 40);
        productos.put(celular.obtenerIdProducto(), celular);
        // agregar camisa textil
        Producto camisa;
        camisa = new Producto("P-TEXTIL-01", "Camisa de algodon", TipoMercancia.TEXTIL, 120);
        productos.put(camisa.obtenerIdProducto(), camisa);
        // agregar pantalon textil
        Producto pantalon;
        pantalon = new Producto("P-TEXTIL-02", "Pantalon de lona", TipoMercancia.TEXTIL, 90);
        productos.put(pantalon.obtenerIdProducto(), pantalon);
    }

    /**
     * Crear cliente nuevo con nivel seleccionado.
     */
    public Cliente crearCliente(String nombre, int nivelValor) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // generar identificador
            String id;
            id = GeneradorId.conPrefijo("C-");
            // convertir nivel
            NivelServicio nivel;
            nivel = NivelServicio.desdeValor(nivelValor);
            // crear cliente
            Cliente cliente;
            cliente = new Cliente(id, nombre, nivel);
            // guardar
            clientes.put(id, cliente);
            // retornar cliente
            return cliente;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener lista de clientes.
     */
    public List<Cliente> obtenerClientes() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar copia
            return new ArrayList<>(clientes.values());
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener catalogo de productos.
     */
    public List<Producto> obtenerProductos() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar copia
            return new ArrayList<>(productos.values());
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Crear pedido manual y encolar segun stock.
     */
    public Pedido crearPedido(String idCliente, int nivelValor, List<DetallePedido> detalles) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // generar identificador
            String idPedido;
            idPedido = GeneradorId.conPrefijo("PED-");
            // convertir nivel
            NivelServicio nivel;
            nivel = NivelServicio.desdeValor(nivelValor);
            // verificar cliente existe
            if (clientes.containsKey(idCliente) == false) {
                // usar primer cliente por defecto
                idCliente = "C-3";
            }
            // crear pedido
            Pedido pedido;
            pedido = new Pedido(idPedido, idCliente, nivel, detalles);
            // verificar stock
            boolean hayStock;
            hayStock = verificarStockSinBloqueo(pedido);
            // clasificar estado
            if (hayStock) {
                pedido.asignarEstado(EstadoPedido.EN_ESPERA);
            } else {
                pedido.asignarEstado(EstadoPedido.EN_ESPERA_STOCK);
            }
            // agregar a cola
            colaEspera.add(pedido);
            // retornar pedido
            return pedido;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener cola ordenada segun politica.
     */
    public List<Pedido> obtenerColaOrdenada() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear copia
            List<Pedido> copia;
            copia = new ArrayList<>(colaEspera);
            // ordenar con planificador
            return planificador.ordenar(copia);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener pedidos en proceso.
     */
    public List<Pedido> obtenerEnProceso() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar copia
            return new ArrayList<>(enProceso.values());
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener completados recientes.
     */
    public List<Pedido> obtenerCompletados() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar copia
            return new ArrayList<>(completados);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Cambiar politica de planificacion.
     */
    public void cambiarPolitica(PoliticaPlanificacion nueva) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // asignar politica
            planificador.asignarPolitica(nueva);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener politica actual.
     */
    public PoliticaPlanificacion obtenerPolitica() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar politica
            return planificador.obtenerPolitica();
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Activar o pausar generador automatico.
     */
    public void asignarGenerador(boolean activo) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // asignar bandera
            generadorActivo = activo;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Verificar si generador esta activo.
     */
    public boolean estaGeneradorActivo() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar bandera
            return generadorActivo;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Tomar siguiente pedido con stock para procesar.
     */
    public Pedido tomarSiguiente() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // ordenar copia
            List<Pedido> ordenada;
            ordenada = planificador.ordenar(colaEspera);
            // recorrer ordenada
            for (Pedido candidato : ordenada) {
                // verificar estado en espera
                if (candidato.obtenerEstado() == EstadoPedido.EN_ESPERA) {
                    // verificar stock
                    boolean hayStock;
                    hayStock = verificarStockSinBloqueo(candidato);
                    if (hayStock) {
                        // remover de cola
                        colaEspera.remove(candidato);
                        // marcar en proceso
                        candidato.asignarEstado(EstadoPedido.EN_PROCESO);
                        candidato.asignarIniciadoEn(System.currentTimeMillis());
                        // guardar en mapa
                        enProceso.put(candidato.obtenerIdPedido(), candidato);
                        // retornar candidato
                        return candidato;
                    } else {
                        // marcar sin stock
                        candidato.asignarEstado(EstadoPedido.EN_ESPERA_STOCK);
                    }
                }
            }
            // retornar nulo si no hay candidato
            return null;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Finalizar pedido y liberar recursos.
     */
    public void finalizarPedido(Pedido pedido) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // descontar stock
            descontarStockSinBloqueo(pedido);
            // marcar completado
            pedido.asignarEstado(EstadoPedido.COMPLETADO);
            pedido.asignarFinalizadoEn(System.currentTimeMillis());
            pedido.asignarProgreso(100);
            pedido.asignarRestanteSegundos(0);
            pedido.asignarRecursoActual("");
            // remover de proceso
            enProceso.remove(pedido.obtenerIdPedido());
            // agregar a completados al inicio
            completados.add(0, pedido);
            // limitar a 200
            if (completados.size() > 200) {
                // remover ultimo
                completados.remove(completados.size() - 1);
            }
            // liberar recursos del pedido
            gestor.liberarTodos(pedido.obtenerIdPedido());
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Registrar llegada de mercaderia en canal.
     */
    public void registrarLlegada(String idProducto, int cantidad) {
        // crear aviso
        AvisoLlegada aviso;
        aviso = new AvisoLlegada(idProducto, cantidad);
        // publicar en canal
        canal.publicar(aviso);
    }

    /**
     * Procesar aviso desde inventario con almacen y stock.
     */
    public void procesarAviso(AvisoLlegada aviso) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // buscar producto
            Producto producto;
            producto = productos.get(aviso.obtenerIdProducto());
            // verificar existencia
            if (producto == null) {
                return;
            }
            // sumar existencias
            int nuevoStock;
            nuevoStock = producto.obtenerExistencias() + aviso.obtenerCantidad();
            producto.asignarExistencias(nuevoStock);
            // generar lote
            String lote;
            lote = GeneradorId.conPrefijo("LOTE-");
            // asignar en almacen
            almacen.asignarLote(producto.obtenerIdProducto(), lote, aviso.obtenerCantidad());
            // reevaluar espera por stock
            for (Pedido pedido : colaEspera) {
                // verificar estado
                if (pedido.obtenerEstado() == EstadoPedido.EN_ESPERA_STOCK) {
                    // verificar stock ahora
                    boolean hayStock;
                    hayStock = verificarStockSinBloqueo(pedido);
                    if (hayStock) {
                        // mover a espera normal
                        pedido.asignarEstado(EstadoPedido.EN_ESPERA);
                    }
                }
            }
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Provocar deadlock deliberado entre dos operaciones.
     */
    public BloqueoActivo provocarDeadlock() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear identificadores de demo
            String operacionA;
            operacionA = "OP-DEMO-A";
            String operacionB;
            operacionB = "OP-DEMO-B";
            // intentar tomar empaque para A sin espera
            boolean tomoA;
            tomoA = gestor.intentarAdquirir(operacionA, RecursoTipo.EMPAQUE, 0);
            // intentar tomar calidad para B sin espera
            boolean tomoB;
            tomoB = gestor.intentarAdquirir(operacionB, RecursoTipo.CALIDAD, 0);
            // crear pedidos de muestra si no existen en proceso
            if (enProceso.containsKey(operacionA) == false) {
                // crear detalle simple
                List<DetallePedido> detallesA;
                detallesA = new ArrayList<>();
                DetallePedido detalleA;
                detalleA = new DetallePedido("P-FRAGIL-01", 5);
                detallesA.add(detalleA);
                Pedido pedidoA;
                pedidoA = new Pedido(operacionA, "C-1", NivelServicio.NIVEL_1, detallesA);
                pedidoA.asignarEstado(EstadoPedido.BLOQUEADO);
                pedidoA.asignarMotivoBloqueo("Espera CALIDAD que posee OP-DEMO-B");
                pedidoA.asignarRecursoActual("EMPAQUE");
                enProceso.put(operacionA, pedidoA);
            }
            if (enProceso.containsKey(operacionB) == false) {
                // crear detalle simple
                List<DetallePedido> detallesB;
                detallesB = new ArrayList<>();
                DetallePedido detalleB;
                detalleB = new DetallePedido("P-PESADO-01", 5);
                detallesB.add(detalleB);
                Pedido pedidoB;
                pedidoB = new Pedido(operacionB, "C-2", NivelServicio.NIVEL_2, detallesB);
                pedidoB.asignarEstado(EstadoPedido.BLOQUEADO);
                pedidoB.asignarMotivoBloqueo("Espera EMPAQUE que posee OP-DEMO-A");
                pedidoB.asignarRecursoActual("CALIDAD");
                enProceso.put(operacionB, pedidoB);
            }
            // forzar registro en detector
            detector.forzarBloqueo(operacionA, operacionB, RecursoTipo.EMPAQUE, RecursoTipo.CALIDAD, "Orden inverso EMPAQUE y CALIDAD entre fragil y pesado");
            // retornar ultimo bloqueo
            List<BloqueoActivo> lista;
            lista = detector.obtenerBloqueos();
            return lista.get(lista.size() - 1);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Resolver deadlock eligiendo operacion ganadora.
     */
    public void resolverDeadlock(String ganadora) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // determinar perdedora
            String perdedora;
            perdedora = "";
            // buscar en bloqueos
            for (BloqueoActivo bloqueo : detector.obtenerBloqueos()) {
                // verificar participacion
                if (bloqueo.obtenerOperacionA().equals(ganadora)) {
                    perdedora = bloqueo.obtenerOperacionB();
                } else if (bloqueo.obtenerOperacionB().equals(ganadora)) {
                    perdedora = bloqueo.obtenerOperacionA();
                }
            }
            // verificar si hay perdedora
            if (perdedora.equals("") == false) {
                // liberar recursos de perdedora
                gestor.liberarTodos(perdedora);
                // buscar pedido perdedor
                Pedido pedidoPerdedor;
                pedidoPerdedor = enProceso.get(perdedora);
                if (pedidoPerdedor != null) {
                    // remover de proceso
                    enProceso.remove(perdedora);
                    // regresar a espera
                    pedidoPerdedor.asignarEstado(EstadoPedido.EN_ESPERA);
                    pedidoPerdedor.asignarMotivoBloqueo("");
                    pedidoPerdedor.asignarRecursoActual("");
                    // reencolar al final
                    colaEspera.add(pedidoPerdedor);
                } else {
                    // solo liberar si no hay pedido
                    gestor.liberarTodos(perdedora);
                }
            }
            // limpiar registro en detector
            detector.resolver(ganadora);
            // actualizar ganadora a en proceso
            Pedido pedidoGanador;
            pedidoGanador = enProceso.get(ganadora);
            if (pedidoGanador != null) {
                pedidoGanador.asignarEstado(EstadoPedido.EN_PROCESO);
                pedidoGanador.asignarMotivoBloqueo("");
            }
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener resumen de estadisticas.
     */
    public ResumenEstadisticas obtenerResumen() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear resumen
            ResumenEstadisticas resumen;
            resumen = new ResumenEstadisticas();
            // asignar completados
            resumen.asignarCompletados(completados.size());
            // calcular promedios
            double sumaEspera;
            sumaEspera = 0;
            double sumaProceso;
            sumaProceso = 0;
            // recorrer completados
            for (Pedido pedido : completados) {
                // calcular espera
                long espera;
                espera = pedido.obtenerIniciadoEn() - pedido.obtenerCreadoEn();
                if (espera < 0) {
                    espera = 0;
                }
                sumaEspera = sumaEspera + (espera / 1000.0);
                // calcular proceso
                long proceso;
                proceso = pedido.obtenerFinalizadoEn() - pedido.obtenerIniciadoEn();
                if (proceso < 0) {
                    proceso = 0;
                }
                sumaProceso = sumaProceso + (proceso / 1000.0);
            }
            // verificar si hay datos
            if (completados.size() > 0) {
                resumen.asignarEsperaPromedioSeg(sumaEspera / completados.size());
                resumen.asignarProcesoPromedioSeg(sumaProceso / completados.size());
            }
            // contar por estado
            int esperaCount;
            esperaCount = 0;
            // recorrer cola
            for (Pedido pedido : colaEspera) {
                // contar espera
                if (pedido.obtenerEstado() == EstadoPedido.EN_ESPERA) {
                    esperaCount = esperaCount + 1;
                } else if (pedido.obtenerEstado() == EstadoPedido.EN_ESPERA_STOCK) {
                    esperaCount = esperaCount + 1;
                }
            }
            resumen.asignarEnEspera(esperaCount);
            resumen.asignarEnProceso(enProceso.size());
            // contar bloqueados
            int bloqueados;
            bloqueados = detector.obtenerBloqueos().size();
            resumen.asignarBloqueados(bloqueados);
            // asignar politica
            resumen.asignarPoliticaActual(planificador.obtenerPolitica().name());
            resumen.asignarGeneradorActivo(generadorActivo);
            // retornar resumen
            return resumen;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener estado de recursos.
     */
    public List<InfoRecurso> obtenerRecursos() {
        // delegar a gestor
        return gestor.obtenerEstado();
    }

    /**
     * Obtener bloqueos activos.
     */
    public List<BloqueoActivo> obtenerBloqueos() {
        // delegar a detector
        return detector.obtenerBloqueos();
    }

    // verificar stock sin tomar cerrojo externo
    private boolean verificarStockSinBloqueo(Pedido pedido) {
        // recorrer detalles
        for (DetallePedido detalle : pedido.obtenerDetalles()) {
            // buscar producto
            Producto producto;
            producto = productos.get(detalle.obtenerIdProducto());
            // verificar existencia
            if (producto == null) {
                return false;
            }
            // comparar stock
            if (producto.obtenerExistencias() < detalle.obtenerCantidad()) {
                return false;
            }
        }
        // retornar verdadero
        return true;
    }

    // descontar stock sin tomar cerrojo externo
    private void descontarStockSinBloqueo(Pedido pedido) {
        // recorrer detalles
        for (DetallePedido detalle : pedido.obtenerDetalles()) {
            // buscar producto
            Producto producto;
            producto = productos.get(detalle.obtenerIdProducto());
            // verificar existencia
            if (producto != null) {
                // calcular nuevo
                int nuevo;
                nuevo = producto.obtenerExistencias() - detalle.obtenerCantidad();
                // evitar negativo
                if (nuevo < 0) {
                    nuevo = 0;
                }
                // asignar
                producto.asignarExistencias(nuevo);
            }
        }
    }
}
