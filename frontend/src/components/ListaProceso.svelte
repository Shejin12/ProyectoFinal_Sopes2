<script>
  // importar estado
  import { proceso } from "../stores/estado.js";
</script>

<!-- mostrar pedidos en proceso con progreso -->
<div class="card mb-3">
  <div class="card-header bg-primary text-white">En proceso</div>
  <div class="card-body p-0">
    <table class="table table-sm mb-0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Recurso</th>
          <th>Progreso</th>
          <th>Restante</th>
        </tr>
      </thead>
      <tbody>
        {#each $proceso as pedido}
          <tr>
            <td>{pedido.idPedido}</td>
            <td>{pedido.recursoActual}</td>
            <td style="min-width: 140px">
              <div class="progress">
                <div class="progress-bar" style="width: {pedido.progreso}%">{pedido.progreso}%</div>
              </div>
            </td>
            <td>{pedido.restanteSegundos} s</td>
          </tr>
        {/each}
        {#if $proceso.length === 0}
          <tr><td colspan="4" class="text-center text-muted">Sin pedidos en proceso</td></tr>
        {/if}
      </tbody>
    </table>
  </div>
</div>
