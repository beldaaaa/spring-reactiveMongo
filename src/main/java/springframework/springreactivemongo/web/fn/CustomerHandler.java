package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;
import springframework.springreactivemongo.service.CustomerService;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;

    Mono<ServerResponse> createCustomer(ServerRequest request) {
        return customerService.createCustomer(request.bodyToMono(CustomerDTO.class))
                .flatMap(customerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
                                .build(customerDTO.getId()))
                        .build());
    }

    Mono<ServerResponse> customerList(ServerRequest serverRequest) {

        return ServerResponse
                .ok()
                .body(customerService.customerList(), CustomerDTO.class);
    }

    Mono<ServerResponse> findById(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(customerService.findByCustomerId(serverRequest.pathVariable("customerId")), CustomerDTO.class);
    }
}
