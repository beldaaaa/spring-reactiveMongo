package springframework.springreactivemongo.service;

import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;

public interface CustomerService {
    Mono<CustomerDTO> saveCustomer(CustomerDTO beerDTO);

    Mono<CustomerDTO> findCustomerById(String customerId);
}
