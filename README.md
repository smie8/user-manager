# user-manager

Service that exposes REST endpoints to manage a user and user groups database.
Clients can maintain users and user groups, and associate users to groups and vice versa.

## Running the application locally
Prerequisites:
- Git installed on your machine
- JDK 17 installed on your machine

1. Clone the repository: `git clone <repo-url>`
2. Navigate to the repo directory
3. Build the application using Gradle wrapper: `./gradlew build`
4. Run the application: `./gradlew bootrun`
5. The application will be running on `http://localhost:8080`

## API documentation
You can access the API documentation at: `http://localhost:8080/swagger-ui.html`,
while the application is running.

## Invoking the API
The API can be invoked using any REST client such as curl, Postman or Thunder Client in VS Code.

Example using curl:
- Get all users:
  `curl http://localhost:8080/api/v1/user`
- Create new user:
  `curl -X POST curl http://localhost:8080/api/v1/user -H "Content-Type: application/json" -d '{"name": "John Doe"}'`