
import math


class Estacionamiento:
    TARIFA_BOLETO_PERDIDO = 300.0

    def calcular_total(self, minutos: int, tipo_cliente: str = "normal",
                       boleto_perdido: bool = False) -> float:
        """
        Calcula el total a pagar de acuerdo con el modelo de la práctica.

        Nota para el alumno:
        trate esta implementación como un sistema que debe verificarse.
        No asuma que todo lo que hace el código es correcto.
        """
        if minutos < 0:
            raise ValueError("Los minutos no pueden ser negativos")

        if tipo_cliente not in ("normal", "frecuente"):
            raise ValueError("Tipo de cliente no válido")

        if boleto_perdido:
            total = self.TARIFA_BOLETO_PERDIDO
        elif minutos < 15:
            total = 0.0
        elif minutos <= 60:
            total = 20.0
        else:
            horas_adicionales = (minutos - 60) // 60
            total = 20.0 + (horas_adicionales * 15.0)

        if tipo_cliente == "frecuente":
            total *= 0.90

        return round(total, 2)
