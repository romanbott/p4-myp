## Casos normales

- Cliente normal, 12 minutos -> costo 0
- Cliente normal, 70 minutos -> costo 35
- Cliente normal, 500 minutos, boleto perdido -> costo 300

## Casos frontera

- Cliente normal, 0 minutos -> costo 0
- Cliente normal, 14 minutos -> costo 0
- Cliente normal, 15 minutos -> costo 0
- Cliente normal, 16 minutos -> costo 20
- Cliente normal, 17 minutos -> costo 20
- Cliente normal, 60 minutos -> costo 20
- Cliente normal, 61 minutos -> costo 35
- Cliente normal, 120 minutos -> costo 35
- Cliente normal, 121 minutos -> costo 50

## Entradas inválidas

- Cliente normal, -1 minutos -> excepcion
- Cliente normal, 15.5 minutos -> excepcion (solo en Python)
- Cliente "invalido", 0 minutos -> excepcion

## Casos de interacción entre reglas

- Cliente frecuente, 50 minutos -> costo 18
- Cliente frecuente, 121 minutos -> costo 45
- Cliente frecuente, 500 minutos, boleto perdido -> costo 300
- Cliente frecuente, 61 minutos -> costo 31.5

## Caso para el costo nunca negativo

- Con varias entradas válidas (0, 15, 16, 60, 61, 121, 500 y 1440 minutos, con ambos tipos de cliente y con o sin boleto perdido), el costo nunca es negativo.

## Caso adicional propuesto por el equipo

Probamos el caso limite para la tercera hora.

- Cliente normal, 180 minutos -> costo 50
- Cliente normal, 181 minutos -> costo 65
- Cliente frecuente, 180 minutos -> costo 45
- Cliente frecuente, 181 minutos -> costo 58.5
