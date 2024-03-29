# Establece la imagen base como openjdk:17-jdk-alpine y la renombra como "builder"
FROM openjdk:17-jdk-alpine as builder

# Establece el directorio de trabajo dentro del contenedor como /app/msvc-usuario
WORKDIR /app/msvc-usuario

# Copia el archivo pom.xml al directorio /app
COPY ./pom.xml /app
# Copia los archivos .mvn, mvnw, y pom.xml del proyecto al directorio /app/msvc-usuario
COPY ./msvc-usuario/.mvn ./.mvn
COPY ./msvc-usuario/mvnw .
COPY ./msvc-usuario/pom.xml .

# Ejecuta el comando Maven para compilar el proyecto y empaquetar el archivo JAR
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
# Opcional: Descarga las dependencias sin ejecutar pruebas. Puedes descomentar la línea siguiente si es necesario.
# RUN ./mvnw dependency:go-offline
# Copia el contenido del directorio src del proyecto al directorio /app/msvc-usuario/src
COPY ./msvc-usuario/src ./src

# Ejecuta nuevamente el comando Maven para empaquetar el archivo JAR final
RUN ./mvnw clean package -DskipTests

# Establece una nueva imagen base como openjdk:17j-dk-alpine
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo dentro deWORKDIR /appl contenedor como /app

# Copia el archivo JAR compilado desde la imagen "builder" al directorio /app
COPY --from=builder /app/msvc-usuario/target/msvc-usuario-0.0.1-SNAPSHOT.jar .
# Expone el puerto 8001 para que sea accesible desde fuera del contenedor
EXPOSE 8001

# Establece el comando de entrada para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "msvc-usuario-0.0.1-SNAPSHOT.jar"]