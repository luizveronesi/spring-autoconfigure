# Spring Autoconfigure

Java API template with Spring Boot, Spring Security for Google OAuth2 JWT and MongoDB database.

## Installation

```bash
# Clone the repository
git https://github.com/luizveronesi/spring-autoconfigure.git

# Navigate to the project directory
cd spring-autoconfigure

# Install dependencies
mvn install
```

```bash
# Docker installation
mvn clean package -f pom.xml -U
docker build . -t spring-autoconfigure-example:latest
docker create --name spring-autoconfigure-example --network your-network --ip x.x.x.x --restart unless-stopped spring-autoconfigure-example:latest bash
docker start tycho-sentence
```

## Usage

```bash
# Run the application
java -jar target/api.jar
```

## Configuration

You need to add your MongoDB Credentials at application.yml file, or add them as environment variables, as in the example.

MONGODB_USERNAME: your_username  
MONGODB_PASSWORD: your_password  
MONGODB_URI: mongodb://127.0.0.1:27017/database

If you change the package structure you need to update the Application.java in bot @ComponentScan and @EnableMongoRepositories annotations.

## Endpoint Examples

### GET /user/profile

Retrieves the logged user based on the given JWT.

## MongoDB Examples

|                   Name |   Type    | Description                                                          |
| ---------------------: | :-------: | -------------------------------------------------------------------- |
|            `User.java` |  Entity   | Representation of user MongoDB collection.                           |
|       `UserRepository` | Interface | MongoRepository interface with persistence methods.                  |
| `UserCustomRepository` | Interface | Interface for custom persistence methods.                            |
|   `UserRepositoryImpl` |   Class   | Custom implementation with methods using MongoTemplate and Criteria. |

## Next steps

Make OpenAPI available using Google JWT.
