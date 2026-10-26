<script>
  // importar api
  import { obtenerAlmacen } from "../services/api.js";
  import { onMount } from "svelte";

  // declarar datos
  let pagina = 0;
  let tamano = 20;
  let capacidad = 0;
  let libres = 0;
  let ocupadas = 0;
  let ubicaciones = [];

  // cargar pagina
  async function cargar() {
    // pedir almacen
    let datos = await obtenerAlmacen(pagina, tamano);
    // asignar valores
    capacidad = datos.capacidad;
    libres = datos.libres;
    ocupadas = datos.ocupadas;
    ubicaciones = datos.ubicaciones;
  }

  // cargar al iniciar
  onMount(cargar);

  // ir a anterior
  function anterior() {
    // verificar limite
    if (pagina > 0) {
      pagina = pagina - 1;
      cargar();
    }
  }

  // ir a siguiente
  function siguiente() {
    // avanzar
    pagina = pagina + 1;
    cargar();
  }
</script>

<!-- mostrar almacen paginado -->
<div class="card mb-3">
  <div class="card-header bg-dark text-white">Almacen no contiguo</div>
  <div class="card-body">
    <p class="mb-2">Capacidad: {capacidad} Libres: {libres} Ocupadas: {ocupadas}</p>
    <table class="table table-sm">
      <thead>
        <tr><th>Ubicacion</th><th>Producto</th><th>Lote</th></tr>
      </thead>
      <tbody>
        {#each ubicaciones as ubicacion}
          <tr>
            <td>{ubicacion.idUbicacion}</td>
            <td>{ubicacion.idProducto}</td>
            <td>{ubicacion.lote}</td>
          </tr>
        {/each}
      </tbody>
    </table>
    <div class="d-flex gap-2">
      <button class="btn btn-sm btn-secondary" on:click={anterior}>Anterior</button>
      <button class="btn btn-sm btn-secondary" on:click={siguiente}>Siguiente</button>
      <button class="btn btn-sm btn-outline-secondary" on:click={cargar}>Recargar</button>
    </div>
  </div>
</div>
