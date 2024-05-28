package springframework.springreactivemongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.domain.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    Mono<Customer> findFirstByCustomerName(String customerName);

}
