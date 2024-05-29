package springframework.springreactivemongo.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.BeerDTO;

public interface BeerService {

    Mono<BeerDTO> createBeer(Mono<BeerDTO> beerDTO);

    Mono<BeerDTO> createBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(String beerId, BeerDTO beerDTO);

    Mono<Void> deleteBeer(String beerId);

    Mono<BeerDTO> findByBeerId(String beerId);

    Flux<BeerDTO> beerList();

    Mono<BeerDTO> findFirstByBeerName(String beerName);

    Flux<BeerDTO> findByBeerStyle(String beerStyle);
}
