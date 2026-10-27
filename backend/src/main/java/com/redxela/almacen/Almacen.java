package com.redxela.almacen;

import com.redxela.config.ConfiguracionSistema;
import com.redxela.model.Ubicacion;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// gestionar ubicaciones fisicas con asignacion no contigua
public class Almacen {

    // declarar lista de ubicaciones
    private final List<Ubicacion> ubicaciones;

    // declarar cerrojo para exclusion mutua
    private final ReentrantLock cerrojo;

    // construir almacen con capacidad configurada
    public Almacen() {
        // crear lista vacia
        ubicaciones = new ArrayList<>();
        // crear cerrojo
        cerrojo = new ReentrantLock();
        // invocar inicializacion
        inicializar();
    }

    // inicializar ubicaciones por pasillos y niveles
    private void inicializar() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // limpiar lista
            ubicaciones.clear();
            // definir pasillos base
            int total;
            total = ConfiguracionSistema.CAPACIDAD_ALMACEN;
            // recorrer indices
            for (int indice = 0; indice < total; indice++) {
                // calcular pasillo
                int pasillo;
                pasillo = (indice / 20) + 1;
                // calcular nivel
                int nivel;
                nivel = (indice % 20) + 1;
                // crear identificador
                String id;
                id = "U-" + pasillo + "-" + nivel;
                // crear ubicacion
                Ubicacion ubicacion;
                ubicacion = new Ubicacion(id, pasillo, nivel);
                // agregar a lista
                ubicaciones.add(ubicacion);
            }
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Asignar lote en ubicaciones libres no contiguas.
     */
    public List<Ubicacion> asignarLote(String idProducto, String lote, int cantidad) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear lista de asignadas
            List<Ubicacion> asignadas;
            asignadas = new ArrayList<>();
            // calcular por ubicacion una unidad por simplicidad de reparto
            int restante;
            restante = cantidad;
            // recorrer con salto para forzar no contiguidad
            int salto;
            salto = 7;
            int inicio;
            inicio = (int) (System.currentTimeMillis() % ubicaciones.size());
            // buscar libres
            for (int paso = 0; paso < ubicaciones.size(); paso++) {
                // verificar si ya termino
                if (restante <= 0) {
                    break;
                }
                // calcular indice salteado
                int indice;
                indice = (inicio + paso * salto) % ubicaciones.size();
                // obtener ubicacion
                Ubicacion actual;
                actual = ubicaciones.get(indice);
                // verificar si esta libre
                if (actual.estaOcupada() == false) {
                    // ocupar ubicacion
                    actual.asignarOcupada(true);
                    actual.asignarIdProducto(idProducto);
                    actual.asignarLote(lote);
                    actual.asignarCantidad(1);
                    // agregar a asignadas
                    asignadas.add(actual);
                    // descontar restante
                    restante = restante - 1;
                }
            }
            // retornar asignadas
            return asignadas;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener pagina de ubicaciones para consulta.
     */
    public List<Ubicacion> obtenerPagina(int pagina, int tamano) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear resultado
            List<Ubicacion> resultado;
            resultado = new ArrayList<>();
            // calcular inicio
            int inicio;
            inicio = pagina * tamano;
            // calcular fin
            int fin;
            fin = inicio + tamano;
            // ajustar fin
            if (fin > ubicaciones.size()) {
                fin = ubicaciones.size();
            }
            // verificar rango valido
            if (inicio < ubicaciones.size()) {
                // recorrer rango
                for (int i = inicio; i < fin; i++) {
                    // agregar copia de referencia
                    resultado.add(ubicaciones.get(i));
                }
            }
            // retornar resultado
            return resultado;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Contar ubicaciones libres.
     */
    public int contarLibres() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // iniciar contador
            int libres;
            libres = 0;
            // recorrer ubicaciones
            for (Ubicacion ubicacion : ubicaciones) {
                // verificar si esta libre
                if (ubicacion.estaOcupada() == false) {
                    // incrementar
                    libres = libres + 1;
                }
            }
            // retornar conteo
            return libres;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Contar ubicaciones ocupadas.
     */
    public int contarOcupadas() {
        // calcular total menos libres
        int libres;
        libres = contarLibres();
        // retornar diferencia
        return ubicaciones.size() - libres;
    }

    /**
     * Obtener capacidad total.
     */
    public int obtenerCapacidad() {
        // retornar tamano
        return ubicaciones.size();
    }

    /**
     * Liberar ubicaciones de un producto al consumir stock.
     */
    public int liberarPorProducto(String idProducto, int cantidad) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // iniciar liberadas
            int liberadas;
            liberadas = 0;
            // recorrer ubicaciones
            for (Ubicacion ubicacion : ubicaciones) {
                // verificar si ya termino
                if (liberadas >= cantidad) {
                    break;
                }
                // verificar si esta ocupada con ese producto
                if (ubicacion.estaOcupada()) {
                    if (ubicacion.obtenerIdProducto().equals(idProducto)) {
                        // liberar ubicacion
                        ubicacion.asignarOcupada(false);
                        ubicacion.asignarIdProducto("");
                        ubicacion.asignarLote("");
                        ubicacion.asignarCantidad(0);
                        // incrementar
                        liberadas = liberadas + 1;
                    }
                }
            }
            // retornar liberadas
            return liberadas;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }
}
