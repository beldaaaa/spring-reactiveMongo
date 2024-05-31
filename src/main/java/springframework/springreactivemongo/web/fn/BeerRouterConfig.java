package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BeerRouterConfig {

    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = "/{beerId}";
    public static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);
    private final BeerHandler beerHandler;

    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, ACCEPT_JSON, beerHandler::beerList)
                .GET(BEER_PATH_ID, ACCEPT_JSON, beerHandler::findByBeerId)
                .POST(BEER_PATH, ACCEPT_JSON, beerHandler::createBeer)
                .PUT(BEER_PATH_ID, ACCEPT_JSON, beerHandler::updateBeer)
                .PATCH(BEER_PATH_ID, ACCEPT_JSON, beerHandler::patchBeer)
                .DELETE(BEER_PATH_ID, ACCEPT_JSON, beerHandler::deleteBeer)
                .build();
    }
}
