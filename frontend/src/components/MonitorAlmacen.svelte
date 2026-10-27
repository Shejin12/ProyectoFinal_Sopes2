<script>
  // importar api
  import { obtenerAlmacen } from "../services/api.js";
  import { onMount } from "svelte";

  // declarar datos
  let pagina = $state(0);
  let tamano = $state(20);
  let capacidad = $state(0);
  let libres = $state(0);
  let ocupadas = $state(0);
  let ubicaciones = $state([]);

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
  onMount(() => {
    // cargar de inmediato
    cargar();
    // repetir cada 2 segundos
    let intervalo = setInterval(cargar, 2000);
    // limpiar al desmontar
    return () => clearInterval(intervalo);
  });

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
      <button class="btn btn-sm btn-secondary" onclick={anterior}>Anterior</button>
      <button class="btn btn-sm btn-secondary" onclick={siguiente}>Siguiente</button>
      <button class="btn btn-sm btn-outline-secondary" onclick={cargar}>Recargar</button>
    </div>
  </div>
</div>
