<script>
  // importar componentes
  import MonitorRecursos from "./components/MonitorRecursos.svelte";
  import ListaEspera from "./components/ListaEspera.svelte";
  import ListaProceso from "./components/ListaProceso.svelte";
  import PanelEstadisticas from "./components/PanelEstadisticas.svelte";
  import FormularioPedido from "./components/FormularioPedido.svelte";
  import FormularioRecepcion from "./components/FormularioRecepcion.svelte";
  import PanelDeadlock from "./components/PanelDeadlock.svelte";
  import MonitorAlmacen from "./components/MonitorAlmacen.svelte";
  // importar servicios y estado
  import { obtenerEstado } from "./services/api.js";
  import { aplicarEstado } from "./stores/estado.js";
  import { onMount } from "svelte";

  // cargar estado periodico
  async function cargar() {
    // pedir estado
    try {
      let estado = await obtenerEstado();
      // aplicar a stores
      aplicarEstado(estado);
    } catch (error) {
      // ignorar error de conexion
    }
  }

  // iniciar polling al montar
  onMount(() => {
    // cargar de inmediato
    cargar();
    // repetir cada segundo
    let intervalo = setInterval(cargar, 1000);
    // limpiar al desmontar
    return () => clearInterval(intervalo);
  });
</script>

<!-- mostrar estructura principal -->
<nav class="navbar navbar-dark bg-dark mb-3">
  <div class="container-fluid">
    <span class="navbar-brand">REDXela Centro de Distribucion</span>
    <span class="text-white-50">Simulacion en tiempo real</span>
  </div>
</nav>

<main class="container-fluid pb-4">
  <div class="row">
    <div class="col-md-4">
      <PanelEstadisticas />
      <FormularioPedido />
      <FormularioRecepcion />
    </div>
    <div class="col-md-4">
      <MonitorRecursos />
      <ListaProceso />
      <ListaEspera />
    </div>
    <div class="col-md-4">
      <PanelDeadlock />
      <MonitorAlmacen />
    </div>
  </div>
</main>
