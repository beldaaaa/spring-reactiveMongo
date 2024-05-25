package springframework.springreactivemongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.mapper.CustomerMapper;
import springframework.springreactivemongo.model.CustomerDTO;
import springframework.springreactivemongo.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO) {
        return customerDTO.map(customerMapper::customerDtoToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> findCustomerById(String customerId) {
        return null;
    }
}
