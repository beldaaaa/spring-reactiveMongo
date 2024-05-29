package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.model.CustomerDTO;
import springframework.springreactivemongo.service.CustomerService;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;

    Mono<ServerResponse> customerList(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .body(customerService.customerList(), CustomerDTO.class);
    }
}
