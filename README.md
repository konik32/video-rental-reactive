# video-rental-reactive
The application has RESTlike interface. Import `postman.json` to your postman. You will find there three prepared requests for renting movies, returning movies and checking custmer details (bonus points, currently rented movies). Database is prefilled with following content:

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

## Running application
Clone the repository and run `mvnw spring-boot:run` Application listens on port `8080`. It might take a while to run it for the first time, because it download embedded mongodb.

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
