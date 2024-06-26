package springframework.springreactivemongo.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;

public interface CustomerService {

    Mono<CustomerDTO> createCustomer(Mono<CustomerDTO> customerDTO);

    Mono<CustomerDTO> createCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateCustomer(String customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(String customerId, CustomerDTO customerDTO);

    Mono<Void> deleteCustomer(String customerId);

    Mono<CustomerDTO> findByCustomerId(String customerId);

    Flux<CustomerDTO> customerList();

    Mono<CustomerDTO> findFirstByCustomerName(String customerName);

}
