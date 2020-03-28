FROM gradle:jdk8
WORKDIR /module
COPY . .
CMD ["./gradlew", "bootRun"]
