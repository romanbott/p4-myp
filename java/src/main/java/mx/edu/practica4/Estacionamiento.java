
package mx.edu.practica4;

public class Estacionamiento {

    public static final double TARIFA_BOLETO_PERDIDO = 300.0;

    public double calcularTotal(int minutos, String tipoCliente, boolean boletoPerdido) {
        /*
         * Nota para el alumno:
         * trate esta implementación como un sistema que debe verificarse.
         * No asuma que todo lo que hace el código es correcto.
         */
        if (minutos < 0) {
            throw new IllegalArgumentException("Los minutos no pueden ser negativos");
        }

        if (!"normal".equals(tipoCliente) && !"frecuente".equals(tipoCliente)) {
            throw new IllegalArgumentException("Tipo de cliente no válido");
        }

        double total;

        if (boletoPerdido) {
            total = TARIFA_BOLETO_PERDIDO;
        } else if (minutos < 15) {
            total = 0.0;
        } else if (minutos <= 60) {
            total = 20.0;
        } else {
            int horasAdicionales = (minutos - 60) / 60;
            total = 20.0 + (horasAdicionales * 15.0);
        }

        if ("frecuente".equals(tipoCliente)) {
            total *= 0.90;
        }

        return Math.round(total * 100.0) / 100.0;
    }
}
