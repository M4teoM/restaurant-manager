#!/usr/bin/env bash
set -euo pipefail
echo "🧪 SMOKE TESTS — Restaurant Manager"
docker-compose up -d --build

# Esperar DB healthy (máx ~60s)
i=0; until [ "$(docker inspect -f '{{.State.Health.Status}}' restaurant-db)" = "healthy" ]; do
  i=$((i+1)); [ $i -gt 30 ] && { echo "❌ DB no healthy"; exit 1; }; sleep 2;
done
echo "✅ DB healthy"

# Ver salida del backend (puede terminar con exit 0)
docker-compose logs backend --tail=100 | egrep -m1 -q "Conectado a base de datos|Restaurant Manager" \
  && echo "✅ Backend ejecutó correctamente" || echo "⚠️ No se vio salida del backend"

# Verificar tablas
docker-compose exec -T database psql -U postgres -d restaurant -c "\dt" | grep -q "restaurants" \
  && echo "✅ Tablas OK" || { echo "❌ Tablas no encontradas"; exit 1; }

# Probar persistencia
docker-compose exec -T database psql -U postgres -d restaurant -c \
"INSERT INTO restaurants (name) VALUES ('SmokeTest R1');" >/dev/null
docker-compose restart
sleep 8
docker-compose exec -T database psql -U postgres -d restaurant -c \
"SELECT name FROM restaurants WHERE name='SmokeTest R1';" | grep -q "SmokeTest R1" \
  && echo "✅ Persistencia OK" || { echo "❌ Dato no persistió"; exit 1; }

echo "✅ TODOS LOS TESTS PASARON"
