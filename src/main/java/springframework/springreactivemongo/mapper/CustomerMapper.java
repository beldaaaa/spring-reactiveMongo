package springframework.springreactivemongo.mapper;

import org.mapstruct.Mapper;
import springframework.springreactivemongo.domain.Customer;
import springframework.springreactivemongo.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    CustomerDTO CustomerToCustomerDto(Customer customer);

    Customer CustomerDtoToCustomer(CustomerDTO customerDTO);
}
