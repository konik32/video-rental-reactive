package com.casumo.videorental.controllers;

public interface Paths {
    String API = "/api";
    String MOVIES = API + "/movies";
    String MOVIE = MOVIES + "/{id}";
    String CUSTOMERS = API + "/customers";
    String CUSTOMER = CUSTOMERS + "/{id}";
    String CUSTOMER_MOVIES = CUSTOMER + "/movies";
    String CUSTOMER_MOVIES_RETURN = CUSTOMER_MOVIES + "/return";
    String FILES = API + "/files";
}
