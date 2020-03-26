FROM gradle:jdk-8
WORKDIR /module
COPY . .
CMD ["./gradlew", "bootRun"]
