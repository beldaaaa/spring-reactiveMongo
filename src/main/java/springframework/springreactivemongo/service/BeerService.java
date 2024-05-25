package springframework.springreactivemongo.service;

import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.BeerDTO;

public interface BeerService {

    Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO);

    Mono<BeerDTO> findBeerById(String beerId);
}
