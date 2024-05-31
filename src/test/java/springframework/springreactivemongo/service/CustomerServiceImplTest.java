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
public class CustomerServiceImplTest {
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
                .customerName("Jolanda")
                .build();
    }

    public static CustomerDTO helperCustomerDTO() {
        return new CustomerMapperImpl().customerToCustomerDto(helperCustomer());
    }

    @Test
    void createCustomer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.createCustomer(Mono.just(customerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO).isNotNull();
        assertThat(persistedDTO.getId()).isNotNull();
    }

    @Test
    void updateCustomer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.createCustomer(Mono.just(customerDTO));
        final String updatedCustomerName = "JustAnotherName";

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO.getId()).isNotNull();

        persistedDTO.setCustomerName("JustAnotherName");
        AtomicReference<CustomerDTO> updatedAtomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> updatedMono = customerService.updateCustomer(persistedDTO.getId(), persistedDTO);

        updatedMono.subscribe(updatedAtomicDTO::set);
        await().until(() -> updatedAtomicDTO.get() != null);

        CustomerDTO updatedDTO = updatedAtomicDTO.get();
        assertThat(updatedDTO).isNotNull();
        assertThat(updatedDTO.getCustomerName()).isEqualTo(updatedCustomerName);
    }

    @Test
    void patchCustomer() {
        final String patchedCustomerName = "JustAnotherRandomNameAfterPatch";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> savedMono = customerService.createCustomer(Mono.just(customerDTO));

        savedMono.subscribe(savedDTO -> {
            atomicBoolean.set(true);
            atomicDTO.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDTO = atomicDTO.get();
        assertThat(persistedDTO.getId()).isNotNull();

        persistedDTO.setCustomerName("JustAnotherRandomNameAfterPatch");
        AtomicReference<CustomerDTO> patchedAtomicDTO = new AtomicReference<>();
        Mono<CustomerDTO> patchedMono = customerService.patchCustomer(persistedDTO.getId(), persistedDTO);

        patchedMono.subscribe(patchedAtomicDTO::set);
        await().until(() -> patchedAtomicDTO.get() != null);

        CustomerDTO patchedDTO = patchedAtomicDTO.get();
        assertThat(patchedDTO.getCustomerName()).isEqualTo(patchedCustomerName);
    }

    @Test
    void deleteCustomer() {
        CustomerDTO customerToDelete = helperCustomerDTO();
        customerToDelete.setId("465126865421");
        customerService.deleteCustomer(customerToDelete.getId()).subscribe();
        Mono<CustomerDTO> expectedEmptyCustomerMono = customerService.findByCustomerId(customerToDelete.getId());

        StepVerifier.create(expectedEmptyCustomerMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findFirstByCustomerName() {
        CustomerDTO customerToFind = helperCustomerDTO();
        //  AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<CustomerDTO> foundCustomer = customerService.findFirstByCustomerName(customerToFind.getCustomerName());

        foundCustomer.subscribe();//(dto -> {
//            atomicBoolean.set(true);
//            System.out.println(dto.toString());
//        });
//        await().untilTrue(atomicBoolean);
    }
}