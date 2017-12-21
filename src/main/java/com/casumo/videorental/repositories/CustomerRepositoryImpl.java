package com.casumo.videorental.repositories;

import com.casumo.videorental.model.Customer;
import com.casumo.videorental.model.RentedMovie;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static com.mongodb.client.model.Filters.in;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private final ReactiveMongoOperations mongoOperations;

    @Override
    public Flux<RentedMovie> findCustomersRentedMovies(String customerId, Collection<String> movies) {

        TypedAggregation<Customer> customerTypedAggregation = newAggregation(Customer.class, match(Criteria.where("name").is(customerId)),
                unwind("rentedMovies"),
                match(Criteria.where("rentedMovies.title").in(movies)),
                project("rentedMovies.title", "rentedMovies.rentedFrom", "rentedMovies.rentedTo"));
        return mongoOperations.aggregate(customerTypedAggregation, RentedMovie.class).doOnError(e -> e.printStackTrace());
    }

    @Override
    public Mono<UpdateResult> pushRentedMovies(String customerId, Collection<RentedMovie> movies) {
        Query byCustomerName = new Query(Criteria.where("name").is(customerId));
        return mongoOperations.updateFirst(byCustomerName, new Update().pushAll("rentedMovies", movies.toArray()), Customer.class);
    }

    @Override
    public Mono<UpdateResult> pullRentedMovies(String customerId, Collection<String> movies) {
        Query byCustomerName = new Query(Criteria.where("name").is(customerId));
        return mongoOperations.updateFirst(byCustomerName, new Update().pull("rentedMovies", new Query(Criteria.where("title").in(movies))), Customer.class);
    }


}
