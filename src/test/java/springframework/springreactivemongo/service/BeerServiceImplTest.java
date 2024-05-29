package springframework.springreactivemongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import springframework.springreactivemongo.domain.Beer;
import springframework.springreactivemongo.mapper.BeerMapper;
import springframework.springreactivemongo.mapper.BeerMapperImpl;
import springframework.springreactivemongo.model.BeerDTO;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class BeerServiceImplTest {

    @Autowired
    BeerService beerService;
    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(helperBeer());
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

    public static BeerDTO helperBeerDTO() {
        return new BeerMapperImpl().beerToBeerDto(helperBeer());
    }

    @Test
    void saveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isNotNull();
    }

    @Test
    void updateBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));
        final String updatedBeerName = "JustAnotherName";

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO.getId()).isNotNull();

        persistedDTO.setBeerName("JustAnotherName");
        AtomicReference<BeerDTO> updatedAtomicDTO = new AtomicReference<>();
        Mono<BeerDTO> updatedMono = beerService.updateBeer(persistedDTO.getId(), persistedDTO);

        updatedMono.subscribe(updatedAtomicDTO::set);
        await().until(() -> updatedAtomicDTO.get() != null);

        BeerDTO updatedDTO = updatedAtomicDTO.get();
        assertThat(updatedDTO).isNotNull();
        assertThat(updatedDTO.getBeerName()).isEqualTo(updatedBeerName);
    }

    @Test
    void patchBeer() {
        final String patchedBeerName = "JustAnotherRandomNameAfterPatch";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDTO = new AtomicReference<>();
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO.getId()).isNotNull();

        persistedDTO.setBeerName("JustAnotherRandomNameAfterPatch");
        AtomicReference<BeerDTO> patchedAtomicDTO = new AtomicReference<>();
        Mono<BeerDTO> patchedMono = beerService.patchBeer(persistedDTO.getId(), persistedDTO);

        patchedMono.subscribe(patchedAtomicDTO::set);
        await().until(() -> patchedAtomicDTO.get() != null);

        BeerDTO patchedDTO = patchedAtomicDTO.get();
        assertThat(patchedDTO.getBeerName()).isEqualTo(patchedBeerName);
    }

    @Test
    void deleteBeer() {
        BeerDTO beerToDelete = helperBeerDTO();
        beerToDelete.setId("798798789");
        beerService.deleteBeer(beerToDelete.getId()).subscribe();
        Mono<BeerDTO> expectedEmptyBeerMono = beerService.findByBeerId(beerToDelete.getId());

        StepVerifier.create(expectedEmptyBeerMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findFirstByBeerName() {
        BeerDTO beerToFind = helperBeerDTO();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> foundBeer = beerService.findFirstByBeerName(beerToFind.getBeerName());

        foundBeer.subscribe(dto -> {
            atomicBoolean.set(true);
            System.out.println(dto.toString());
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void findByBeerStyle() {
        BeerDTO beerToFind = helperBeerDTO();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        beerService.findByBeerStyle(beerToFind.getBeerStyle())
                .subscribe(dto -> {
                    atomicBoolean.set(true);
                    System.out.println(dto.toString());
                });
        await().untilTrue(atomicBoolean);
    }

}

