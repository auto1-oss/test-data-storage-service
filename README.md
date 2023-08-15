# <img src="logo.png" alt="Project Icon" width="30" height="30"> Test Data Storage Service

Store and manage test data efficiently.


## Overview

The Test Data Service is a tool designed to simplify the storage, management, and manipulation of test data.


### Prerequisites

- Java Development Kit (JDK) version 8 or later. (For running service locally)
- Docker (optional, for containerized deployment).

### Installation (API Only)

To run the Test Data Service in a Docker container, follow these steps:

1. **Make sure you have Docker installed on your system.**

2. **Use the following Docker run command to launch the service container:**

    ```sh
    docker run -d --name test-data-service-api \
        -p 8085:8085 \
        -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/test_data_storage \
        -e SPRING_DATASOURCE_USERNAME=postgres_username \
        -e SPRING_DATASOURCE_PASSWORD=postgres_password \
        -e BASIC_USERNAME=custom_user \
        -e BASIC_PASSWORD=custom_password \
        auto1/test-data-storage-service:latest
    ```

   Customize the command by adjusting the parameters according to your needs:

  - **Port:** Map the desired port on your host machine to the container's port 8085.
  - **SPRING_DATASOURCE_URL:** Set the database connection URL. This service was tested with PostgreSQL.
  - **SPRING_DATASOURCE_USERNAME:** Provide the database username.
  - **SPRING_DATASOURCE_PASSWORD:** Provide the database password.
  - **BASIC_USERNAME:** Customize the basic authentication username. Default is `user1`.
  - **BASIC_PASSWORD:** Customize the basic authentication password. Default is `password`.

3. **Once the container is running, the service will be accessible at `http://localhost:8085`.**

Remember to ensure that your database is properly configured and accessible from the Docker container.

### Installation with Docker Compose

## Installation with Docker Compose

To quickly set up and deploy the Test Data Storage service along with DB and its UI interface, you can use Docker Compose. Follow these steps:

1. **Install Docker and Docker Compose** if you haven't already.

2. **Create a `docker-compose.yml` file**.

4. **Use a template from**  [docker-compose.yml](docker-compose.yml). 

3. **Navigate to the directory** containing the `docker-compose.yml` file in your terminal.

4. Run the following command to start the containers:

```bash
docker-compose up -d
````

This will launch the Test Data Storage service, its database, and the UI interface in separate containers.

Access the UI interface by opening your web browser and navigating to [http://localhost:3000](http://localhost:3000).

### Configuration Parameters

Within the `docker-compose.yml` file, you might need to configure the following parameters based on your preferences:

- **Database Settings (for `db` service):**
    - `POSTGRES_USER`: Username for the PostgreSQL database.
    - `POSTGRES_PASSWORD`: Password for the PostgreSQL user.
    - `POSTGRES_DB`: Name of the PostgreSQL database.

- **Test Data Service Settings (for `test-data-service` service):**
    - `SPRING_DATASOURCE_URL`: Database connection URL.
    - `SPRING_DATASOURCE_USERNAME`: Database username.
    - `SPRING_DATASOURCE_PASSWORD`: Database password.
    - `BASIC_USERNAME`: Username for basic authentication.
    - `BASIC_PASSWORD`: Password for basic authentication.

- **UI Settings (for `ui` service):**
    - `REACT_APP_BASE_URL_API`: Base URL for the Test Data Service API. Make sure you are using publicly accessible API url.
    - `REACT_APP_BASIC_AUTH_USER`: Basic authentication username.
    - `REACT_APP_BASIC_AUTH_PASSWORD`: Basic authentication password.

Adjust these parameters as needed to match your environment and preferences.

You've successfully set up the Test Data Storage service with Docker Compose. You can now manage data types through the UI interface.


## Usage

### Creating Test Data Queues

```http
POST /v1/queue/omni-type
Content-Type: application/json
Accept: application/json

{
  "dataType": "NEW_TYPE"
}
```

### Filling Test Data Queues

```http
POST /v1/queue/omni/{dataType}
Content-Type: application/json
Accept: application/json

{
 "prop1": "value1",
 "prop2": "value2",
 "prop3": "value3"
}
```

**Description:**

- `dataType`: Replace this with the previously created data type name.
- `Body`: The body of the request can be either a JSON object containing the data properties or plain text. While it's recommended to use JSON format for better data organization, the body can also be provided as plain text.

## UI Interface (Optional)

Optionally, you can install a UI interface to conveniently manage data types. The instructions for setting up the UI interface are provided in the [UI Installation Guide](https://github.com/auto1-oss/test-data-storage-ui).

The UI interface allows you to interact with the API operations for creating, updating, deleting, and retrieving data types in a user-friendly manner. It can enhance the overall experience of managing your data types.


## Managing Test Data Types

### Create Test Data Type

Create a new Test Data Type by sending a `POST` request to `/queue/omni-type`. Provide the `OmniTypeDTO` object in the request body in JSON format. This endpoint does not return a response.

### Update Test Data Type

Update an existing Test Data Type by sending a `PUT` request to `/queue/omni-type/{id}` where `{id}` is the ID of the Test Data Type to be updated. Provide the updated `OmniTypeDTO` object in the request body in JSON format. The endpoint returns the updated `OmniTypeDTO` object.

### Delete Test Data Type

Delete a Test Data Type by sending a `DELETE` request to `/queue/omni-type/{id}` where `{id}` is the ID of the Test Data Type to be deleted. This endpoint does not return a response.

### Get All Test Data Types

Retrieve a list of all Test Data Types by sending a `GET` request to `/queue/omni-types`. The response will be a list of `OmniTypeDTO` objects.


## Managing Data Queue Items

### Create Data Queue Item

Create a new data queue item by sending a `POST` request to `/queue/omni/{data-type}` where `{data-type}` is the type of data associated with the data queue item. Provide the data as a string in the request body. This endpoint does not return a response.

### Get Data Queue Item

Retrieve a data queue item from a specific queue by sending a `GET` request to `/queue/omni/{data-type}` where `{data-type}` is the type of data associated with the data queue item. The response will be a string containing the data queue item data.

### Purge All Data by Data Type

Purge all data queue items of a specific data type by sending a `POST` request to `/queue/omni/{data-type}/purge` where `{data-type}` is the type of data associated with the data queue items. This endpoint does not return a response.

### Count Data Queue Items by Data Type

Count the number of data queue items of a specific data type by sending a `GET` request to `/queue/omni/{data-type}/count` where `{data-type}` is the type of data associated with the data queue items. The response will be a `DataQueueItemCountDTO` object.

### Count All Data Queue Items

Retrieve the count of all data queue items by sending a `GET` request to `/queue/omni/count`. The response will be a list of `DataQueueItemCountDTO` objects.

### Delete Data Queue Item by ID

Delete a data queue item by sending a `DELETE` request to `/queue/omni/{id}` where `{id}` is the ID of the data queue item to be deleted. This endpoint does not return a response.

### Search Data Queue Items

Search for data queue items by sending a `POST` request to `/queue/omni/search`. Provide the search criteria in the request body as a `DataQueueSearchDTO` object. The response will be a list of `DataQueueItemDTO` objects.

### Archive Data Queue

Archive the data queue by sending a `POST` request to `/queue/omni/clean`. Provide the required data in the request body as an `ArchiveDataQueueDTO` object. This endpoint does not return a response.



## License

This project is licensed under the [Apache-2.0 license](LICENSE).

## Acknowledgements

We would like to thank the open-source community for their valuable contributions and support.

