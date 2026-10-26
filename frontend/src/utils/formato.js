// dar formato a segundos en texto legible
export function formatoSegundos(valor) {
  // verificar nulo
  if (valor === null || valor === undefined) {
    return "0 s";
  }
  // redondear a un decimal
  let redondeado = Math.round(valor * 10) / 10;
  // convertir a texto
  return redondeado + " s";
}

// dar formato a fecha en hora local
export function formatoHora(marca) {
  // verificar marca
  if (!marca) {
    return "-";
  }
  // crear fecha
  let fecha = new Date(marca);
  // convertir a texto
  return fecha.toLocaleTimeString();
}

// obtener etiqueta de nivel
export function etiquetaNivel(nivel) {
  // verificar objeto
  if (!nivel) {
    return "-";
  }
  // si es texto directo retornar
  if (typeof nivel === "string") {
    return nivel;
  }
  // si tiene valor usarlo
  if (nivel.name) {
    return nivel.name;
  }
  // retorno por defecto
  return "NIVEL";
}

// obtener clase bootstrap por nivel
export function claseNivel(nivelTexto) {
  // iniciar clase por defecto
  let clase = "bg-secondary";
  // clasificar por texto
  if (nivelTexto === "NIVEL_1") {
    clase = "bg-danger";
  } else if (nivelTexto === "NIVEL_2") {
    clase = "bg-warning text-dark";
  } else if (nivelTexto === "NIVEL_3") {
    clase = "bg-primary";
  } else if (nivelTexto === "NIVEL_4") {
    clase = "bg-info text-dark";
  } else if (nivelTexto === "NIVEL_5") {
    clase = "bg-success";
  }
  // retornar clase
  return clase;
}
