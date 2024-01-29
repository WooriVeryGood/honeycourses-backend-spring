# Dockerfile

# jdk17 Image Start
FROM openjdk:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# REST Doc 문서화 html 복사
COPY build/docs/asciidoc/index.html /app/static/docs

# Working directory 세팅
WORKDIR /app


# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]