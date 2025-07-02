# wishlist-manager

[en-US](README.md) | [pt-BR](README.pt-br.md)

A Spring Boot application for managing customer wishlists.  
Built using Clean Architecture principles, supports RESTful APIs and includes integration tests with Embedded MongoDB.

---

## Features

- Add products to a customer’s wishlist
- Remove products from a wishlist
- Check if a product exists in a wishlist
- Retrieve all products from a wishlist
- Maximum product limit validation

---

## Tech Stack

- Java 21
- Spring Boot 3.x
- Embedded MongoDB (`flapdoodle.embed.mongo`)
- JUnit 5 + MockMvc (Integration Tests)
- Maven

---

## Running the application

#### Requirements:
- Java 21+
- Maven 3.8+

### Setup

Clone the repository:
```bash 
git clone git@github.com:cesar-reb/wishlist-manager.git 
cd wishlist-manager
```

Build the project:
```mvn clean install```

Before running the application, make sure to configure the MongoDB connection URI.

You can define it in the `application.yml` file:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/wishlist
```
Adjust the URI as needed based on your local or production environment.

Run the application:
```mvn spring-boot:run```

The API will be available at `http://localhost:8080`.

## Running Tests
```mvn test```

## API Documentation (Swagger)

The interactive API documentation is available via Swagger UI.

### Access

After starting the application, go to:
```http://localhost:8080/swagger-ui.html```
or
```http://localhost:8080/swagger-ui/index.html```
## Contributing

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request