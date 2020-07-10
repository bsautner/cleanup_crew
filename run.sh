./gradlew clean build
scp ./build/libs/cleanup_crew-1.0.jar
java -cp ./build/libs/cleanup_crew-1.0.jar com.colony.Application

