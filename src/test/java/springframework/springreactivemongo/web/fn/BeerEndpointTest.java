package springframework.springreactivemongo.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.domain.Beer;
import springframework.springreactivemongo.model.BeerDTO;
import springframework.springreactivemongo.service.BeerServiceImplTest;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureWebTestClient
public class BeerEndpointTest {
    private static final int TEST_ID = 132657456;

    @Autowired
    private WebTestClient webTestClient;

    public BeerDTO helperBeer() {
        FluxExchangeResult<BeerDTO> beerDTOFluxExchangeResult = webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.helperBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        List<String> location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange().returnResult(BeerDTO.class).getResponseBody().blockFirst();
    }

    @Test
    void createBeer() {
        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.helperBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void createBeerWithBadData() {
        Beer testBeer = BeerServiceImplTest.helperBeer();
        testBeer.setBeerName("");

        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void updateBeerWithBadRequest() {
        BeerDTO testBeer = helperBeer();
        testBeer.setBeerStyle("");

        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, testBeer)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeerNotFound() {
        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, TEST_ID)
                .body(Mono.just(BeerServiceImplTest.helperBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void updateBeerFound() {

        BeerDTO beerDTO = helperBeer();

        webTestClient.put()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void patchBeerNotFound() {
        webTestClient.patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, TEST_ID)
                .body(Mono.just(BeerServiceImplTest.helperBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchBeerFound() {
        BeerDTO beerDTO = helperBeer();

        webTestClient.patch()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .body(Mono.just(beerDTO), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeerNotFound() {
        webTestClient.delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(100)
    void deleteBeerFound() {
        BeerDTO beerDTO = helperBeer();

        webTestClient.delete()
                .uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    void findByIdNotFound() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void findByIdFound() {
        BeerDTO beerDTO = helperBeer();

        webTestClient.get().uri(BeerRouterConfig.BEER_PATH_ID, beerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    @Order(2)
    void beerList() {
        webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

}
