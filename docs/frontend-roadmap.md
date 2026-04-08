# Roadmap Frontend React

## Objetivo

Construir una SPA que consuma el backend actual sin duplicar lógica de negocio.

## Stack recomendado

- React con Vite
- React Router
- Axios
- STOMP sobre WebSocket
- Estado local simple al inicio

Más adelante:

- TanStack Query para cache y sincronización
- Zustand para estado global ligero

## Estructura sugerida

```text
frontend/
  src/
    app/
    components/
    features/
      auth/
      streams/
      services/
      chat/
      store/
      profile/
    layouts/
    pages/
    lib/
```

## Rutas iniciales

- `/` landing pública
- `/login`
- `/dashboard/streams`
- `/dashboard/services`
- `/dashboard/store`
- `/dashboard/chat`
- `/dashboard/profile`
- `/dashboard/settings`

## Orden de construcción

1. Landing con modal obligatorio de +18 y políticas.
2. Login y registro.
3. Layout dashboard reutilizable.
4. Pantalla de streams con filtros.
5. Pantalla de servicios y reserva.
6. Pantalla de chat con lista de conversaciones y mensajes.
7. Perfil y configuración.
8. Tienda y órdenes.

## Principios UX para retención

- El chat siempre debe estar a uno o dos clics.
- Después de reservar un servicio, el usuario debe quedar dentro de una conversación.
- Cada módulo debe empujar al siguiente paso natural:
  stream -> mensaje -> reserva -> pago -> seguimiento.
- Mostrar actividad reciente y conversaciones activas en dashboard.

## Integraciones frontend-backend

REST:

- Auth: login, register, me.
- Streams: list/create.
- Services: list/create/bookings.
- Products: list/create.
- Orders: create/list.
- Chat: conversations/messages.

Tiempo real:

- WebSocket endpoint `/ws`
- App destination `/app/chat/{conversationId}`
- Topic `/topic/conversations/{conversationId}`
- User queue `/user/queue/messages`

## Buenas prácticas para ti como junior

- Nunca pongas URLs “quemadas” por toda la app. Usa un cliente API central.
- Mantén componentes pequeños y páginas delgadas.
- Separa UI de llamadas HTTP.
- Maneja token en un único helper.
- No mezcles lógica del chat con la vista.
- Empieza simple y refactoriza cuando duela, no antes.
