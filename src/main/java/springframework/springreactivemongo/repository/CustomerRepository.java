package springframework.springreactivemongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import springframework.springreactivemongo.domain.Customer;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
