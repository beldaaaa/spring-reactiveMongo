package springframework.springreactivemongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.domain.Beer;
import springframework.springreactivemongo.mapper.BeerMapper;
import springframework.springreactivemongo.model.BeerDTO;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;
    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(helperBeer());
    }

    public static Beer helperBeer() {
        return Beer.builder()
                .beerName("RandomName")
                .beerStyle("RandomStyle")
                .upc("661")
                .price(BigDecimal.ONE)
                .quantityOnHand(1)
                .build();
    }

    @Test
    void saveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getBeerName());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

}