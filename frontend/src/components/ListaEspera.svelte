<script>
  // importar estado y formato
  import { cola } from "../stores/estado.js";
  import { claseNivel } from "../utils/formato.js";
</script>

<!-- mostrar lista de espera ordenada -->
<div class="card mb-3">
  <!-- mostrar encabezado -->
  <div class="card-header bg-secondary text-white">Lista de espera</div>
  <!-- mostrar cuerpo -->
  <div class="card-body p-0">
    <!-- mostrar tabla -->
    <table class="table table-sm mb-0">
      <thead>
        <tr>
          <th>ID</th>
          <th>Cliente</th>
          <th>Nivel</th>
          <th>Articulos</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        {#each $cola as pedido}
          <tr>
            <td>{pedido.idPedido}</td>
            <td>{pedido.idCliente}</td>
            <td><span class="badge {claseNivel(pedido.nivel)}">{pedido.nivel}</span></td>
            <td>{pedido.detalles.length}</td>
            <td>{pedido.estado}</td>
          </tr>
        {/each}
        {#if $cola.length === 0}
          <tr><td colspan="5" class="text-center text-muted">Sin pedidos en espera</td></tr>
        {/if}
      </tbody>
    </table>
  </div>
</div>
