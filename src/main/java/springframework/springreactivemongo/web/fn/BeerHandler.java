package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.BeerDTO;
import springframework.springreactivemongo.service.BeerService;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    Mono<ServerResponse> createBeer(ServerRequest request) {
        return beerService.createBeer(request.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse//returning a new publisher
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    Mono<ServerResponse> beerList(ServerRequest serverRequest) {
        Flux<BeerDTO> dtoFlux;

        if (serverRequest.queryParam("beerStyle").isPresent()) {
            dtoFlux = beerService.findByBeerStyle(serverRequest.queryParam("beerStyle").get());
        } else {
            dtoFlux = beerService.beerList();
        }
        return ServerResponse.ok()
                .body(dtoFlux, BeerDTO.class);
    }

    Mono<ServerResponse> findBeerById(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(beerService.findByBeerId(serverRequest.pathVariable("beerId")), BeerDTO.class);
    }

}
