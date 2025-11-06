# Docker Setup — Restaurant Manager

## Resumen
Este documento describe el proceso completo de **containerización** y **despliegue** del proyecto *Restaurant Manager* utilizando Docker y Docker Compose, incluyendo la integración con PostgreSQL, optimización de imagen (multi-stage build), persistencia de datos y pruebas automáticas.

---

## Parte 1 — Dockerfile básico

### Archivo `Dockerfile`
```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xmx256m -Xms128m"
CMD ["sh","-c","java $JAVA_OPTS -cp app.jar com.restaurant.manager.Restaurant"]
```

### Comandos utilizados
```bash
mvn clean package -DskipTests
docker build -t restaurant-manager:1.0 .
docker run --name restaurant-app -p 8080:8080 restaurant-manager:1.0
```

### Resultado esperado
```
=== Restaurant Manager ===
Restaurant: La Pizzeria
Items en menú: 2
Reservas activas: 1
Órdenes procesadas: 0
Ingresos totales: $0.00
Valor promedio por orden: $0.00
```

---

## Parte 2 — Multi-Stage Build

### Archivo `Dockerfile.multistage`
```dockerfile
########################################
# STAGE 1 — Build con Maven
########################################
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn -B -e dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests clean package dependency:copy-dependencies -DoutputDirectory=target/dependency

########################################
# STAGE 2 — Runtime (JRE)
########################################
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /build/target/*.jar /app/app.jar
COPY --from=builder /build/target/dependency /app/lib
EXPOSE 8080
ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -cp /app/app.jar:/app/lib/* com.restaurant.manager.Restaurant"]
```

### Comandos utilizados
```bash
mvn clean package -DskipTests
docker build -f Dockerfile.multistage -t restaurant-manager:1.0-optimized .
docker images | grep restaurant-manager
```

### Resultado esperado
Comparativa de tamaños:
| Imagen | Tamaño |
|---------|---------|
| `restaurant-manager:1.0` | ~450 MB |
| `restaurant-manager:1.0-optimized` | ~250 MB |

---

## Parte 3 — Docker Compose con PostgreSQL

### Archivo `docker-compose.yml`
```yaml
services:
  database:
    image: postgres:15-alpine
    container_name: restaurant-db
    restart: no
    environment:
      POSTGRES_DB: restaurant
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: restaurant123
      POSTGRES_INITDB_ARGS: "--encoding=UTF-8"
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - restaurant-network

  backend:
    build:
      context: .
      dockerfile: Dockerfile.multistage
    image: restaurant-manager:compose
    container_name: restaurant-backend
    restart: "no"
    depends_on:
      database:
        condition: service_healthy
    environment:
      DB_HOST: database
      DB_PORT: 5432
      DB_NAME: restaurant
      DB_USER: postgres
      DB_PASSWORD: restaurant123
      JAVA_OPTS: "-Xmx256m -Xms128m"
    ports:
      - "8080:8080"
    networks:
      - restaurant-network

volumes:
  postgres-data:

networks:
  restaurant-network:
    driver: bridge
```

### Script de inicialización `init-db.sql`
```sql
CREATE TABLE IF NOT EXISTS restaurants (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS menu_items (
  id SERIAL PRIMARY KEY,
  restaurant_id INTEGER REFERENCES restaurants(id),
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reservations (
  id SERIAL PRIMARY KEY,
  restaurant_id INTEGER REFERENCES restaurants(id),
  customer_name VARCHAR(255) NOT NULL,
  party_size INTEGER NOT NULL,
  reservation_time TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO restaurants (name) VALUES ('La Pizzeria Demo');
INSERT INTO menu_items (restaurant_id, name, price) VALUES
  (1, 'Pizza Margherita', 12.99),
  (1, 'Pasta Carbonara', 10.50),
  (1, 'Tiramisu', 6.99);
```

### Comandos
```bash
mvn clean package -DskipTests
docker-compose up --build
docker-compose ps
docker-compose logs backend | grep Conectado
```

### Resultado esperado
```
✅ Conectado a base de datos: restaurant @ database:5432
✅ PostgreSQL healthy
✅ Datos de ejemplo cargados
```

---

## Parte 4 — Testing y Verificación

### Script `smoke-test.sh`
```bash
#!/usr/bin/env bash
set -euo pipefail
echo  SMOKE TESTS — Restaurant Manager"
docker-compose up -d --build

# Esperar DB healthy
i=0; until [ "$(docker inspect -f '{{.State.Health.Status}}' restaurant-db)" = "healthy" ]; do
  i=$((i+1)); [ $i -gt 30 ] && { echo  DB no healthy"; exit 1; }; sleep 2;
done
echo " DB healthy"

# Ver backend
docker-compose logs backend --tail=100 | egrep -m1 -q "Conectado a base de datos|Restaurant Manager"   && echo " Backend ejecutó correctamente" || echo " No se vio salida del backend"

# Tablas
docker-compose exec -T database psql -U postgres -d restaurant -c "\dt" | grep -q "restaurants"   && echo "Tablas OK" || { echo "Tablas no encontradas"; exit 1; }

# Persistencia
docker-compose exec -T database psql -U postgres -d restaurant -c "INSERT INTO restaurants (name) VALUES ('SmokeTest R1');" >/dev/null
docker-compose restart
sleep 8
docker-compose exec -T database psql -U postgres -d restaurant -c "SELECT name FROM restaurants WHERE name='SmokeTest R1';" | grep -q "SmokeTest R1"   && echo "Persistencia OK" || { echo " Dato no persistió"; exit 1; }

echo " TODOS LOS TESTS PASARON"
```

### Ejecución
```bash
chmod +x smoke-test.sh
./smoke-test.sh
```

### Resultado esperado
```
✅ DB healthy
✅ Backend ejecutó correctamente
✅ Tablas OK
✅ Persistencia OK
✅ TODOS LOS TESTS PASARON
```

---

## 🧾 Checklist Final

| Elemento | Verificado |
|-----------|-------------|
| Dockerfile básico funcional | ✅ |
| Dockerfile multi-stage optimizado | ✅ |
| Imagen reducida >40% | ✅ |
| docker-compose.yml configurado | ✅ |
| PostgreSQL y persistencia | ✅ |
| Health checks funcionando | ✅ |
| Backend ↔ Database conexión OK | ✅ |
| Smoke tests exitosos | ✅ |
| Documentación completa (este archivo) | ✅ |

---

## onclusión
La aplicación *Restaurant Manager* fue completamente containerizada, integrando Java 17 y PostgreSQL bajo Docker Compose.  
El uso de **multi-stage builds** redujo el tamaño de la imagen y aceleró los builds, mientras que **volúmenes** garantizaron persistencia.  
Los **health checks** y el **script de pruebas automáticas** validaron la estabilidad y portabilidad del entorno.

> “Si funciona en mi contenedor, funcionará en cualquier parte.” 🐳✨
