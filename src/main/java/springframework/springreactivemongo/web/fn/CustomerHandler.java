package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;
import springframework.springreactivemongo.service.CustomerService;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;

    Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        return customerService.createCustomer(serverRequest.bodyToMono(CustomerDTO.class))
                .flatMap(customerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
                                .build(customerDTO.getId()))
                        .build());
    }

    Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerDTO.class)
                .flatMap(customerDTO -> customerService
                        .updateCustomer(serverRequest.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse
                        .noContent()
                        .build());
    }

    Mono<ServerResponse> patchCustomer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CustomerDTO.class)
                .flatMap(customerDTO -> customerService
                        .patchCustomer(serverRequest.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDTO -> ServerResponse
                        .noContent()
                        .build());
    }

    Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        return customerService.findByCustomerId(serverRequest.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> customerService.deleteCustomer(customerDTO.getId()))
                .then(ServerResponse
                        .noContent()
                        .build());
    }

    Mono<ServerResponse> customerList(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(customerService.customerList(), CustomerDTO.class);
    }

    Mono<ServerResponse> findByCustomerId(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .body(customerService.findByCustomerId(serverRequest.pathVariable("customerId"))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), CustomerDTO.class);
    }
}
