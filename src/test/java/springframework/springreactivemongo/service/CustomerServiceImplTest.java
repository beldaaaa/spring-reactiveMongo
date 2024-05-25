package springframework.springreactivemongo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.domain.Customer;
import springframework.springreactivemongo.mapper.CustomerMapper;
import springframework.springreactivemongo.model.CustomerDTO;

import java.util.concurrent.atomic.AtomicBoolean;

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
                .customerName("Jolanda")
                .build();
    }

    @Test
    void saveCustomer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<CustomerDTO> savedMono = customerService.saveCustomer(Mono.just(customerDTO));
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getCustomerName());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }


}