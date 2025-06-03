FROM openjdk:17-jdk-slim

# 작업 디렉토리 지정
WORKDIR /app

# jar 파일을 동적으로 복사 (빌드 시 파일명을 고정하지 않아도 됨)
COPY build/libs/*.jar app.jar

# 환경변수를 외부에서 넣을 수 있게 ENTRYPOINT 사용
ENTRYPOINT ["java", "-jar", "app.jar"]
