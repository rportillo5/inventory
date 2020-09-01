# Dockerfile - Inventory
FROM openjdk:8-jdk-alpine AS build
WORKDIR /workspace/app

COPY . /workspace/app
# Gradle -- RUN ./gradlew --build-cache clean build
RUN ./mvnw install -DskipTests
# Gradle -- RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
# create new user so that micro service does not run as root
RUN addgroup -S cdxms && adduser -S cdxms -G cdxms
USER cdxms:cdxms

VOLUME /tmp
# Gradle -- ARG DEPENDENCY=/workspace/app/build/dependency
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.banderasmusic.rest.inventory.InventoryApplication"]