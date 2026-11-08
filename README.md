# Práctica 4: pruebas unitarias

Pruebas unitarias para el sistema de ticket de estacionamiento, diseñadas a
partir del reglas R1–R11 de la práctica.

## Requisitos

- Python 3.10 o superior, con pytest 8 o superior. uv es opcional.
- Java 17 o superior y Maven 3.9 o superior.

## Ejecutar las pruebas de Python

Con uv:

```bash
cd python
uv sync
uv run pytest -v
```

Si pytest ya está disponible en el sistema:

```bash
cd python
pytest -v
```

## Ejecutar las pruebas de Java

```bash
cd java
mvn test
```
