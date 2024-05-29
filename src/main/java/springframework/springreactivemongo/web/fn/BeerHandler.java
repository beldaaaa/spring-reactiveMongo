package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.BeerDTO;
import springframework.springreactivemongo.service.BeerService;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    Mono<ServerResponse> beerList(ServerRequest serverRequest) {
        Flux<BeerDTO> dtoFlux;

        if(serverRequest.queryParam("beerStyle").isPresent()){
            dtoFlux = beerService.findByBeerStyle(serverRequest.queryParam("beerStyle").get());
        }else{
            dtoFlux = beerService.beerList();
        }
        return ServerResponse.ok()
                .body(dtoFlux, BeerDTO.class);
    }

}
