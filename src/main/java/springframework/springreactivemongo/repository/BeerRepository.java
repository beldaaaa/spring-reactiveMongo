package springframework.springreactivemongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import springframework.springreactivemongo.domain.Beer;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

}
