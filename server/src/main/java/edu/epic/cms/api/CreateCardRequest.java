package edu.epic.cms.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequest {

    // Removed @Size(16) validation because the incoming string is an RSA-encrypted Base64 payload, not 16 digits.
    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Expire date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Expire date must be in MM/YY format")
    private String expireDate;

    @Size(max = 20, message = "Card status must not exceed 20 characters")
    private String cardStatus;

    @Min(value = 0, message = "Credit limit must be a positive value")
    private Integer creditLimit;

    @Min(value = 0, message = "Cash limit must be a positive value")
    private Integer cashLimit;

    @Min(value = 0, message = "Available credit limit must be a positive value")
    private Integer availableCreditLimit;

    @Min(value = 0, message = "Available cash limit must be a positive value")
    private Integer availableCashLimit;
}
