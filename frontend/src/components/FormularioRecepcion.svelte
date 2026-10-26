<script>
  // importar api
  import { registrarRecepcion, obtenerProductos } from "../services/api.js";
  import { onMount } from "svelte";

  // declarar datos
  let productos = [];
  let idProducto = "";
  let cantidad = 10;
  let mensaje = "";

  // cargar productos
  onMount(async () => {
    // obtener lista
    productos = await obtenerProductos();
    // preseleccionar
    if (productos.length > 0) {
      idProducto = productos[0].idProducto;
    }
  });

  // enviar recepcion
  async function enviarRecepcion() {
    // validar
    if (idProducto === "") {
      mensaje = "Seleccionar producto";
      return;
    }
    // registrar
    await registrarRecepcion(idProducto, Number(cantidad));
    // confirmar
    mensaje = "Mercaderia registrada en recepcion";
  }
</script>

<!-- mostrar recepcion de mercaderia -->
<div class="card mb-3">
  <div class="card-header bg-info">Recepcion de mercaderia</div>
  <div class="card-body">
    <label class="form-label" for="selRec">Producto</label>
    <select id="selRec" class="form-select mb-2" bind:value={idProducto}>
      {#each productos as producto}
        <option value={producto.idProducto}>{producto.idProducto} - {producto.nombre}</option>
      {/each}
    </select>
    <label class="form-label" for="inpRec">Cantidad recibida</label>
    <input id="inpRec" type="number" min="1" max="200" class="form-control mb-2" bind:value={cantidad} />
    <button class="btn btn-info w-100" on:click={enviarRecepcion}>Registrar llegada</button>
    {#if mensaje !== ""}
      <div class="alert alert-info mt-2 mb-0">{mensaje}</div>
    {/if}
  </div>
</div>
