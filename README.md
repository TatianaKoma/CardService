# greedobank-kharkiv/card-service

## Getting Started

To run project specify your DB_USER, DB_PASSWORD;
DB_URL=jdbc:postgresql://localhost:5433/greedobank_card?createDatabaseIfNotExist=true&autoReconnect=true;

PostgreSQL 14.3 version

To launch this app in isolation mode:
1. start your docker
2. docker build .
3. docker run -p 8080:8080 --env DB_URL="jdbc:postgresql://host.docker.internal:5433/greedobank_card?createDatabaseIfNotExist=true&autoReconnect=true" --env DB_USER=postgres --env DB_PASSWORD=postgres --env USER_CLI
   ENT_URL="http://host.docker.internal:8081/api/v1/users?email=" {your image ID}

## Documentation

It's available within http://localhost:8080/swagger-ui/index.html
