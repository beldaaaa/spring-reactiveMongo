package springframework.springreactivemongo.mapper;

import org.mapstruct.Mapper;
import springframework.springreactivemongo.domain.Customer;
import springframework.springreactivemongo.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
