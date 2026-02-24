package edu.epic.cms.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequestDTO {

    // Removed @Size(16) validation because the incoming string is an RSA-encrypted Base64 payload, not 16 digits.
    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Expire date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Expire date must be in MM/YY format")
    private String expireDate;

    @Min(value = 0, message = "Credit limit must be a positive value")
    private Integer creditLimit;

    @Min(value = 0, message = "Cash limit must be a positive value")
    private Integer cashLimit;

    @Min(value = 0, message = "Available credit limit must be a positive value")
    private Integer availableCreditLimit;

    @Min(value = 0, message = "Available cash limit must be a positive value")
    private Integer availableCashLimit;

    private String lastUpdatedUser;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(Integer cashLimit) {
        this.cashLimit = cashLimit;
    }

    public Integer getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public void setAvailableCreditLimit(Integer availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    public Integer getAvailableCashLimit() {
        return availableCashLimit;
    }

    public void setAvailableCashLimit(Integer availableCashLimit) {
        this.availableCashLimit = availableCashLimit;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
}
