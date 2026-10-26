<script>
  // importar estado global
  import { recursos } from "../stores/estado.js";
</script>

<!-- mostrar tarjeta de recursos compartidos -->
<div class="card mb-3">
  <!-- mostrar encabezado -->
  <div class="card-header bg-dark text-white">Recursos compartidos</div>
  <!-- mostrar cuerpo -->
  <div class="card-body p-0">
    <!-- mostrar tabla -->
    <table class="table table-sm table-striped mb-0">
      <!-- mostrar encabezados -->
      <thead>
        <tr>
          <th>Recurso</th>
          <th>Total</th>
          <th>Libres</th>
          <th>En uso por</th>
        </tr>
      </thead>
      <!-- mostrar filas -->
      <tbody>
        {#each $recursos as recurso}
          <tr>
            <td>{recurso.tipo}</td>
            <td>{recurso.total}</td>
            <td>{recurso.disponibles}</td>
            <td>
              {#each recurso.duenos as dueno}
                <span class="badge bg-primary me-1">{dueno}</span>
              {/each}
              {#if recurso.duenos.length === 0}
                <span class="text-muted">libre</span>
              {/if}
            </td>
          </tr>
        {/each}
      </tbody>
    </table>
  </div>
</div>
