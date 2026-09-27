# TP: Sistema de ventas con HQL, Spring Boot y GUI

## 1. Propósito y recorrido del TP

Construir, en iteraciones, un sistema de ventas para una casa de tecnología. El
objetivo didáctico es que la misma regla de negocio evolucione de una consulta
probada en la consola HQL a una operación expuesta por HTTP y finalmente a una
pantalla.

El recorrido acordado es el siguiente:

```text
script.hql + consola HQL
             |
             v
Service con EntityManager y HQL parametrizado
             |
             v
Controller REST
             |
             v
GUI web
```

No se usarán `Repository` ni DAO en la primera versión. Cada `@Service` será
dueño de sus consultas HQL y recibirá un `EntityManager`. Esta alternativa es
totalmente válida para el TP: evita una capa ceremonial y deja HQL visible. Si
el proyecto crece, se puede extraer una clase de consultas por agregado, pero
sin modificar los contratos del controller.

## 2. Relevamiento del modelo actual

Las entidades se encuentran en `app.mappings` y conforman estos agregados:

| Área | Entidades y relaciones disponibles |
|---|---|
| Catálogo | `Producto` pertenece a una `Categoria` y a un `Proveedor`. `ProveedorCategoria` modela qué categorías puede proveer cada proveedor. |
| Clientes | `Cliente` tiene `Usuario` y `TipoCliente` (`Consumidor Final` o `Revendedor`). |
| Venta | `Orden` vincula cliente, empleado, fecha generada y fecha entregada. `DetalleOrden` vincula orden, producto y cantidad. |
| Promociones | `Promocion` tiene períodos en `PromocionVigencia`; `PromocionProducto` aplica un descuento a un producto durante una vigencia. |
| Organización | `Empleado` puede tener un `jefe` de la misma entidad. |

Las asociaciones sólo están declaradas hacia el lado `@ManyToOne`; por ejemplo,
`Orden` no tiene una colección de detalles y `Cliente` no tiene una colección de
órdenes.

### Convenciones importantes

- En HQL se usan los nombres de las **entidades y atributos Java**, no las
  tablas ni columnas SQL: `Producto`, `precioUnitario`, `idProducto`.
- `flgDiscontinuo` es un `Integer`: `0 = vendible` y
  `1 = discontinuado`.
- Una orden sin `fechaEntregada` se considera pendiente. Una orden con fecha de
  entrega se considera entregada. No existe todavía una cancelación formal.
- El importe de una línea se calcula hoy como
  `detalle.cantidad * detalle.producto.precioUnitario`. Esta es una simplificación:
  no queda guardado ni el precio histórico ni el descuento aplicado.

### Hallazgos que quedan como mejora, no como bloqueo

1. Las vigencias de `script.hql` usan `NOW +/- días`, por lo que en cada carga
   hay promociones vigentes, vencidas y próximas. Requiere una versión de la
   consola con aritmética temporal habilitada.

## 3. Alcance funcional: casos de uso

El nombre de la entidad existente `Orden` se interpretará como **orden de
venta**, no como orden de compra al proveedor.

| ID | Actor | Caso de uso | Resultado y reglas principales |
|---|---|---|---|
| CU-01 | Visitante/cliente | Consultar catálogo | Ve productos vendibles por categoría, proveedor o texto, con precio y stock. Los discontinuados no se ofrecen. |
| CU-02 | Cliente | Identificarse | Se autentica por `username` y contraseña. Para el TP la contraseña actual es texto plano; no es aceptable fuera del aula. |
| CU-03 | Cliente | Consultar promociones vigentes | Ve sólo promociones cuyo período contiene la fecha consultada y el descuento de cada producto. Si hay varias, se muestra la mayor. |
| CU-04 | Vendedor | Crear orden de venta | Selecciona cliente, empleado y renglones. Cada cantidad debe ser positiva, el producto vendible y el stock suficiente. Se crea la orden y sus detalles en una única transacción. |
| CU-05 | Vendedor | Consultar orden | Visualiza cabecera, renglones, subtotales y total calculado. Puede buscar por cliente y rango de fechas. |
| CU-06 | Depósito | Entregar orden | Sólo una orden pendiente puede marcarse entregada. Se establece `fechaEntregada`; no se permite una fecha anterior a `fechaGenerada`. |
| CU-07 | Compras/depósito | Consultar reposición | Obtiene productos cuyo stock está en o debajo del punto de reposición, agrupables por proveedor. |
| CU-08 | Administrador | Administrar catálogo | Alta y modificación de producto, categoría, proveedor y relación proveedor-categoría. No se borra físicamente un producto vendido: se lo discontinúa. |
| CU-09 | Administrador | Administrar promociones | Define promoción, período válido y productos con descuento entre 0 y 1. Los períodos inválidos o solapados deben rechazarse según la política que se defina. |
| CU-10 | Supervisor | Consultar indicadores | Ve facturación calculada por cliente/categoría/empleado y productos sin ventas para tomar decisiones. |

### Flujo central: “Vendedor carga una orden de venta”

1. El vendedor selecciona un cliente y agrega renglones al carrito.
2. El sistema consulta productos vendibles, promociones de la fecha y stock.
3. El vendedor confirma. El servicio revalida todas las cantidades y stock en
   una transacción.
4. Se persiste `Orden` con `fechaGenerada` y cada `DetalleOrden`.
5. Se descuenta stock de cada producto. Si cualquier renglón falla, se revierte
   todo y no queda una orden parcial.
6. La GUI muestra el número de orden y el total calculado.

**Decisión de alcance:** en esta versión el descuento se calcula al consultar.
Como mejora posterior, `DetalleOrden` debería guardar `precioUnitarioVendido` y
`descuentoAplicado`, para que una promoción futura no cambie el total histórico.

## 4. Catálogo de consultas HQL

Probar primero cada consulta en la consola usando valores concretos. En Java se
reemplazan los literales por parámetros `:nombre`. Las consultas de lectura no
deben interpolar texto del usuario.

### Nivel SIMPLE

| ID | Objetivo | HQL |
|---|---|---|
| S1 | Listar productos vendibles | `select p from Producto p where p.flgDiscontinuo = 0 order by p.descripcion` |
| S2 | Buscar por texto | `select p from Producto p where lower(p.descripcion) like lower(concat('%', :texto, '%')) and p.flgDiscontinuo = 0 order by p.precioUnitario` |
| S3 | Productos de un proveedor | `select p from Producto p where p.proveedor.idProveedor = :idProveedor order by p.descripcion` |
| S4 | Productos bajo reposición | `select p from Producto p where p.unidadesStock <= p.unidadesReposicion and p.flgDiscontinuo = 0 order by p.proveedor.empresa, p.descripcion` |
| S5 | Clientes de un tipo | `select c from Cliente c where c.tipoCliente.idTipoCliente = :idTipoCliente order by c.nombre` |
| S6 | Empleados sin jefe | `select e from Empleado e where e.jefe is null order by e.nombre` |

### Nivel INTERMEDIO

| ID | Objetivo | HQL |
|---|---|---|
| I1 | Catálogo con categoría y proveedor | `select p.descripcion, p.precioUnitario, c.descripcion, pr.empresa from Producto p join p.categoria c join p.proveedor pr where p.flgDiscontinuo = 0 order by c.descripcion, p.descripcion` |
| I2 | Proveedores habilitados para una categoría | `select pc.proveedor from ProveedorCategoria pc where pc.categoria.idCategoria = :idCategoria order by pc.proveedor.empresa` |
| I3 | Promociones vigentes a una fecha | `select pp.producto, pv.promocion, pp.descuento from PromocionProducto pp join pp.promocionVigencia pv where pv.fechaInicio <= :fecha and pv.fechaFin >= :fecha order by pp.producto.descripcion` |
| I4 | Órdenes de un cliente en un período | `select o from Orden o where o.cliente.idCliente = :idCliente and o.fechaGenerada between :desde and :hasta order by o.fechaGenerada desc` |
| I5 | Renglones de una orden con subtotal | `select d.producto.descripcion, d.cantidad, d.producto.precioUnitario, d.cantidad * d.producto.precioUnitario from DetalleOrden d where d.orden.idOrden = :idOrden` |
| I6 | Unidades vendidas por categoría | `select p.categoria.descripcion, sum(d.cantidad) from DetalleOrden d join d.producto p group by p.categoria.descripcion order by sum(d.cantidad) desc` |
| I7 | Cantidad de órdenes atendidas por empleado | `select o.empleado.nombre, count(o) from Orden o group by o.empleado.nombre having count(o) >= :minimo order by count(o) desc` |
| I8 | Clientes sin compras | `select c from Cliente c where not exists (select o.idOrden from Orden o where o.cliente = c) order by c.nombre` |

### Nivel COMPLEJO

| ID | Objetivo | HQL |
|---|---|---|
| C1 | Mejor descuento vigente por producto | `select pp.producto, max(pp.descuento) from PromocionProducto pp join pp.promocionVigencia pv where pv.fechaInicio <= :fecha and pv.fechaFin >= :fecha group by pp.producto` |
| C2 | Facturación estimada por cliente | `select o.cliente.nombre, sum(d.cantidad * d.producto.precioUnitario) from DetalleOrden d join d.orden o where o.fechaGenerada between :desde and :hasta group by o.cliente.nombre order by sum(d.cantidad * d.producto.precioUnitario) desc` |
| C3 | Productos nunca vendidos | `select p from Producto p where not exists (select d.idDetalleOrden from DetalleOrden d where d.producto = p) order by p.descripcion` |
| C4 | Supervisores y ventas de sus subordinados directos | `select jefe.nombre, count(o), sum(d.cantidad * d.producto.precioUnitario) from DetalleOrden d join d.orden o join o.empleado vendedor join vendedor.jefe jefe where o.fechaGenerada between :desde and :hasta group by jefe.nombre order by sum(d.cantidad * d.producto.precioUnitario) desc` |
| C5 | Órdenes cuyo importe supera un umbral | `select o from Orden o where (select sum(d.cantidad * d.producto.precioUnitario) from DetalleOrden d where d.orden = o) > :importeMinimo order by o.fechaGenerada desc` |
| C6 | Productos más caros que el promedio de su categoría | `select p from Producto p where p.precioUnitario > (select avg(p2.precioUnitario) from Producto p2 where p2.categoria = p.categoria) order by p.categoria.descripcion, p.precioUnitario desc` |
| C7 | Descontar stock de forma condicional | `update Producto p set p.unidadesStock = p.unidadesStock - :cantidad where p.idProducto = :idProducto and p.flgDiscontinuo = 0 and p.unidadesStock >= :cantidad` |
| C8 | Entregar sólo una orden pendiente | `update Orden o set o.fechaEntregada = :fechaEntrega where o.idOrden = :idOrden and o.fechaEntregada is null and :fechaEntrega >= o.fechaGenerada` |

### Cómo evaluar las consultas

- Para S1--C6, registrar la consulta, parámetros, cantidad de filas y dos filas
  representativas en un informe breve.
- C1 devuelve cada `Producto` y su descuento máximo; la GUI calcula
  `precioUnitario * (1 - descuento)`.
- C2, C4 y C5 calculan importes de referencia con el precio actual. Documentar
  esa limitación.
- C7 y C8 son mutaciones: se ejecutan dentro de `@Transactional`, se verifica
  el valor retornado por `executeUpdate()` y luego se limpia el contexto de
  persistencia (`entityManager.clear()`) si se siguen usando entidades ya
  cargadas. Un resultado `0` significa que la precondición no se cumplió.

## 5. Diseño técnico propuesto

### Paquetes

```text
app.mappings/       Entidades existentes
app.service/        Reglas de negocio y HQL directo
app.dto/            Entradas y salidas HTTP; nunca exponer entidades directamente
app.controller/     Endpoints REST y validación de la solicitud
app.exception/      Excepciones de negocio + manejador HTTP
app.web/            HTML/JS de la GUI (iteración final)
```

Servicios iniciales:

- `CatalogoService`: S1--S4, I1--I2, ABM de productos/categorías/proveedores.
- `PromocionService`: I3, C1 y administración de promociones.
- `OrdenVentaService`: I4--I5, C2--C5, alta y entrega de órdenes.
- `ClienteService`: S5, I8 y búsqueda/autenticación académica.
- `ReporteService`: I6--I7, C2--C6.

### Patrón de servicio sin Repository

```java
@Service
public class CatalogoService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Producto> buscarVendibles(String texto) {
        return entityManager.createQuery("""
                select p from Producto p
                where p.flgDiscontinuo = 0
                  and lower(p.descripcion) like lower(concat('%', :texto, '%'))
                order by p.descripcion
                """, Producto.class)
            .setParameter("texto", texto)
            .getResultList();
    }
}
```

Para crear una orden, `OrdenVentaService` tendrá `@Transactional` (no
`readOnly`) y hará `persist` de la cabecera/detalles más la actualización de
stock. El controller no contiene HQL, transacciones ni reglas de stock.

### Contratos HTTP tentativos

| Método y ruta | Caso de uso |
|---|---|
| `GET /api/productos?texto=&categoriaId=&proveedorId=` | Catálogo y búsqueda |
| `GET /api/productos/bajo-stock` | Reposición |
| `GET /api/promociones/vigentes?fecha=2026-09-27` | Promociones |
| `POST /api/ordenes` | Crear orden de venta |
| `GET /api/ordenes/{id}` | Consultar cabecera/renglones/total |
| `GET /api/ordenes?clienteId=&desde=&hasta=` | Historial de cliente |
| `PATCH /api/ordenes/{id}/entrega` | Marcar entrega |
| `GET /api/reportes/facturacion-clientes?desde=&hasta=` | Indicador C2 |

Entrada sugerida para `POST /api/ordenes`:

```json
{
  "clienteId": 1,
  "empleadoId": 2,
  "items": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 7, "cantidad": 1 }
  ]
}
```

La respuesta debe devolver un DTO con el id de orden, fechas, renglones,
subtotales, descuentos consultados y total. No devolver `Orden` directamente:
evita acoplamiento, problemas de serialización perezosa y filtrar campos como
`Usuario.password`.

## 6. Plan de entrega incremental

| Entrega | Objetivo verificable | Evidencia mínima |
|---|---|---|
| E0: Base | Arranque de Spring y datos de prueba | Aplicación conecta a HSQLDB; `script.hql` carga datos; se corrigen fechas/codificación necesarias. |
| E1: Consola | HQL de simple a complejo | Archivo con S1--C6 ejecutadas, parámetros y capturas/resultados. C7/C8 se prueban sobre datos descartables. |
| E2: Servicios | HQL invocado desde Java | Servicios con `EntityManager`, parámetros y pruebas `@SpringBootTest` para catálogo, promociones y órdenes. |
| E3: API | Casos de uso por HTTP | Controllers, DTOs, validaciones y respuestas 400/404/409 coherentes. Pruebas con Postman/HTTP client. |
| E4: GUI | Operación end-to-end | Pantallas de catálogo, creación/consulta/entrega de orden y un reporte. |
| E5: Cierre | Calidad y demo | README de ejecución, guion de demo, validaciones y decisiones conocidas. |

## 7. Criterios de aceptación y pruebas

1. Una búsqueda nunca trae productos discontinuados.
2. Una promoción sólo aparece si la fecha está dentro de `[fechaInicio,
   fechaFin]`.
3. No se crea una orden si un item tiene cantidad menor o igual a cero, producto
   inexistente/discontinuado o stock insuficiente.
4. Al crear correctamente una orden, se generan todos sus detalles y se reduce
   el stock una sola vez. Ante cualquier error no se persiste nada.
5. La orden muestra subtotales y total coherentes con sus renglones.
6. Una orden entregada no puede volver a marcarse como pendiente con el endpoint
   de entrega; una segunda entrega informa conflicto o no-op explícito.
7. Consultas de reporte reciben fechas y no concatenan parámetros en el HQL.
8. La API nunca expone contraseñas ni entidades JPA crudas.

Casos de prueba mínimos: alta válida; stock exacto; stock insuficiente; producto
discontinuado; promoción vigente/no vigente; cliente sin órdenes; doble entrega;
reporte en rango vacío y rango con ventas. Para C7, comprobar tanto el caso de
una fila actualizada como el de cero filas.

## 8. Decisiones para la exposición

- Defender el uso de `EntityManager` en servicios: HQL queda centralizado junto
  a la regla de negocio y cada operación tiene transacción explícita.
- Aclarar que HQL trabaja sobre objetos y relaciones, mientras Hibernate genera
  el SQL según HSQLDB.
- Demostrar una consulta de cada nivel, luego el mismo caso desde un endpoint y
  finalmente desde la GUI.
- Presentar como mejora realista el precio histórico y descuento por detalle,
  más el cambio `Double` a `BigDecimal`.
