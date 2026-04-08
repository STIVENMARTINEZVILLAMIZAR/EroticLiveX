# Arquitectura Backend

## Visión general

La plataforma está diseñada como un backend monolítico modular. Esto significa que desplegamos una sola aplicación Spring Boot, pero el código está separado por dominios de negocio para poder escalar sin reescribir la base.

Los dominios actuales son:

- `auth`: registro, login, JWT y seguridad.
- `user`: perfil, preferencias y roles.
- `creator`: onboarding y perfil público de creadores.
- `stream`: transmisiones en vivo simuladas y filtros.
- `marketplace`: servicios y agendamiento.
- `commerce`: tienda, órdenes y pagos simulados.
- `chat`: conversaciones persistentes y WebSocket.
- `catalog`: categorías y etiquetas reutilizables.
- `config` y `common`: infraestructura compartida.

## Arquitectura por capas

Cada dominio sigue una estructura consistente:

- `controller`: expone endpoints REST o mensajes WebSocket.
- `service`: concentra reglas de negocio y coordinación entre módulos.
- `repository`: acceso a base de datos con Spring Data JPA.
- `entity`: modelo persistente.
- `dto`: contratos de entrada y salida.

Esta separación ayuda a:

- Evitar lógica de negocio en controladores.
- Reutilizar reglas para web y futuro móvil.
- Mantener el código entendible para un equipo creciendo.

## Flujo funcional central

El flujo principal que queremos optimizar es:

1. Usuario entra a la landing.
2. Acepta mayoría de edad y políticas.
3. Se registra o inicia sesión.
4. Descubre streams o servicios.
5. Interactúa por chat.
6. Agenda un servicio o compra un producto.
7. Paga dentro del sistema.
8. Continúa la conversación sin salir de la plataforma.

Este flujo favorece retención, recurrencia y monetización.

## Seguridad

La seguridad actual está pensada para API y SPA:

- `Spring Security` para protección de rutas.
- `JWT` para sesiones stateless.
- `PasswordEncoder` con BCrypt.
- Filtro HTTP para autenticar peticiones REST.
- Interceptor STOMP para autenticar WebSockets con el mismo token.

Esto permite que la lógica de negocio sea consumida por:

- Web React
- Futuro app móvil React Native o Flutter
- Integraciones administrativas

## Tiempo real

El chat usa:

- Persistencia en SQL para historial.
- WebSocket STOMP para entrega en tiempo real.
- `SimpMessagingTemplate` para broadcast a conversación y cola por usuario.

Beneficio práctico:

- Si el usuario vuelve después, el historial sigue allí.
- Si el usuario está conectado, recibe mensajes al instante.

## Modelo de datos

Entidades clave:

- `AppUser`
- `UserPreference`
- `CreatorProfile`
- `Category`
- `Tag`
- `LiveStream`
- `ServiceOffering`
- `ServiceBooking`
- `Product`
- `CustomerOrder`
- `OrderItem`
- `PaymentTransaction`
- `Conversation`
- `ConversationParticipant`
- `ChatMessage`

## Estrategia de escalabilidad

Decisiones pensadas para crecer:

- Monolito modular primero: entrega rápido y reduce complejidad inicial.
- DTOs claros: evita acoplar frontend al modelo JPA.
- JWT y REST: facilitan cliente web y móvil.
- Catálogo compartido: categorías y tags reutilizables entre módulos.
- Chat desacoplado del resto: más fácil mover luego a infraestructura dedicada si la carga crece.

Evolución futura recomendada:

1. Separar notificaciones.
2. Separar pagos reales en un módulo/adaptador externo.
3. Mover chat a broker dedicado si el tráfico aumenta.
4. Introducir caché para listados.
5. Agregar observabilidad y métricas.

## Endpoints base

Principales rutas ya disponibles:

- `/api/v1/auth/*`
- `/api/v1/users/me`
- `/api/v1/creators/*`
- `/api/v1/streams/*`
- `/api/v1/services/*`
- `/api/v1/products/*`
- `/api/v1/orders/*`
- `/api/v1/chat/conversations/*`
- `/ws` para WebSocket

## Siguiente paso recomendado

El siguiente bloque natural del proyecto es el frontend React consumiendo esta API:

1. Landing
2. Login/registro
3. Dashboard
4. Streams
5. Servicios
6. Chat
7. Perfil
