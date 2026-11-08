import pytest

from estacionamiento import Estacionamiento


@pytest.fixture
def sistema():
    """Preparación reutilizable para las pruebas."""
    return Estacionamiento()


# R4 y R5: de 0 a 15 minutos el costo es $0.00.
@pytest.mark.parametrize(
    "minutos, esperado",
    [
        pytest.param(0, 0.00, id="0min"),
        pytest.param(12, 0.00, id="12min"),
        pytest.param(14, 0.00, id="14min"),
        pytest.param(15, 0.00, id="15min"),
    ],
)
def test_costo_cero_hasta_quince_minutos(sistema, minutos, esperado):
    # Arrange (el resto de los parámetros en parametrize)
    tipo_cliente = "normal"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(esperado)


# R6: de 16 a 60 minutos el costo es $20.00.
@pytest.mark.parametrize(
    "minutos, esperado",
    [
        pytest.param(16, 20.00, id="16min"),
        pytest.param(17, 20.00, id="17min"),
        pytest.param(60, 20.00, id="60min"),
    ],
)
def test_primera_hora_cuesta_veinte(sistema, minutos, esperado):
    # Arrange (el resto de los parámetros en parametrize)
    tipo_cliente = "normal"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(esperado)


# R7: después de 60 minutos se cobran $15.00 por cada hora adicional iniciada.
@pytest.mark.parametrize(
    "minutos, esperado",
    [
        pytest.param(61, 35.00, id="61min-una-hora-iniciada"),
        pytest.param(70, 35.00, id="70min-una-hora-iniciada"),
        pytest.param(120, 35.00, id="120min-sin-hora-extra"),
        pytest.param(121, 50.00, id="121min-dos-horas-iniciadas"),
    ],
)
def test_horas_adicionales_iniciadas(sistema, minutos, esperado):
    # Arrange (el resto de los parámetros en parametrize)
    tipo_cliente = "normal"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(esperado)


# R3: el boleto perdido cuesta $300.00 sin importar los minutos ni el tipo.
@pytest.mark.parametrize(
    "minutos",
    [
        pytest.param(0, id="0min"),
        pytest.param(500, id="500min"),
        pytest.param(1440, id="1440min"),
    ],
)
def test_boleto_perdido_cuesta_trescientos(sistema, minutos):
    # Arrange (el resto de los parámetros en parametrize)
    tipo_cliente = "normal"
    boleto_perdido = True

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(300.00)


# R8: el cliente frecuente recibe 10 % de descuento sobre R6.
def test_cliente_frecuente_paga_descuento_sobre_primera_hora(sistema):
    # Arrange
    minutos = 50
    tipo_cliente = "frecuente"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(18.00)


# R7 y R8: el descuento de cliente frecuente también aplica a las horas extra.
def test_cliente_frecuente_paga_descuento_sobre_horas_adicionales(sistema):
    # Arrange
    minutos = 121
    tipo_cliente = "frecuente"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(45.00)


# R7 y R8: caso con decimales en el resultado.
def test_cliente_frecuente_genera_costo_decimal(sistema):
    # Arrange
    minutos = 61
    tipo_cliente = "frecuente"
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(31.50)


# R3 y R8: el descuento no aplica cuando el boleto está perdido.
def test_boleto_perdido_no_lleva_descuento(sistema):
    # Arrange
    minutos = 500
    tipo_cliente = "frecuente"
    boleto_perdido = True

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(300.00)


# R10: los minutos negativos deben lanzar un error.
@pytest.mark.parametrize(
    "minutos",
    [
        pytest.param(-1, id="menos-uno"),
        pytest.param(-100, id="menos-cien"),
    ],
)
def test_minutos_negativos_lanzan_error(sistema, minutos):
    # Arrange (el resto de los parámetros en parametrize)
    tipo_cliente = "normal"
    boleto_perdido = False

    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)


# R11: un tipo de cliente no válido debe lanzar un error.
@pytest.mark.parametrize(
    "tipo_cliente",
    [
        pytest.param("", id="vacio"),
        pytest.param("vip", id="vip"),
        pytest.param("Normal", id="mayusculas"),
    ],
)
def test_tipo_de_cliente_invalido_lanza_error(sistema, tipo_cliente):
    # Arrange
    minutos = 0
    boleto_perdido = False

    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)


# R1: los minutos deben ser un número entero.
def test_minutos_no_enteros_lanzan_error(sistema):
    # Arrange
    minutos = 15.5
    tipo_cliente = "normal"
    boleto_perdido = False

    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)


# R9: el costo nunca puede ser negativo.
def test_costo_nunca_es_negativo(sistema):
    # Arrange
    minutos_validos = [0, 15, 16, 60, 61, 121, 180, 181, 500]
    tipos_validos = ["normal", "frecuente"]
    opciones_boleto = [False, True]

    # Act / Assert
    for minutos in minutos_validos:
        for tipo_cliente in tipos_validos:
            for boleto_perdido in opciones_boleto:
                resultado = sistema.calcular_total(
                    minutos, tipo_cliente, boleto_perdido
                )
                assert resultado >= 0, (
                    f"costo negativo con minutos={minutos}, "
                    f"tipo={tipo_cliente}, perdido={boleto_perdido}"
                )


# R7 y R8: caso adicional propuesto, tercera hora adicional.
@pytest.mark.parametrize(
    "minutos, tipo_cliente, esperado",
    [
        pytest.param(180, "normal", 50.00, id="180min-normal-hora-completa"),
        pytest.param(181, "normal", 65.00, id="181min-normal-hora-iniciada"),
        pytest.param(180, "frecuente", 45.00, id="180min-frecuente-hora-completa"),
        pytest.param(181, "frecuente", 58.50, id="181min-frecuente-hora-iniciada"),
    ],
)
def test_tercera_hora_adicional(sistema, minutos, tipo_cliente, esperado):
    # Arrange (el resto de los parámetros en parametrize)
    boleto_perdido = False

    # Act
    resultado = sistema.calcular_total(minutos, tipo_cliente, boleto_perdido)

    # Assert
    assert resultado == pytest.approx(esperado)
