// definir base de la API del backend
const BASE_URL = "http://localhost:7000";

// obtener lista de clientes
export async function obtenerClientes() {
  // hacer peticion GET
  const respuesta = await fetch(BASE_URL + "/api/clientes");
  // convertir a json
  return respuesta.json();
}

// crear cliente nuevo
export async function crearCliente(nombre, nivel) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/clientes", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, nivel })
  });
  // convertir a json
  return respuesta.json();
}

// obtener productos
export async function obtenerProductos() {
  // hacer peticion GET
  const respuesta = await fetch(BASE_URL + "/api/productos");
  // convertir a json
  return respuesta.json();
}

// obtener mercancias
export async function obtenerMercancias() {
  // hacer peticion GET
  const respuesta = await fetch(BASE_URL + "/api/mercancias");
  // convertir a json
  return respuesta.json();
}

// obtener estado completo
export async function obtenerEstado() {
  // hacer peticion GET
  const respuesta = await fetch(BASE_URL + "/api/estado");
  // convertir a json
  return respuesta.json();
}

// obtener almacen paginado
export async function obtenerAlmacen(pagina, tamano) {
  // construir url
  let url = BASE_URL + "/api/almacen?pagina=" + pagina + "&tamano=" + tamano;
  // hacer peticion GET
  const respuesta = await fetch(url);
  // convertir a json
  return respuesta.json();
}

// crear pedido manual
export async function crearPedido(idCliente, nivel, detalles) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/pedidos", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ idCliente, nivel, detalles })
  });
  // convertir a json
  return respuesta.json();
}

// registrar llegada de mercaderia
export async function registrarRecepcion(idProducto, cantidad) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/recepcion", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ idProducto, cantidad })
  });
  // convertir a json
  return respuesta.json();
}

// cambiar politica
export async function cambiarPolitica(politica) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/politica", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ politica })
  });
  // convertir a json
  return respuesta.json();
}

// cambiar generador
export async function cambiarGenerador(activo) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/generador", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ activo })
  });
  // convertir a json
  return respuesta.json();
}

// provocar deadlock
export async function provocarDeadlock() {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/deadlock/provocar", { method: "POST" });
  // convertir a json
  return respuesta.json();
}

// resolver deadlock
export async function resolverDeadlock(ganadora) {
  // hacer peticion POST
  const respuesta = await fetch(BASE_URL + "/api/deadlock/resolver", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ ganadora })
  });
  // convertir a json
  return respuesta.json();
}
