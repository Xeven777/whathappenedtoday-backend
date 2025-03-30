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

## Hosting the API

You can deploy this API in several ways:

### Local Deployment

1. Build the application:

   ```bash
   ./mvnw clean package
   ```

2. Run the JAR file:
   ```bash
   java -jar target/whathappenedtoday-backend-0.0.1-SNAPSHOT.jar
   ```

### Cloud Platforms

#### Railway

1. Connect your GitHub repository to Railway
2. Set environment variables in the Railway dashboard
3. Railway will automatically deploy your application

### Docker Deployment

1. Create a `Dockerfile` in the project root:

   ```dockerfile
   FROM eclipse-temurin:21-jdk
   WORKDIR /app
   COPY target/whathappenedtoday-backend-0.0.1-SNAPSHOT.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. Build and run the Docker image:
   ```bash
   docker build -t whathappenedtoday-api .
   docker run -p 8080:8080 -e MONGODB_URI=your_mongodb_uri -e MONGODB_DATABASE=your_database_name whathappenedtoday-api
   ```

### Kubernetes Deployment

For scaling in production, you can deploy to Kubernetes:

1. Create a deployment file `kubernetes/deployment.yaml`:

   ```yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: whathappenedtoday-api
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: whathappenedtoday-api
     template:
       metadata:
         labels:
           app: whathappenedtoday-api
       spec:
         containers:
           - name: whathappenedtoday-api
             image: whathappenedtoday-api:latest
             ports:
               - containerPort: 8080
             env:
               - name: MONGODB_URI
                 valueFrom:
                   secretKeyRef:
                     name: mongodb-credentials
                     key: uri
               - name: MONGODB_DATABASE
                 valueFrom:
                   secretKeyRef:
                     name: mongodb-credentials
                     key: database
   ```

2. Apply the configuration:
   ```bash
   kubectl apply -f kubernetes/deployment.yaml
   ```

### AWS Elastic Beanstalk

1. Install the EB CLI
2. Initialize EB in your project:
   ```bash
   eb init
   ```
3. Deploy the application:
   ```bash
   eb create
   ```
4. Set environment variables:
   ```bash
   eb setenv MONGODB_URI=your_mongodb_uri MONGODB_DATABASE=your_database_name
   ```

Remember to ensure your MongoDB instance is accessible from your hosting environment, and properly secure your database connection string in production.
