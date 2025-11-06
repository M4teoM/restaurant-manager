# Runtime Java 17 (ligero)
FROM eclipse-temurin:17-jre-jammy

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR construido por Maven
# (usa comodín para no depender del nombre exacto)
COPY target/*.jar app.jar

# Opcional: documenta el puerto si luego expones HTTP
EXPOSE 8080

# Opciones de JVM (ajústalas si quieres)
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# Arranque por clase principal (sin MANIFEST)
CMD ["sh","-c","java $JAVA_OPTS -cp app.jar com.restaurant.manager.Restaurant"]