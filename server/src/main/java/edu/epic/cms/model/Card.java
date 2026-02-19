package edu.epic.cms.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card {

    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only digits")
    private String cardNumber;

    @NotBlank(message = "Expire date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Expire date must be in MM/YY format")
    private String expireDate;

    @Size(max = 20, message = "Card status must not exceed 20 characters")
    private String cardStatus;

    @Min(value = 0, message = "Credit limit must be a positive value")
    private int creditLimit;

    @Min(value = 0, message = "Cash limit must be a positive value")
    private int cashLimit;

    @Min(value = 0, message = "Available credit limit must be a positive value")
    private int availableCreditLimit;

    @Min(value = 0, message = "Available cash limit must be a positive value")
    private int availableCashLimit;

    private LocalDateTime lastUpdateTime;

}
