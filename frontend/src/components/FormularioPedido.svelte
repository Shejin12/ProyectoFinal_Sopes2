<script>
  // importar api
  import { crearPedido, crearCliente, obtenerClientes, obtenerProductos } from "../services/api.js";
  import { onMount } from "svelte";

  // declarar listas
  let clientes = $state([]);
  let productos = $state([]);
  // declarar seleccion
  let idCliente = $state("");
  let nivel = $state(3);
  let idProducto = $state("");
  let cantidad = $state(1);
  // declarar nombre para cliente nuevo
  let nombreNuevo = $state("");
  let nivelNuevo = $state(3);
  let mensaje = $state("");

  // cargar datos al iniciar
  onMount(async () => {
    // obtener clientes
    clientes = await obtenerClientes();
    // obtener productos
    productos = await obtenerProductos();
    // preseleccionar primero
    if (clientes.length > 0) {
      idCliente = clientes[0].idCliente;
    }
    if (productos.length > 0) {
      idProducto = productos[0].idProducto;
    }
  });

  // enviar pedido manual
  async function enviarPedido() {
    // validar seleccion
    if (idCliente === "" || idProducto === "") {
      mensaje = "Seleccionar cliente y producto";
      return;
    }
    // crear detalle
    let detalles = [{ idProducto, cantidad: Number(cantidad) }];
    // enviar
    await crearPedido(idCliente, Number(nivel), detalles);
    // limpiar mensaje
    mensaje = "Pedido registrado";
  }

  // crear cuenta de cliente
  async function registrarCliente() {
    // validar nombre
    if (nombreNuevo === "") {
      mensaje = "Ingresar nombre de cliente";
      return;
    }
    // crear cliente
    let creado = await crearCliente(nombreNuevo, Number(nivelNuevo));
    // recargar lista
    clientes = await obtenerClientes();
    // seleccionar nuevo
    idCliente = creado.idCliente;
    // limpiar
    nombreNuevo = "";
    mensaje = "Cliente creado";
  }
</script>

<!-- mostrar formularios de pedido y cliente -->
<div class="card mb-3">
  <div class="card-header bg-warning">Ingreso manual</div>
  <div class="card-body">
    <h6>Crear pedido</h6>
    <label class="form-label" for="selCli">Cliente</label>
    <select id="selCli" class="form-select mb-2" bind:value={idCliente}>
      {#each clientes as cliente}
        <option value={cliente.idCliente}>{cliente.idCliente} - {cliente.nombre}</option>
      {/each}
    </select>
    <label class="form-label" for="selNiv">Nivel</label>
    <select id="selNiv" class="form-select mb-2" bind:value={nivel}>
      <option value={1}>1 Expres</option>
      <option value={2}>2 Prioritario</option>
      <option value={3}>3 Estandar</option>
      <option value={4}>4 Programado</option>
      <option value={5}>5 Economico</option>
    </select>
    <label class="form-label" for="selProd">Producto</label>
    <select id="selProd" class="form-select mb-2" bind:value={idProducto}>
      {#each productos as producto}
        <option value={producto.idProducto}>{producto.idProducto} - {producto.nombre}</option>
      {/each}
    </select>
    <label class="form-label" for="inpCant">Cantidad</label>
    <input id="inpCant" type="number" min="1" max="50" class="form-control mb-2" bind:value={cantidad} />
    <button class="btn btn-primary w-100 mb-3" onclick={enviarPedido}>Registrar pedido</button>
    <h6>Crear cliente</h6>
    <input class="form-control mb-2" placeholder="Nombre de cliente" bind:value={nombreNuevo} />
    <select class="form-select mb-2" bind:value={nivelNuevo}>
      <option value={1}>1 Expres</option>
      <option value={2}>2 Prioritario</option>
      <option value={3}>3 Estandar</option>
      <option value={4}>4 Programado</option>
      <option value={5}>5 Economico</option>
    </select>
    <button class="btn btn-secondary w-100" onclick={registrarCliente}>Crear cuenta</button>
    {#if mensaje !== ""}
      <div class="alert alert-info mt-2 mb-0">{mensaje}</div>
    {/if}
  </div>
</div>
