package mx.edu.practica4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstacionamientoTest {

    private Estacionamiento sistema;

    @BeforeEach
    void setUp() {
        sistema = new Estacionamiento();
    }

    // # R4 y R5: de 0 a 15 minutos el costo es $0.00.
    @ParameterizedTest(name = "{0} minutos -> {1}")
    @CsvSource({
            "0,  0.00",
            "12, 0.00",
            "14, 0.00",
            "15, 0.00"
    })
    void costoCeroHastaQuinceMinutos(int minutos, double esperado) {
        // Arrange (el resto de los parámetros en CsvSource)

        String tipoCliente = "normal";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(esperado, resultado, 0.001);
    }

    // # R6: de 16 a 60 minutos el costo es $20.00.
    @ParameterizedTest(name = "{0} minutos -> {1}")
    @CsvSource({
            "16, 20.00",
            "17, 20.00",
            "60, 20.00"
    })
    void primeraHoraCuestaVeinte(int minutos, double esperado) {
        // Arrange (el resto de los parámetros en CsvSource)

        String tipoCliente = "normal";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(esperado, resultado, 0.001);
    }

    // # R7: después de 60 minutos se cobran $15.00 por cada hora adicional
    // iniciada.
    @ParameterizedTest(name = "{0} minutos -> {1}")
    @CsvSource({
            "61, 35.00",
            "70, 35.00",
            "120, 35.00",
            "121, 50.00"
    })
    void horasAdicionalesIniciadas(int minutos, double esperado) {
        // Arrange (el resto de los parámetros en CsvSource)

        String tipoCliente = "normal";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(esperado, resultado, 0.001);
    }

    // # R3: el boleto perdido cuesta $300.00 sin importar los minutos ni el tipo.
    @ParameterizedTest(name = "{0} minutos, boleto perdido")
    @CsvSource({
            "0",
            "500",
            "1440"
    })
    void boletoPerdidoCuestaTrescientos(int minutos) {
        // Arrange (el resto de los parámetros en CsvSource)

        String tipoCliente = "normal";
        boolean boletoPerdido = true;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(300.00, resultado, 0.001);
    }

    // # R8: el cliente frecuente recibe 10 % de descuento sobre R6.
    @Test
    void clienteFrecuenteDescuentoPrimeraHora() {
        // Arrange
        int minutos = 50;
        String tipoCliente = "frecuente";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(18.00, resultado, 0.001);
    }

    // # R7 y R8: el descuento de cliente frecuente también aplica a las horas
    // extra.
    @Test
    void clienteFrecuenteDescuentoHorasAdicionales() {
        // Arrange
        int minutos = 121;
        String tipoCliente = "frecuente";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(45.00, resultado, 0.001);
    }

    // # R7 y R8: caso con decimales en el resultado.
    @Test
    void clienteFrecuenteCostoDecimal() {
        // Arrange
        int minutos = 61;
        String tipoCliente = "frecuente";
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(31.50, resultado, 0.001);
    }

    // # R3 y R8: el descuento no aplica cuando el boleto está perdido.
    @Test
    void boletoPerdidoNoLlevaDescuento() {
        // Arrange
        int minutos = 500;
        String tipoCliente = "frecuente";
        boolean boletoPerdido = true;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(300.00, resultado, 0.001);
    }

    // # R10: los minutos negativos deben lanzar un error.
    @ParameterizedTest(name = "{0} minutos")
    @CsvSource({
            "-1",
            "-100"
    })
    void minutosNegativosLanzanError(int minutos) {
        // Arrange (el resto de los parámetros en CsvSource)

        String tipoCliente = "normal";
        boolean boletoPerdido = false;

        // Act
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sistema.calcularTotal(minutos, tipoCliente, boletoPerdido));

        // Assert
        assertEquals("Los minutos no pueden ser negativos", error.getMessage());
    }

    // # R11: un tipo de cliente fuera del modelo debe lanzar un error.
    @ParameterizedTest(name = "cliente tipo \"{0}\"")
    @ValueSource(strings = { "", "vip", "Normal" })
    void tipoDeClienteInvalidoLanzaError(String tipo) {
        // Arrange
        int minutos = 10;
        String tipoCliente = tipo;
        boolean boletoPerdido = false;

        // Act
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sistema.calcularTotal(minutos, tipoCliente, boletoPerdido));

        // Assert
        assertEquals("Tipo de cliente no válido", error.getMessage());
    }

    // # R9: el costo nunca puede ser negativo.
    @Test
    void costoNuncaEsNegativo() {
        // Arrange
        int[] minutosValidos = { 0, 1, 15, 16, 60, 61, 120, 121, 500, 1440 };
        String[] tiposValidos = { "normal", "frecuente" };
        boolean[] opcionesBoleto = { false, true };

        // Act / Assert
        for (int minutos : minutosValidos) {
            for (String tipoCliente : tiposValidos) {
                for (boolean boletoPerdido : opcionesBoleto) {
                    double costo = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);
                    assertTrue(costo >= 0,
                            () -> "costo negativo con minutos=" + minutos
                                    + ", tipo=" + tipoCliente + ", perdido=" + boletoPerdido);
                }
            }
        }
    }

    // # R7 y R8: caso adicional del equipo, tercera hora adicional.
    @ParameterizedTest(name = "{0} min, {1} -> {2}")
    @CsvSource({
            "180, normal, 50.00",
            "181, normal, 65.00",
            "180, frecuente, 45.00",
            "181, frecuente, 58.50"
    })
    void terceraHoraAdicional(int minutos, String tipoCliente, double esperado) {
        // Arrange (el resto de los parámetros en CsvSource)
        boolean boletoPerdido = false;

        // Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertEquals(esperado, resultado, 0.001);
    }
}
