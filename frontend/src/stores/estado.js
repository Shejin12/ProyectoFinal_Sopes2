import { writable } from "svelte/store";

// crear almacen de estado global
export const recursos = writable([]);
export const cola = writable([]);
export const proceso = writable([]);
export const completados = writable([]);
export const bloqueos = writable([]);
export const estadisticas = writable(null);
export const politica = writable("PRIORIDAD_ESTRICTA");

// actualizar todo el estado desde objeto estado
export function aplicarEstado(estado) {
  // verificar objeto valido
  if (!estado) {
    return;
  }
  // actualizar recursos
  if (estado.recursos) {
    recursos.set(estado.recursos);
  }
  // actualizar cola
  if (estado.cola) {
    cola.set(estado.cola);
  }
  // actualizar proceso
  if (estado.proceso) {
    proceso.set(estado.proceso);
  }
  // actualizar completados
  if (estado.completados) {
    completados.set(estado.completados);
  }
  // actualizar bloqueos
  if (estado.bloqueos) {
    bloqueos.set(estado.bloqueos);
  }
  // actualizar estadisticas
  if (estado.estadisticas) {
    estadisticas.set(estado.estadisticas);
  }
  // actualizar politica
  if (estado.politica) {
    politica.set(estado.politica);
  }
}
