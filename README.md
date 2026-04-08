# LUJURIA WEB

Backend inicial para una plataforma tipo marketplace + streaming + chat persistente en tiempo real.

## Stack actual

- Java 17
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA
- WebSockets con STOMP
- MySQL para desarrollo
- H2 para pruebas
- Flyway para migraciones

## Módulos backend incluidos

- Autenticación y registro
- Perfil de usuario
- Perfil de creador
- Streaming simulado
- Marketplace de servicios
- Agendamiento de servicios
- Tienda de productos
- Órdenes y pagos simulados
- Chat persistente con WebSocket

## Arranque rápido

1. Levanta MySQL:

```bash
docker compose up -d
```

2. Ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

3. Prueba la compilación:

```bash
./mvnw test
```

4. Frontend React:

```bash
cd frontend
npm install
npm run dev
```

## Documentación

- Arquitectura: [docs/architecture.md](/home/camper/Documentos/LUJURIA/docs/architecture.md)
- Roadmap frontend React: [docs/frontend-roadmap.md](/home/camper/Documentos/LUJURIA/docs/frontend-roadmap.md)
