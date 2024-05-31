package springframework.springreactivemongo.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.domain.Customer;
import springframework.springreactivemongo.model.CustomerDTO;
import springframework.springreactivemongo.service.CustomerServiceImplTest;

import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureWebTestClient
public class CustomerEndpointTest {
    private static final int TEST_ID = 132657456;

    @Autowired
    private WebTestClient webTestClient;

    public CustomerDTO helperCustomer() {
        FluxExchangeResult<CustomerDTO> customerDTOFluxExchangeResult = webTestClient
                .post()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(CustomerServiceImplTest.helperCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDTO.class);

        List<String> location = customerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient
                .get()
                .uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .returnResult(CustomerDTO.class)
                .getResponseBody()
                .blockFirst();
    }

    @Test
    void createCustomer() {
        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(CustomerServiceImplTest.helperCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void createCustomerWithBadData() {
        Customer testCustomer = CustomerServiceImplTest.helperCustomer();
        testCustomer.setCustomerName("");

        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void updateCustomerWithBadRequest() {
        CustomerDTO testCustomer = helperCustomer();
        testCustomer.setCustomerName("");

        webTestClient.put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, testCustomer)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomerNotFound() {
        webTestClient.put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void updateCustomerFound() {

        CustomerDTO customerDTO = helperCustomer();

        webTestClient.put()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void patchCustomerNotFound() {
        webTestClient.patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchCustomerFound() {
        CustomerDTO customerDTO = helperCustomer();

        webTestClient.patch()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteCustomerNotFound() {
        webTestClient.delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(100)
    void deleteCustomerFound() {
        CustomerDTO customerDTO = helperCustomer();

        webTestClient.delete()
                .uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void findByIdNotFound() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, TEST_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void findByIdFound() {
        CustomerDTO customerDTO = helperCustomer();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH_ID, customerDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(2)
    void customerList() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThanOrEqualTo(1)));
    }

}

