# Inventario (ms-Inventario)

Gestion de libros, stock por sucursal, y reservas. Microservicio central del catalogo de la libreria.

## Puerto

**8094** | DB: `inventario_ms`

## Endpoints

### Libros (`/api/libros`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/libros` | Listar todos |
| GET | `/api/libros/{id}` | Obtener por ID |
| GET | `/api/libros/buscar?nombre=` | Buscar por nombre |
| GET | `/api/libros/{id}/precio` | Obtener precio de venta |
| POST | `/api/libros` | Crear libro |
| PUT | `/api/libros/{id}` | Actualizar libro |
| DELETE | `/api/libros/{id}` | Eliminar libro |
| POST | `/api/libros/{id}/unidades-vendidas?cantidad=` | Incrementar unidades vendidas |

### Stock (`/api/stock-libros`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/stock-libros` | Listar todo el stock |
| GET | `/api/stock-libros/{id}` | Obtener por ID |
| POST | `/api/stock-libros` | Crear registro de stock |
| PUT | `/api/stock-libros/{id}` | Actualizar stock |
| DELETE | `/api/stock-libros/{id}` | Eliminar registro |
| POST | `/api/stock-libros/anadir` | Anadir stock |
| POST | `/api/stock-libros/reducir` | Reducir stock |
| POST | `/api/stock-libros/validar` | Validar stock suficiente |

### Reservas (`/api/reservas`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Obtener por ID |
| POST | `/api/reservas` | Crear reserva |
| DELETE | `/api/reservas/{id}` | Eliminar reserva |

## Crear libro

```json
POST /api/libros
{
  "nombre": "Cien Anos de Soledad",
  "descripcion": "Novela del realismo magico",
  "editorial": "Sudamericana",
  "autor": "Gabriel Garcia Marquez",
  "precioCompra": 5000,
  "precioVenta": 15000,
  "categoria": "FICCION",
  "fechaCreacion": "2026-01-15"
}
```

### Categorias validas

`FICCION`, `NO_FICCION`, `ROMANCE`, `EDUCATIVO`, `TERROR`, `HISTORIA`, `INFANTIL`, `NOVELA_GRAFICA`, `RELIGION`, `POLITICA`, `CIENCIA_FICCION`

## Ejecucion

```cmd
cd ms-Inventario
.\mvnw.cmd spring-boot:run
```
