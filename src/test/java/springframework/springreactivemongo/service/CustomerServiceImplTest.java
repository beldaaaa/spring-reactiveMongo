package springframework.springreactivemongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import springframework.springreactivemongo.domain.Customer;
import springframework.springreactivemongo.mapper.CustomerMapper;
import springframework.springreactivemongo.mapper.CustomerMapperImpl;
import springframework.springreactivemongo.model.CustomerDTO;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class CustomerServiceImplTest {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerMapper customerMapper;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(helperCustomer());
    }

    public static Customer helperCustomer() {
        return Customer.builder()
                .id("99999999")
                .customerName("Jolanda")
                .build();
    }

    public static CustomerDTO CustomerDTO() {
        return new CustomerMapperImpl().customerToCustomerDto(helperCustomer());
    }

    @Test
    void saveBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isEqualTo(customerDTO.getId());
    }

    @Test
    void updateBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));
        final String updatedBeerName = "JustAnotherName";

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isEqualTo(customerDTO.getId());

        persistedDTO.setCustomerName("JustAnotherName");
        AtomicReference<CustomerDTO> updatedAtomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> updatedMono = customerService.updateCustomer(persistedDTO.getId(), persistedDTO);

        updatedMono.subscribe(updatedAtomicDTO::set);
        await().until(() -> updatedAtomicDTO.get() != null);

        CustomerDTO updatedDTO = updatedAtomicDTO.get();
        assertThat(updatedDTO).isNotNull();
        assertThat(updatedDTO.getCustomerName()).isEqualTo(updatedBeerName);
    }

    @Test
    void patchBeer() {
        final String patchedBeerName = "JustAnotherRandomNameAfterPatch";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isEqualTo(customerDTO.getId());

        persistedDTO.setCustomerName("JustAnotherRandomNameAfterPatch");
        AtomicReference<CustomerDTO> patchedAtomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> patchedMono = customerService.patchCustomer(persistedDTO.getId(), persistedDTO);

        patchedMono.subscribe(patchedAtomicDTO::set);
        await().until(() -> patchedAtomicDTO.get() != null);

        CustomerDTO patchedDTO = patchedAtomicDTO.get();
        assertThat(patchedDTO).isNotNull();
        assertThat(patchedDTO.getCustomerName()).isEqualTo(patchedBeerName);
    }

    @Test
    void deleteBeer() {
        CustomerDTO beerToDelete = CustomerDTO();
        customerService.deleteCustomer(beerToDelete.getId()).subscribe();

        Mono<CustomerDTO> expectedEmptyBeerMono = customerService.findCustomerById(beerToDelete.getId());

        StepVerifier.create(expectedEmptyBeerMono)
                .expectNextCount(0)
                .verifyComplete();
    }


}