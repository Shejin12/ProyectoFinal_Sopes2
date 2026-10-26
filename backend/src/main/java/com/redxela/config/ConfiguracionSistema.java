package com.redxela.config;

// definir constantes globales de la simulacion
public class ConfiguracionSistema {

    // declarar cantidad de estaciones de empaque
    public static final int TOTAL_EMPAQUE = 6;

    // declarar cantidad de areas de carga
    public static final int TOTAL_CARGA = 2;

    // declarar cantidad de encargados de bodega
    public static final int TOTAL_BODEGA = 10;

    // declarar cantidad de montacargas
    public static final int TOTAL_MONTACARGAS = 3;

    // declarar cantidad de estaciones de calidad
    public static final int TOTAL_CALIDAD = 4;

    // declarar cantidad de sistemas de escaneo
    public static final int TOTAL_ESCANEO = 1;

    // declarar capacidad total del almacen en ubicaciones
    public static final int CAPACIDAD_ALMACEN = 200;

    // declarar tamano de pagina para consulta de almacen
    public static final int PAGINA_ALMACEN = 20;

    // declarar puerto del servidor
    public static final int PUERTO_SERVIDOR = 7000;

    // declarar tiempo base por unidad en milisegundos
    public static final int MS_POR_UNIDAD = 800;

    // declarar intervalo de difusion por websocket en milisegundos
    public static final int MS_DIFUSION = 1000;

    // declarar pausa del generador automatico en milisegundos
    public static final int MS_GENERADOR_MIN = 2000;

    // declarar pausa maxima del generador automatico en milisegundos
    public static final int MS_GENERADOR_MAX = 6000;

    // declarar tiempo maximo de espera por nivel en minutos
    public static final int ESPERA_NIVEL_1 = 1;
    public static final int ESPERA_NIVEL_2 = 10;
    public static final int ESPERA_NIVEL_3 = 30;
    public static final int ESPERA_NIVEL_4 = 60;
    public static final int ESPERA_NIVEL_5 = 120;

    // declarar constructor privado para evitar instanciacion
    private ConfiguracionSistema() {
    }
}
