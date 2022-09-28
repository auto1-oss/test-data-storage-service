# test-data-storage-service

## How to run service locally

Preinstalled apps: **Docker**

1. ### Create DB
```
docker run --name postgres-db -e POSTGRES_PASSWORD=docker -p 5432:5432 -d postgres
```

2. ### Find IP address of created DB
```
docker network inspect bridge
```
Search and copy IP address without subnet mask from `postgres-db` container  
> Example `"Name": "postgres-db"` -> `"IPv4Address": "172.17.0.2/16"` -> IP address is `172.17.0.2`  

3. ### Run the service

In the next command instead of placeholder `IPv4Address` paste found IP address
```
docker run --name test-data-storage -e SPRING_DATASOURCE_PASSWORD=docker -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_URL=jdbc:postgresql://{IPv4Address}:5432/postgres -p 8080:8080 -d test-data-storage-service
```
Now service is running on `localhost`
