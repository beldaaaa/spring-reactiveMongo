package springframework.springreactivemongo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String id;

    @NotBlank
    @NotNull
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

