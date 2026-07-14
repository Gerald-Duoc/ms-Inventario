# Inventario — Catalogo y Stock

## Que es

El corazon del catalogo de la libreria. Este microservicio es la fuente de verdad de **que libros existen**, **cuantos hay en cada sucursal**, y **que reservas hay pendientes**. No vende nada ni despacha — solo lleva la cuenta del inventario fisico.

## Los tres dominios

### 1. Libros (catalogo)

Cada libro tiene titulo, autor, editorial, precio de compra (lo que la libreria paga), precio de venta (lo que paga el cliente), categoria, y un contador de unidades vendidas. Las 11 categorias son fijas: `FICCION`, `NO_FICCION`, `ROMANCE`, `EDUCATIVO`, `TERROR`, `HISTORIA`, `INFANTIL`, `NOVELA_GRAFICA`, `RELIGION`, `POLITICA`, `CIENCIA_FICCION`.

### 2. Stock por sucursal

Aqui es donde se pone interesante. La libreria tiene 2 sucursales fisicas (Centro en Santiago, Norte en Antofagasta). Cada libro puede tener diferente cantidad de ejemplares en cada sucursal, con un **stock minimo** (para alertas) y un **stock maximo** (capacidad del estante). La entidad `StockLibro` es el puente: vincula un libro con una sucursal y guarda las cantidades.

**Operaciones importantes:**
- `anadir` — Cuando llega mercaderia de un proveedor o se transfiere desde otra sucursal, se suman ejemplares.
- `reducir` — Cuando se hace una venta, se restan ejemplares. Si no hay stock suficiente, la operacion se rechaza con un error.
- `validar` — Antes de procesar un carrito de compras, se verifica que **todos** los productos tengan stock suficiente en la sucursal elegida.

### 3. Reservas

Cuando un cliente hace un pedido online, los libros se "reservan" para que otro cliente no los compre antes. Las reservas estan parcialmente implementadas — se guardan pero aun no descuentan stock automaticamente.

## Que otros servicios usan

- **TiendaWeb** consulta el catalogo para mostrar productos, verifica stock antes de agregar al carrito, y reserva stock al confirmar una compra.
- **Ventas** consulta precios y valida stock antes de procesar una venta. Tambien descuenta stock y registra unidades vendidas despues de finalizar.
- **Sucursal** llama a las operaciones de stock cuando se aprueba una transferencia entre sucursales.

## Ejecutar

```cmd
cd ms-Inventario
.\mvnw.cmd spring-boot:run
```

Puerto: **8094** | DB: `inventario_ms`

## Endpoints

**Libros** (`/api/libros`): CRUD completo + busqueda por nombre + precio + unidades vendidas.
**Stock** (`/api/stock-libros`): CRUD + `/anadir` + `/reducir` + `/validar`.
**Reservas** (`/api/reservas`): Crear, listar, ver, eliminar.
