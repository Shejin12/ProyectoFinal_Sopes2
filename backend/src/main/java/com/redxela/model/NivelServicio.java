package com.redxela.model;

// definir niveles de servicio con espera objetivo en minutos
public enum NivelServicio {
    NIVEL_1(1, 1),
    NIVEL_2(2, 10),
    NIVEL_3(3, 30),
    NIVEL_4(4, 60),
    NIVEL_5(5, 120);

    // declarar valor numerico del nivel
    private final int valor;

    // declarar espera maxima en minutos
    private final int esperaMinutos;

    // construir nivel con valor y espera
    NivelServicio(int valor, int esperaMinutos) {
        // asignar valor
        this.valor = valor;
        // asignar espera
        this.esperaMinutos = esperaMinutos;
    }

    /**
     * Obtener valor numerico del nivel.
     */
    public int obtenerValor() {
        // retornar valor
        return valor;
    }

    /**
     * Obtener espera maxima en minutos.
     */
    public int obtenerEsperaMinutos() {
        // retornar espera
        return esperaMinutos;
    }

    /**
     * Buscar nivel por valor numerico.
     */
    public static NivelServicio desdeValor(int valor) {
        // declarar resultado por defecto
        NivelServicio resultado;
        resultado = NIVEL_5;
        // recorrer valores
        for (NivelServicio nivel : NivelServicio.values()) {
            // comparar valor
            if (nivel.valor == valor) {
                // asignar coincidencia
                resultado = nivel;
            }
        }
        // retornar resultado
        return resultado;
    }
}
