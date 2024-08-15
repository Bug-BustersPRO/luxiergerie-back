FROM openjdk:21

# Install Maven

ENV MAVEN_HOME=/usr/share/maven
RUN set -e -u -x \
    && mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz https://apache.osuosl.org/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Copy sources

WORKDIR /app
COPY . /app

# Install dependencies

RUN set -e -u -x \
    && mvn clean install -DskipTests \
    && ln -s /app/target/*.jar /app/target/app.jar

# Configure Docker

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/target/app.jar"]