package com.redxela.concurrencia;

import com.redxela.config.ConfiguracionSistema;
import com.redxela.model.InfoRecurso;
import com.redxela.model.RecursoTipo;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// administrar recursos compartidos con semaforos y exclusion mutua
public class GestorRecursos {

    // declarar semaforos por tipo
    private final Map<RecursoTipo, Semaphore> semaforos;

    // declarar totales por tipo
    private final Map<RecursoTipo, Integer> totales;

    // declarar duenos por tipo
    private final Map<RecursoTipo, List<String>> duenos;

    // declarar recursos asignados por operacion
    private final Map<String, List<RecursoTipo>> asignados;

    // declarar espera actual por operacion
    private final Map<String, RecursoTipo> esperando;

    // declarar cerrojo para estado interno
    private final ReentrantLock cerrojo;

    // construir gestor con capacidades configuradas
    public GestorRecursos() {
        // crear mapas
        semaforos = new EnumMap<>(RecursoTipo.class);
        totales = new EnumMap<>(RecursoTipo.class);
        duenos = new EnumMap<>(RecursoTipo.class);
        asignados = new HashMap<>();
        esperando = new HashMap<>();
        cerrojo = new ReentrantLock();
        // invocar carga inicial
        cargarTotales();
    }

    // cargar totales desde configuracion
    private void cargarTotales() {
        // registrar empaque
        totales.put(RecursoTipo.EMPAQUE, ConfiguracionSistema.TOTAL_EMPAQUE);
        totales.put(RecursoTipo.CARGA, ConfiguracionSistema.TOTAL_CARGA);
        totales.put(RecursoTipo.BODEGA, ConfiguracionSistema.TOTAL_BODEGA);
        totales.put(RecursoTipo.MONTACARGAS, ConfiguracionSistema.TOTAL_MONTACARGAS);
        totales.put(RecursoTipo.CALIDAD, ConfiguracionSistema.TOTAL_CALIDAD);
        totales.put(RecursoTipo.ESCANEO, ConfiguracionSistema.TOTAL_ESCANEO);
        // crear semaforos y listas
        for (RecursoTipo tipo : RecursoTipo.values()) {
            // obtener total
            int total;
            total = totales.get(tipo);
            // crear semaforo justo
            Semaphore semaforo;
            semaforo = new Semaphore(total, true);
            // guardar semaforo
            semaforos.put(tipo, semaforo);
            // crear lista de duenos
            duenos.put(tipo, new ArrayList<>());
        }
    }

    /**
     * Intentar adquirir recurso con espera limitada.
     */
    public boolean intentarAdquirir(String operacionId, RecursoTipo tipo, long esperaMs) {
        // marcar espera actual
        marcarEspera(operacionId, tipo);
        // declarar adquirido
        boolean adquirido;
        adquirido = false;
        // intentar adquirir semaforo
        try {
            // ejecutar intento con tiempo
            adquirido = semaforos.get(tipo).tryAcquire(esperaMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException error) {
            // restaurar bandera
            Thread.currentThread().interrupt();
            // marcar no adquirido
            adquirido = false;
        }
        // verificar resultado
        if (adquirido) {
            // registrar asignacion
            registrarAsignacion(operacionId, tipo);
        } else {
            // limpiar espera si fallo
            limpiarEspera(operacionId);
        }
        // retornar resultado
        return adquirido;
    }

    /**
     * Adquirir recurso de forma bloqueante.
     */
    public void adquirirBloqueante(String operacionId, RecursoTipo tipo) {
        // marcar espera actual
        marcarEspera(operacionId, tipo);
        // intentar adquirir
        try {
            // ejecutar adquisicion
            semaforos.get(tipo).acquire();
            // registrar asignacion
            registrarAsignacion(operacionId, tipo);
        } catch (InterruptedException error) {
            // restaurar bandera
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Liberar un recurso de una operacion.
     */
    public void liberar(String operacionId, RecursoTipo tipo) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // obtener lista de la operacion
            List<RecursoTipo> lista;
            lista = asignados.get(operacionId);
            // verificar existencia
            if (lista != null) {
                // remover tipo
                if (lista.contains(tipo)) {
                    // quitar de lista
                    lista.remove(tipo);
                    // quitar de duenos
                    duenos.get(tipo).remove(operacionId);
                    // liberar permiso
                    semaforos.get(tipo).release();
                }
            }
            // limpiar espera
            esperando.remove(operacionId);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Liberar todos los recursos de una operacion.
     */
    public void liberarTodos(String operacionId) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // obtener lista
            List<RecursoTipo> lista;
            lista = asignados.get(operacionId);
            // verificar existencia
            if (lista != null) {
                // copiar para iterar
                List<RecursoTipo> copia;
                copia = new ArrayList<>(lista);
                // recorrer copia
                for (RecursoTipo tipo : copia) {
                    // quitar de duenos
                    duenos.get(tipo).remove(operacionId);
                    // liberar permiso
                    semaforos.get(tipo).release();
                }
                // limpiar lista
                lista.clear();
            }
            // limpiar espera
            esperando.remove(operacionId);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener estado actual de todos los recursos.
     */
    public List<InfoRecurso> obtenerEstado() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear resultado
            List<InfoRecurso> resultado;
            resultado = new ArrayList<>();
            // recorrer tipos
            for (RecursoTipo tipo : RecursoTipo.values()) {
                // obtener total
                int total;
                total = totales.get(tipo);
                // obtener disponibles
                int disponibles;
                disponibles = semaforos.get(tipo).availablePermits();
                // copiar duenos
                List<String> copiaDuenos;
                copiaDuenos = new ArrayList<>(duenos.get(tipo));
                // crear info
                InfoRecurso info;
                info = new InfoRecurso(tipo, total, disponibles, copiaDuenos);
                // agregar
                resultado.add(info);
            }
            // retornar resultado
            return resultado;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener mapa de asignados para detector.
     */
    public Map<String, List<RecursoTipo>> obtenerAsignadosCopia() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // crear copia
            Map<String, List<RecursoTipo>> copia;
            copia = new HashMap<>();
            // recorrer entradas
            for (Map.Entry<String, List<RecursoTipo>> entrada : asignados.entrySet()) {
                // copiar lista
                copia.put(entrada.getKey(), new ArrayList<>(entrada.getValue()));
            }
            // retornar copia
            return copia;
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Obtener mapa de esperas para detector.
     */
    public Map<String, RecursoTipo> obtenerEsperasCopia() {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // retornar copia
            return new HashMap<>(esperando);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    // marcar operacion en espera de un recurso
    private void marcarEspera(String operacionId, RecursoTipo tipo) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // registrar espera
            esperando.put(operacionId, tipo);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    // limpiar espera de una operacion
    private void limpiarEspera(String operacionId) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // remover espera
            esperando.remove(operacionId);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    // registrar asignacion exitosa
    private void registrarAsignacion(String operacionId, RecursoTipo tipo) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // obtener lista
            List<RecursoTipo> lista;
            lista = asignados.get(operacionId);
            // crear si no existe
            if (lista == null) {
                // crear lista nueva
                lista = new ArrayList<>();
                // guardar
                asignados.put(operacionId, lista);
            }
            // agregar tipo
            lista.add(tipo);
            // agregar dueno
            duenos.get(tipo).add(operacionId);
            // limpiar espera
            esperando.remove(operacionId);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }

    /**
     * Forzar marca de espera para demo de deadlock.
     */
    public void forzarEspera(String operacionId, RecursoTipo tipo) {
        // tomar cerrojo
        cerrojo.lock();
        try {
            // registrar espera manual
            esperando.put(operacionId, tipo);
        } finally {
            // liberar cerrojo
            cerrojo.unlock();
        }
    }
}
