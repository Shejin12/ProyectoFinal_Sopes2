<script>
  // importar estado y api
  import { bloqueos } from "../stores/estado.js";
  import { provocarDeadlock, resolverDeadlock, obtenerEstado } from "../services/api.js";
  import { aplicarEstado } from "../stores/estado.js";

  // provocar bloqueo deliberado
  async function forzar() {
    // invocar backend
    await provocarDeadlock();
    // recargar estado de inmediato
    let estado = await obtenerEstado();
    // aplicar a stores
    aplicarEstado(estado);
  }

  // resolver con ganadora
  async function resolver(ganadora) {
    // invocar backend
    await resolverDeadlock(ganadora);
    // recargar estado de inmediato
    let estado = await obtenerEstado();
    // aplicar a stores
    aplicarEstado(estado);
  }
</script>

<!-- mostrar panel de deadlock -->
<div class="card mb-3 border-danger">
  <div class="card-header bg-danger text-white">Conflicto de recursos</div>
  <div class="card-body">
    <button class="btn btn-outline-danger w-100 mb-3" on:click={forzar}>Provocar deadlock de prueba</button>
    {#if $bloqueos.length === 0}
      <p class="text-muted mb-0">Sin bloqueos detectados</p>
    {/if}
    {#each $bloqueos as bloqueo}
      <div class="alert alert-danger">
        <p class="mb-1"><strong>Bloqueo:</strong> {bloqueo.operacionA} contra {bloqueo.operacionB}</p>
        <p class="mb-1"><strong>Motivo:</strong> {bloqueo.motivo}</p>
        <p class="mb-2"><strong>Recursos:</strong> {bloqueo.recursos.join(", ")}</p>
        <div class="d-flex gap-2">
          <button class="btn btn-sm btn-danger" on:click={() => resolver(bloqueo.operacionA)}>Continuar {bloqueo.operacionA}</button>
          <button class="btn btn-sm btn-secondary" on:click={() => resolver(bloqueo.operacionB)}>Continuar {bloqueo.operacionB}</button>
        </div>
      </div>
    {/each}
  </div>
</div>
