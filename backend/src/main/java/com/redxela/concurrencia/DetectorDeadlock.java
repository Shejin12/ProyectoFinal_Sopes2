package com.redxela.concurrencia;

import com.redxela.model.BloqueoActivo;
import com.redxela.model.RecursoTipo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

// detectar bloqueos por espera circular entre operaciones
public class DetectorDeadlock {

    // declarar gestor de recursos
    private final GestorRecursos gestor;

    // declarar lista de bloqueos activos
    private final List<BloqueoActivo> bloqueos;

    // construir detector con gestor
    public DetectorDeadlock(GestorRecursos gestor) {
        // asignar gestor
        this.gestor = gestor;
        // crear lista segura
        this.bloqueos = new CopyOnWriteArrayList<>();
    }

    /**
     * Verificar espera circular entre dos operaciones.
     */
    public void verificar() {
        // obtener copias
        Map<String, List<RecursoTipo>> asignados;
        asignados = gestor.obtenerAsignadosCopia();
        Map<String, RecursoTipo> esperas;
        esperas = gestor.obtenerEsperasCopia();
        // recorrer pares de esperas
        for (Map.Entry<String, RecursoTipo> entradaA : esperas.entrySet()) {
            // obtener operacion A y recurso que espera
            String operacionA;
            operacionA = entradaA.getKey();
            RecursoTipo esperaA;
            esperaA = entradaA.getValue();
            // recorrer segunda operacion
            for (Map.Entry<String, RecursoTipo> entradaB : esperas.entrySet()) {
                // obtener operacion B
                String operacionB;
                operacionB = entradaB.getKey();
                // evitar comparar consigo misma
                if (operacionA.equals(operacionB)) {
                    continue;
                }
                // obtener recurso que espera B
                RecursoTipo esperaB;
                esperaB = entradaB.getValue();
                // obtener asignados de A y B
                List<RecursoTipo> tieneA;
                tieneA = asignados.get(operacionA);
                List<RecursoTipo> tieneB;
                tieneB = asignados.get(operacionB);
                // verificar nulos
                if (tieneA == null) {
                    continue;
                }
                if (tieneB == null) {
                    continue;
                }
                // verificar condicion circular A espera lo que B tiene y viceversa
                boolean aEsperaDeB;
                aEsperaDeB = tieneB.contains(esperaA);
                boolean bEsperaDeA;
                bEsperaDeA = tieneA.contains(esperaB);
                if (aEsperaDeB && bEsperaDeA) {
                    // registrar bloqueo si no existe
                    registrarSiNuevo(operacionA, operacionB, esperaA, esperaB);
                }
            }
        }
    }

    /**
     * Obtener lista de bloqueos activos.
     */
    public List<BloqueoActivo> obtenerBloqueos() {
        // crear copia
        List<BloqueoActivo> copia;
        copia = new ArrayList<>(bloqueos);
        // retornar copia
        return copia;
    }

    /**
     * Limpiar bloqueos que ya no aplican.
     */
    public void limpiarResueltos() {
        // obtener esperas actuales
        Map<String, RecursoTipo> esperas;
        esperas = gestor.obtenerEsperasCopia();
        // crear lista a remover
        List<BloqueoActivo> remover;
        remover = new ArrayList<>();
        // recorrer bloqueos
        for (BloqueoActivo bloqueo : bloqueos) {
            // verificar si ambas operaciones siguen esperando
            boolean sigueA;
            sigueA = esperas.containsKey(bloqueo.obtenerOperacionA());
            boolean sigueB;
            sigueB = esperas.containsKey(bloqueo.obtenerOperacionB());
            // decidir si remover
            if (sigueA == false) {
                remover.add(bloqueo);
            } else if (sigueB == false) {
                remover.add(bloqueo);
            }
        }
        // remover resueltos
        bloqueos.removeAll(remover);
    }

    /**
     * Forzar registro manual de bloqueo para demo.
     */
    public void forzarBloqueo(String operacionA, String operacionB, RecursoTipo recursoA, RecursoTipo recursoB, String motivo) {
        // crear lista de recursos
        List<RecursoTipo> recursos;
        recursos = new ArrayList<>();
        // agregar recursos
        recursos.add(recursoA);
        recursos.add(recursoB);
        // crear bloqueo
        BloqueoActivo bloqueo;
        bloqueo = new BloqueoActivo(operacionA, operacionB, recursos, motivo);
        // agregar a lista
        bloqueos.add(bloqueo);
    }

    /**
     * Eliminar bloqueo por operacion ganadora.
     */
    public void resolver(String operacionGanadora) {
        // crear lista a remover
        List<BloqueoActivo> remover;
        remover = new ArrayList<>();
        // recorrer bloqueos
        for (BloqueoActivo bloqueo : bloqueos) {
            // verificar si participa ganadora
            if (bloqueo.obtenerOperacionA().equals(operacionGanadora)) {
                remover.add(bloqueo);
            } else if (bloqueo.obtenerOperacionB().equals(operacionGanadora)) {
                remover.add(bloqueo);
            }
        }
        // remover de lista
        bloqueos.removeAll(remover);
    }

    // registrar bloqueo solo si no existe ya
    private void registrarSiNuevo(String operacionA, String operacionB, RecursoTipo esperaA, RecursoTipo esperaB) {
        // recorrer existentes
        for (BloqueoActivo existente : bloqueos) {
            // verificar mismo par en cualquier orden
            boolean mismoPar;
            mismoPar = false;
            if (existente.obtenerOperacionA().equals(operacionA)) {
                if (existente.obtenerOperacionB().equals(operacionB)) {
                    mismoPar = true;
                }
            }
            if (existente.obtenerOperacionA().equals(operacionB)) {
                if (existente.obtenerOperacionB().equals(operacionA)) {
                    mismoPar = true;
                }
            }
            // omitir si ya existe
            if (mismoPar) {
                return;
            }
        }
        // crear lista de recursos
        List<RecursoTipo> recursos;
        recursos = new ArrayList<>();
        // agregar recursos
        recursos.add(esperaA);
        recursos.add(esperaB);
        // crear motivo
        String motivo;
        motivo = "Espera circular entre " + operacionA + " y " + operacionB;
        // crear bloqueo
        BloqueoActivo nuevo;
        nuevo = new BloqueoActivo(operacionA, operacionB, recursos, motivo);
        // agregar
        bloqueos.add(nuevo);
    }
}
