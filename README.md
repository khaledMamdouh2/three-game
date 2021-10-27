# Three Game

### Description
- Simple game consisting of two players communicating through Rest API.
- for more details please check the document (Game of Three - Scoober Coding Challenge JAVA (1).pdf ) added in the repository.
### Programming language, technologies and libraries

- Java 11
- Gradle
- Spring Boot
- lombok

### Prerequisites

Java 11 and gradle should be installed

### How to run the project

Navigate to the project directory and open your command-line or terminal program then type the following commands:
- build the project

```shellscript
gradle build
```
- run the first player application
```shellscript
java -jar build/libs/three-game-0.0.1-SNAPSHOT.jar
```
- run the second player application
```shellscript
java -jar build/libs/three-game-0.0.1-SNAPSHOT.jar --spring.config.name=application_player2
```
- start the game from the browser by typing one of the following URLs depends on the following options:
1. first player start the game with automatic selection of the initial number
```
http://localhost:8080/threegame/start-game/automatic/false 
```
2. first player start the game with manual selection of the initial number (54 for example)
```
http://localhost:8080/threegame/start-game/manual/54 
```
3. second player start the game with automatic selection of the initial number
```
http://localhost:8081/threegame/start-game/automatic/false 
```
4. second player start the game with manual selection of the initial number (100 for example)
```
http://localhost:8081/threegame/start-game/manual/100
```
