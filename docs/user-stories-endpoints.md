# QhuriNet - User stories, tablas y endpoints de prueba

Base URL local: `http://localhost:8083`

Swagger UI: `http://localhost:8083/swagger-ui.html`

Estado:
- `OK`: existe endpoint y cubre el flujo principal.
- `PARCIAL`: existe una parte, pero falta una validacion, filtro, alias exacto o regla de negocio.
- `TODO`: no hay endpoint directo o falta implementar la historia.

## Preparacion en Postman

1. Login admin:
   `POST /api/auth/login`
   Body:
   ```json
   { "username": "admin", "password": "admin123" }
   ```
2. Guardar `token` como `access_token` y `refreshToken` como `refresh_token`.
3. En endpoints protegidos usar header:
   `Authorization: Bearer {{access_token}}`
4. Para datos completos de prueba, correr la app con perfil `mock-data`.
   Usuarios mock:
   - `admin/admin123`
   - `generador/qhuri123`
   - `bodega/qhuri123`
   - `recolector/qhuri123`

## Infraestructura y seguridad

| ID | Historia | Estado | Tablas | Endpoints |
| --- | --- | --- | --- | --- |
| SETUP-1 | Setup proyecto Spring Boot | OK | n/a | Arranque app, Swagger |
| SETUP-2 | Modelo de datos | OK | usuario, rol, usuario_rol, material, publicacion, publicacion_material, recoleccion, calificacion, mensaje_chat, incentivo, usuario_incentivo, certificado, documento_verificacion, reclamo, transaccion_puntos, transaccion_dinero, ruta, metodo_pago, notificacion, faq, soporte_contacto | CRUD por controller |
| SETUP-3 | Repositorio Git con ramas | EXTERNO | n/a | n/a |
| 35-EP5 | Registro con correo | OK | usuario, rol, usuario_rol | `POST /api/auth/register` |
| 51b-EP9 | JWT con refresh token | OK | usuario, rol, memoria refresh token | `POST /api/auth/login`, `POST /api/auth/refresh` |
| 53b-EP9 | Hashear contrasenas con BCrypt | OK | usuario.password | Registro/login usan `PasswordEncoder` |
| 56b-EP9 | Logout invalida refresh token | OK | memoria refresh token | `POST /api/auth/logout` |
| 52b-EP9 | Restriccion por rol | OK | usuario, rol, usuario_rol | `@PreAuthorize` en controllers y endpoints administrativos restringidos a `ADMIN`. |
| 54b-EP9 | Permisos a nivel de recurso | OK | usuario y entidad dueña | Validaciones de propietario/participante con usuario autenticado en publicaciones, recolecciones, chat, calificaciones, pagos, reclamos, transacciones, notificaciones e incentivos. |
| 55b-EP9 | Rate limiting | OK | memoria | `POST /api/auth/login` limitado y filtro general para endpoints mutables bajo `/api/**`. |

## EP1 - Publicacion de material reciclable

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 01-EP1 | Registrar material reciclable | OK | publicacion, publicacion_material, material, usuario | `POST /api/publicaciones`; luego `POST /api/publicaciones/{idPublicacion}/materiales`. Validar con `GET /api/publicaciones/{id}` y `GET /api/publicaciones/{idPublicacion}/materiales`. |
| 02-EP1 | Registrar observaciones del material | OK | publicacion | `PUT /api/publicaciones/{id}/observaciones` con `{ "observaciones": "..." }`. |
| 03-EP1 | Sugerir clasificacion del material | OK | material | `GET /api/materiales/clasificar?texto=...` o `GET /api/materiales/sugerencia?texto=...`; usa busqueda por nombre, categoria y descripcion. |
| 04-EP1 | Validar informacion registrada | OK | publicacion, publicacion_material | `POST /api/publicaciones/{id}/validar`; valida campos obligatorios, ubicacion, descripcion/observaciones y materiales con cantidad mayor a cero. |
| 06-EP1 | Seleccionar fecha de disponibilidad | OK | publicacion | `PUT /api/publicaciones/{id}/fecha-disponibilidad`; rechaza fechas pasadas. |
| 14-EP1 | Chatear con recolector asignado | OK | mensaje_chat, usuario, recoleccion | `POST /api/mensajes-chat` con `idRecoleccion`, `GET /api/mensajes-chat/recoleccion/{idRecoleccion}` y conversacion por usuarios. |

## EP2 - Recoleccion y entrega

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 07-EP2 | Confirmar entrega mediante QR | OK | recoleccion, publicacion, publicacion_material, transaccion_puntos, usuario | `GET /api/recolecciones/{id}/qr`; `POST /api/recolecciones/{id}/validar-qr`; `POST /api/recolecciones/{id}/confirmar-entrega`. Confirma, marca completada y suma puntos. |
| 08-EP2 | Reprogramar recoleccion | OK | recoleccion | `PUT /api/recolecciones/{id}/reprogramar`; valida fecha futura y bloquea estados completado/cancelado. |
| 09-EP2 | Cancelar recoleccion programada | OK | recoleccion, publicacion | `PUT /api/recolecciones/{id}/cancelar`; impide cancelar completadas y reactiva la publicacion. |
| 10-EP2 | Visualizar perfil del recolector | OK | usuario, calificacion, recoleccion | `GET /api/usuarios/{id}/perfil`; `GET /api/calificaciones/recolector/{idRecolector}/reputacion`. |
| 11-EP2 | Calificar servicio de recoleccion | OK | calificacion, recoleccion, usuario | `POST /api/recolecciones/{id}/calificacion` y `POST /api/calificaciones`; valida participante, puntuacion 1-5 y evita duplicados por recoleccion/autor. |
| 12-EP2 | Reportar incidencia en recoleccion | OK | recoleccion | `PUT /api/recolecciones/{id}/incidencia`. |
| 13-EP2 | Marcar recoleccion como prioritaria | OK | recoleccion | `PUT /api/recolecciones/{id}/prioridad`. |

## EP3 - Puntos, incentivos y certificados

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 15-EP3 | Acumular puntos por reciclaje | OK | usuario, transaccion_puntos, recoleccion, publicacion_material | `POST /api/recolecciones/{id}/confirmar-entrega`; luego `GET /api/transacciones-puntos/usuario/{idUsuario}` y `GET /api/usuarios/{id}/perfil`. |
| 16-EP3 | Canjear puntos por recompensas | OK | incentivo, usuario_incentivo, transaccion_puntos, usuario | `POST /api/incentivos/{id}/canjear/{idUsuario}`. |
| 17-EP3 | Recompensas diarias y desafios | OK | incentivo, usuario_incentivo, transaccion_puntos | `GET /api/incentivos/desafios`, `GET /api/usuario-incentivos/progreso/{idUsuario}`, `PUT /api/usuario-incentivos/{id}/reclamar`, `POST /api/usuario-incentivos/recompensa-diaria/{idUsuario}`. |
| 18-EP3 | Tienda de puntos | OK | incentivo | `GET /api/incentivos/recompensas`, `GET /api/incentivos/{id}`. |
| 19-EP3 | Recordatorios y estados de incentivos | OK | usuario_incentivo, incentivo | `GET /api/usuario-incentivos/recordatorios/{idUsuario}`, `GET /api/usuario-incentivos/progreso/{idUsuario}`. |
| 20-EP3 | Certificados digitales | OK | certificado, usuario | `GET /api/certificados`, `GET /api/certificados/usuario/{idUsuario}`, `GET /api/certificados/{id}/pdf`. |
| 21-EP3 | Historial de incentivos | OK | transaccion_puntos | `GET /api/transacciones-puntos/usuario/{idUsuario}` y filtro `?tipo=ganado|canje`. |
| 22-EP3 | Notificaciones de logros y recompensas | OK | notificacion | Se generan notificaciones al completar recoleccion, reclamar incentivo/canje/recompensa diaria; tambien `POST /api/notificaciones/logros/{idUsuario}`. |
| 23-EP3 | Niveles de participacion | OK | usuario, transaccion_puntos | `GET /api/usuarios/{id}/perfil` y `PUT /api/usuarios/{id}/nivel`; recalcula por umbrales de puntos. |
| 24-EP3 | Detalle del valor de puntos por accion | OK | material, transaccion_puntos | `GET /api/transacciones-puntos/valores`, `GET /api/materiales`. |
| 25-EP3 | Filtrar certificados por dificultad | OK | certificado | `GET /api/certificados/dificultad/{nivelDificultad}`. |

## EP4 - Busqueda, mapa y rutas

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 26-EP4 | Visualizar mapa de recoleccion | OK | publicacion | `GET /api/publicaciones/mapa?lat=&lng=&radioKm=&material=&categoria=&tipoPunto=`. |
| 27-EP4 | Filtrar por categoria de material | OK | publicacion, material | `GET /api/publicaciones/categoria-material/{categoria}`. |
| 28-EP4 | Buscador de puntos de recoleccion | OK | publicacion, usuario | `GET /api/publicaciones/buscar?texto=plastico`. |
| 29-EP4 | Lista de puntos de reciclaje | OK | publicacion | `GET /api/publicaciones/activos` lista puntos activos; `GET /api/publicaciones/lista` queda como listado general. |
| 30-EP4 | Detalle del punto seleccionado | OK | publicacion, publicacion_material, calificacion | `GET /api/publicaciones/{id}/detalle` devuelve publicacion, materiales y reputacion/calificaciones relacionadas. |
| 31-EP4 | Generar ruta optima | OK | publicacion, ruta | `GET /api/rutas/optima?lat=&lng=&limite=5`. |
| 32-EP4 | Visualizar detalles de la ruta | OK | ruta | `GET /api/rutas/{id}/detalle`. |
| 33-EP4 | Guardar rutas personalizadas | OK | ruta, usuario | `POST /api/rutas`, `GET /api/rutas/usuario/{idUsuario}`, `DELETE /api/rutas/{id}`. |
| 34-EP4 | Seguimiento del recolector en tiempo real | OK | recoleccion, ubicacion_recolector_historial | `GET /api/recolecciones/{id}/seguimiento`, `PUT /api/recolecciones/{id}/ubicacion`, `GET /api/recolecciones/{id}/ubicacion/historial`. |

## EP5 - Perfil y verificacion

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 36-EP5 | Completar datos del perfil | OK | usuario | `PUT /api/usuarios/{id}/datos-basicos`, `/disponibilidad`, `/foto`, `/tipo-cuenta`, `/descripcion`, `GET /api/usuarios/{id}/perfil`. |
| 37-EP5 | Subir documentacion de verificacion | OK | documento_verificacion, usuario | `POST /api/documentos-verificacion`, `GET /api/documentos-verificacion/usuario/{idUsuario}`, `PUT /api/documentos-verificacion/{id}/revisar`. |

## EP6 - Reportes e historial

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 43-EP6 | Historial de materiales reciclados | OK | publicacion, publicacion_material, recoleccion | `GET /api/publicaciones/historial/{idUsuario}`, `GET /api/estadisticas/materiales`. |
| 44-EP6 | Reporte de actividades del perfil | OK | recoleccion, publicacion, transaccion_puntos | `GET /api/estadisticas/usuario/{idUsuario}`. |
| 45-EP6 | Historial de actividades con filtros | OK | recoleccion | `GET /api/recolecciones/historial?idUsuario=&fechaInicio=&fechaFin=&estado=&material=` usa la query del repository. |
| 46-EP6 | Descargar comprobantes PDF/CSV | OK | recoleccion, transaccion_puntos, transaccion_dinero | `GET /api/comprobantes/{tipo}/{id}.csv`, `GET /api/comprobantes/{tipo}/{id}.pdf`. |
| 47-EP6 | Historial con detalle completo | OK | recoleccion, publicacion, usuario | `GET /api/recolecciones/{id}/detalle` y `GET /api/recolecciones/historial` devuelven recoleccion, publicacion, generador, recolector, materiales y calificaciones. |

## EP7 - Soporte y reclamos

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 48-EP7 | Centro de soporte FAQ y contacto | OK | faq, soporte_contacto, reclamo | `GET /api/soporte/faq`, `GET /api/soporte/contactos`, `POST /api/soporte/tickets`. |
| 49-EP7 | Abrir y gestionar reclamos | OK | reclamo, usuario | `POST /api/reclamos`, `GET /api/reclamos/usuario/{idUsuario}`, `GET /api/reclamos/{id}`, `PUT /api/reclamos/{id}`. |
| 50-EP7 | Contacto telefonico con soporte | OK | soporte_contacto | `GET /api/soporte/contactos`. |

## EP8 - API para integracion/frontend

| ID | Historia | Estado | Tablas | Endpoints y escenarios |
| --- | --- | --- | --- | --- |
| 52-EP8 | CRUD publicaciones via API | OK | publicacion, publicacion_material | `GET/POST/PUT/DELETE /api/publicaciones`; materiales en `/api/publicaciones/{id}/materiales`. |
| 53-EP8 | CRUD recolecciones via API | OK | recoleccion | `GET/POST/PUT/DELETE /api/recolecciones`. |
| 54-EP8 | Ruta optima via API | OK | publicacion, ruta | `GET /api/rutas/optima`. |
| 57-EP8 | Subir imagenes via API | OK | filesystem uploads | `POST /api/archivos` multipart `file`; valida imagen, tipo MIME JPG/PNG/WEBP/GIF y tamano maximo 5 MB. |
| 58-EP8 | Notificaciones push via API | OK | notificacion | `POST /api/notificaciones/push`, `POST /api/notificaciones`, `GET /api/notificaciones/usuario/{idUsuario}`; registra envio push API y estado `enviada`. |
| 59-EP8 | Generar y validar QR via API | OK | recoleccion | `GET /api/recolecciones/{id}/qr`, `POST /api/recolecciones/{id}/validar-qr`, `POST /api/recolecciones/{id}/confirmar-entrega`. |
| 60-EP8 | Estadisticas via API | OK | recoleccion, publicacion, material, transaccion_puntos | `GET /api/estadisticas/globales`, `/materiales`, `/usuario/{idUsuario}`. |

## Verificacion contra capturas del backlog

Las capturas recibidas fueron cruzadas contra esta matriz. El resultado es que las historias del backlog estan incluidas en este archivo. Adicionalmente se conserva `03-EP1` como historia agregada por el requisito de queries para clasificacion/sugerencia de material.

| Bloque | Historias vistas en capturas | Estado en este documento | Cobertura principal |
| --- | --- | --- | --- |
| Infraestructura | `SETUP-1`, `SETUP-2`, `SETUP-3` | Incluidas | Arranque Spring Boot, modelo JPA/CRUD base y nota externa para Git. |
| Seguridad EP9 | `51b-EP9`, `52b-EP9`, `53b-EP9`, `54b-EP9`, `55b-EP9`, `56b-EP9` | Incluidas | JWT/refresh/logout, BCrypt, roles, permisos por propietario y rate limit. |
| EP1 Publicacion | `01-EP1`, `02-EP1`, `04-EP1`, `06-EP1`, `14-EP1` | Incluidas | Publicacion, materiales, observaciones, validacion, fecha disponible y chat con `idRecoleccion`. |
| EP1 extra por queries | `03-EP1` | Agregada | Query de sugerencia/clasificacion de material: `GET /api/materiales/clasificar?texto=...`. |
| EP2 Recoleccion | `07-EP2`, `08-EP2`, `09-EP2`, `10-EP2`, `11-EP2`, `12-EP2`, `13-EP2` | Incluidas | QR, confirmar entrega, reprogramar, cancelar, perfil/reputacion, calificar, incidencias y prioridad. |
| EP3 Puntos e incentivos | `15-EP3` a `25-EP3` | Incluidas | Puntos, canje, desafios, recompensa diaria, tienda, recordatorios, certificados, historial, notificaciones, niveles y valores. |
| EP4 Busqueda/mapa/rutas | `26-EP4` a `34-EP4` | Incluidas | Mapa, filtros, busqueda, lista activa, detalle consolidado, ruta optima, ruta detalle, rutas guardadas y seguimiento del recolector. |
| EP5 Perfil/verificacion | `35-EP5`, `36-EP5`, `37-EP5` | Incluidas | Registro, completar perfil y documentacion de verificacion. |
| EP6 Reportes/historial | `43-EP6`, `44-EP6`, `45-EP6`, `46-EP6`, `47-EP6` | Incluidas | Materiales reciclados, estadisticas de perfil, historial filtrado, comprobantes y detalle completo. |
| EP7 Soporte | `48-EP7`, `49-EP7`, `50-EP7` | Incluidas | FAQ/contacto, tickets/reclamos y contacto telefonico. |
| EP8 API frontend | `52-EP8`, `53-EP8`, `54-EP8`, `57-EP8`, `58-EP8`, `59-EP8`, `60-EP8` | Incluidas | CRUD publicaciones/recolecciones, ruta optima, upload, push API, QR y estadisticas. |

### Equivalencias de endpoint vs capturas

Algunas tareas tecnicas de las capturas mencionan rutas sugeridas. En codigo se cubre la misma historia con estos endpoints equivalentes:

| Historia | Ruta sugerida en capturas | Ruta implementada/documentada |
| --- | --- | --- |
| `10-EP2` Perfil/reputacion del recolector | `/api/recolecciones/promedio-recolector/{id}` | `GET /api/usuarios/{id}/perfil` y `GET /api/calificaciones/recolector/{idRecolector}/reputacion` |
| `17-EP3` Recompensa diaria/desafios | `/api/incentivos/recompensa-diaria/...` y `/api/incentivos/desafios/{id}/reclamar` | `POST /api/usuario-incentivos/recompensa-diaria/{idUsuario}` y `PUT /api/usuario-incentivos/{id}/reclamar` |
| `43-EP6` Historial de materiales reciclados | Estadisticas kg por mes/materiales por mes | `GET /api/publicaciones/historial/{idUsuario}` y `GET /api/estadisticas/materiales` |
| `45-EP6` Historial con filtros | `/api/recolecciones/historial/{id}` | `GET /api/recolecciones/historial?idUsuario=&fechaInicio=&fechaFin=&estado=&material=` |
| `47-EP6` Historial detallado | `/api/usuarios/{id}/actividades` | `GET /api/recolecciones/historial` y `GET /api/recolecciones/{id}/detalle` |
| `58-EP8` Push via API | `/api/notificaciones/enviar` | `POST /api/notificaciones/push` y `POST /api/notificaciones` |

## Queries alineados a user stories

Hay 22 queries `@Query` en repositories, por encima del minimo de 12. Cada grupo queda asociado a historias concretas:

| Repository | Queries/uso | User stories cubiertas |
| --- | --- | --- |
| `IMaterialRepository` | Sugerencia por nombre, categoria y descripcion | `03-EP1`, `24-EP3` |
| `IPublicacionRepository` | Busqueda, mapa, cercanas, categoria, historial | `26-EP4`, `27-EP4`, `28-EP4`, `29-EP4`, `43-EP6` |
| `IPublicacionMaterialRepository` | Kg por categoria/material | `43-EP6`, `60-EP8` |
| `IRecoleccionRepository` | Conteo de actividades e historial filtrado | `44-EP6`, `45-EP6`, `47-EP6` |
| `ICalificacionRepository` | Calificaciones por recolector para reputacion | `10-EP2`, `11-EP2` |
| `IIncentivoRepository` | Incentivos activos y activos por tipo | `16-EP3`, `17-EP3`, `18-EP3` |
| `IUsuarioIncentivoRepository` | Progreso y recordatorios | `17-EP3`, `19-EP3`, `22-EP3` |
| `ITransaccionPuntosRepository` | Historial, filtro por tipo, suma y recompensa diaria | `15-EP3`, `17-EP3`, `21-EP3`, `23-EP3` |
| `ICertificadoRepository` | Certificados por usuario y dificultad | `20-EP3`, `25-EP3` |

## Flujo recomendado de prueba end-to-end

1. `POST /api/auth/login` como `admin` y guardar token.
2. `GET /api/materiales` para confirmar catalogo.
3. `POST /api/auth/register` para crear un usuario generador.
4. `POST /api/publicaciones` para crear publicacion.
5. `POST /api/publicaciones/{idPublicacion}/materiales` para asociar material y kg.
6. `POST /api/publicaciones/{idPublicacion}/validar` para validar datos.
7. `PUT /api/publicaciones/{idPublicacion}/fecha-disponibilidad`.
8. `GET /api/publicaciones/buscar?texto=plastico`, `/mapa`, `/categoria-material/plastico`.
9. `POST /api/recolecciones` para asignar recolector.
10. `GET /api/recolecciones/{id}/qr`.
11. `POST /api/recolecciones/{id}/validar-qr`.
12. `POST /api/recolecciones/{id}/confirmar-entrega`.
13. `GET /api/transacciones-puntos/usuario/{idUsuario}?tipo=ganado` y `GET /api/usuarios/{id}/perfil`.
14. `POST /api/calificaciones` y `GET /api/calificaciones/recolector/{idRecolector}/reputacion`.
15. `GET /api/incentivos/recompensas` y `POST /api/incentivos/{id}/canjear/{idUsuario}`.
16. `GET /api/certificados/usuario/{idUsuario}` y `GET /api/certificados/{id}/pdf`.
17. `GET /api/estadisticas/usuario/{idUsuario}`.
18. `GET /api/soporte/faq`, `POST /api/soporte/tickets`, `GET /api/reclamos/usuario/{idUsuario}`.

## Brechas principales para dejar 100% alineado

Las brechas funcionales detectadas quedaron cerradas en codigo y endpoints. Mantener esta seccion para registrar nuevas brechas si aparecen en pruebas de Postman o QA.
