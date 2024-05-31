package springframework.springreactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class CustomerRouterConfig {

    public static final String CUSTOMER_PATH = "/api/v3/beer";
    public static final String CUSTOMER_PATH_ID = "/{customerId}";
    public static final RequestPredicate ACCEPT_JSON = accept(MediaType.APPLICATION_JSON);
    private final CustomerHandler customerHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return route()
                .GET(CUSTOMER_PATH, ACCEPT_JSON, customerHandler::customerList)
                .GET(CUSTOMER_PATH_ID, ACCEPT_JSON, customerHandler::findByCustomerId)
                .POST(CUSTOMER_PATH, ACCEPT_JSON, customerHandler::createCustomer)
                .PUT(CUSTOMER_PATH_ID, ACCEPT_JSON, customerHandler::updateCustomer)
                .PATCH(CUSTOMER_PATH_ID, ACCEPT_JSON, customerHandler::patchCustomer)
                .DELETE(CUSTOMER_PATH_ID, ACCEPT_JSON, customerHandler::deleteCustomer)
                .build();
    }
}
