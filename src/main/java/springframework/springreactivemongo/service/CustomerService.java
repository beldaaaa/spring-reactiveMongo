package springframework.springreactivemongo.service;

import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;

public interface CustomerService {

    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);

    Mono<CustomerDTO> findCustomerById(String customerId);
}
