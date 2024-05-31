package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.BeerDTO;
import springframework.springreactivemongo.service.BeerService;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    Mono<ServerResponse> createBeer(ServerRequest serverRequest) {
        return beerService.createBeer(serverRequest.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse//returning a new publisher
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    Mono<ServerResponse> updateBeer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .updateBeer(serverRequest.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse
                        .noContent()
                        .build());
    }

    Mono<ServerResponse> patchBeer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .patchBeer(serverRequest.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse
                        .noContent()
                        .build());
    }

    Mono<ServerResponse> deleteBeer(ServerRequest serverRequest) {
        return beerService.findByBeerId(serverRequest.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeer(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
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

    Mono<ServerResponse> findByBeerId(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(beerService.findByBeerId(serverRequest.pathVariable("beerId"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), BeerDTO.class);
    }

}
