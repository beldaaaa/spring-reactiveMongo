package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BeerRouterConfig {

    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = "/{beerId}";
    private final BeerHandler beerHandler;

    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, accept(MediaType.APPLICATION_JSON), beerHandler::beerList)
                .GET(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), beerHandler::findBeerById)
                .POST(BEER_PATH, accept(MediaType.APPLICATION_JSON), beerHandler::createBeer)
                .PUT(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), beerHandler::updateBeer)
                .PATCH(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), beerHandler::patchBeer)
                .build();
    }
}
