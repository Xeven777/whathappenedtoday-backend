# What Happened Today - Backend

Backend service for the What Happened Today application.

## Development Setup

### Database Configuration

This application requires MongoDB. You can configure the database connection in one of these ways:

1. **Environment Variables** (recommended for production):

   - Set `MONGODB_URI` with your MongoDB connection string
   - Set `MONGODB_DATABASE` with your database name

2. **Development Profile**:
   - Create a file `src/main/resources/application-dev.properties`
   - Add your database configuration to this file (see template below)
   - Run the application with the dev profile: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`

Example `application-dev.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@host/database
spring.data.mongodb.database=whathappenedtoday
```

**NOTE**: Never commit your `application-dev.properties` file or any file containing credentials to Git!
