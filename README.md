# update 2018-10-04

Application now supports file upload to Google Cloud Storage. To reduce the load on application, client uploads files directly to GCS. File is validated with signed policy document. The file upload takes several steps:
1. Request for file upload signed policy document. Policy document contains conditions that the file upload should meet (size, content-type, bucket name etc.)
2. Upload file from client eg. browser directly to GCS temporary bucket (files are deleted after TTL)
3. Attach uploaded file information to the resource eg. movie
4. Copy file from temporary bucket to persistent bucket
5. Save file information as separate document in mongodb
6. Attach file reference in movie document (id and serving url)

GCS sdk is synchronous api. To use it in the reactive non-blocking application calls to the Storage were wrapped/isolated with Hystrix thread. Of course calls to Storage block Hystrix threads.

To run application without GCP account use dev profile otherwise configure following application properties:

Things to improve:
1. File content type validation before policy document is created
2. Hystrix command profiling eg. timeouts
3. Unit test coverage only integration tests were implemented

```
application:
  file-upload:
    temporaryBucket: "video-rental-temp"
    persistentBucket: "video-rental-persistent"
spring:
      cloud.gcp.credentials.location: classpath:mock-credentials.json
```


# video-rental-reactive
The application has RESTlike interface. Import `postman.json` to your postman. You will find there prepared requests for renting movies, returning movies, checking custmer details (bonus points, currently rented movies),creating movies with and without images, preparing file upload to GCS with signed policy document, uploading files to GCS. Database is prefilled with following content:

Movies
```
{ "title": "Pulp Fiction", "movieType": "REGULAR" }
{ "title": "Avatar", "movieType": "NEW" }
{ "title": "Star Wars", "movieType": "OLD" }
```
Customer
```
{ "name": "John" }
```
## Main technologies
Although some of the technologies I used are not released yet, I wanted to learn something new while implementing this application.
1. Spring Boot 2
2. Spring WebFlux
3. Spring Data Mongo
4. Embedded MongoDB
5. Spring Reactor
6. Google Cloud Storage
7. Spring Cloud GCP

## Running application
Clone the repository and run `mvnw spring-boot:run -Dspring.profiles.active=dev` Application listens on port `8080`. It might take a while to run it for the first time, because it download embedded mongodb.

## Endpoints

### Rent movies
`POST` http://localhost:8080/api/customers/John/movies
```
{
"rentedMovies": [
	{"title":"Avatar","rentedFrom": "2017-12-19","rentedTo": "2017-12-20"},
	{"title":"Pulp Fiction","rentedFrom": "2017-12-15","rentedTo": "2017-12-20"},
	{"title":"Spider Man","rentedFrom": "2017-12-18","rentedTo": "2017-12-20"},
	{"title":"Star Wars","rentedFrom": "2017-12-13","rentedTo": "2017-12-20"}
  ]

}

```
Response body:
```
{
    "movies": [
        {
            "movieTitle": "Avatar",
            "price": 40
        },
        {
            "movieTitle": "Spider Man",
            "price": 30
        },
        {
            "movieTitle": "Pulp Fiction",
            "price": 90
        },
        {
            "movieTitle": "Star Wars",
            "price": 90
        }
    ],
    "total": 250
}
```
### Return movies
`PUT` http://localhost:8080/api/customers/John/movies/return
```
[
	"Spider Man", "Pulp Fiction"
]
```
Response:
```
{
    "movies": [
        {
            "movieTitle": "Pulp Fiction",
            "price": 30
        },
        {
            "movieTitle": "Spider Man",
            "price": 30
        }
    ],
    "total": 60
}
```
### Get customer's details
`GET` http://localhost:8080/api/customers/John
```
{
    "name": "John",
    "bonusPoints": 2,
    "rentedMovies": [
        {
            "title": "Avatar",
            "rentedFrom": "2017-12-19",
            "rentedTo": "2017-12-20"
        },
        {
            "title": "Star Wars",
            "rentedFrom": "2017-12-13",
            "rentedTo": "2017-12-20"
        }
    ]
}
```

### Create customer 
`POST` http://localhost:8080/api/customers
```
{
	"name": "John"
}
```

### Create movie
`POST` http://localhost:8080/api/movies
```
{
	"title": "Avatar",
	"movieType": "NEW"
}
```

### Get movie
`GET` http://localhost:8080/api/movies/Avatar
```
{
	"title": "Avatar",
	"movieType": "NEW"
}
```
