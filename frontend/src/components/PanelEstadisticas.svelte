<script>
  // importar estado
  import { estadisticas, politica } from "../stores/estado.js";
  import { cambiarPolitica, cambiarGenerador } from "../services/api.js";

  // cambiar politica seleccionada
  async function seleccionarPolitica(evento) {
    // obtener valor
    let valor = evento.target.value;
    // enviar al backend
    await cambiarPolitica(valor);
  }

  // alternar generador
  async function alternarGenerador(evento) {
    // obtener estado del check
    let activo = evento.target.checked;
    // enviar al backend
    await cambiarGenerador(activo);
  }
</script>

<!-- mostrar estadisticas generales -->
<div class="card mb-3">
  <div class="card-header bg-success text-white">Estadisticas y control</div>
  <div class="card-body">
    {#if $estadisticas}
      <p class="mb-1">Completados: <strong>{$estadisticas.completados}</strong></p>
      <p class="mb-1">En espera: <strong>{$estadisticas.enEspera}</strong></p>
      <p class="mb-1">En proceso: <strong>{$estadisticas.enProceso}</strong></p>
      <p class="mb-1">Bloqueados: <strong>{$estadisticas.bloqueados}</strong></p>
      <p class="mb-1">Espera promedio: <strong>{$estadisticas.esperaPromedioSeg} s</strong></p>
      <p class="mb-3">Proceso promedio: <strong>{$estadisticas.procesoPromedioSeg} s</strong></p>
    {/if}
    <label class="form-label" for="selPolitica">Politica de atencion</label>
    <select id="selPolitica" class="form-select mb-3" value={$politica} on:change={seleccionarPolitica}>
      <option value="PRIORIDAD_ESTRICTA">Prioridad estricta por nivel</option>
      <option value="ENVEJECIMIENTO">Envejecimiento sin inanicion</option>
      <option value="SJF">Trabajo mas corto primero</option>
    </select>
    <div class="form-check">
      <input class="form-check-input" type="checkbox" id="chkGen" checked on:change={alternarGenerador} />
      <label class="form-check-label" for="chkGen">Generador automatico activo</label>
    </div>
  </div>
</div>
