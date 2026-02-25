package edu.epic.cms.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
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

    private String cardStatusDescription;

    @Min(value = 0, message = "Credit limit must be a positive value")
    private Integer creditLimit;

    @Min(value = 0, message = "Cash limit must be a positive value")
    private Integer cashLimit;

    @Min(value = 0, message = "Available credit limit must be a positive value")
    private Integer availableCreditLimit;

    @Min(value = 0, message = "Available cash limit must be a positive value")
    private Integer availableCashLimit;

    private LocalDateTime lastUpdateTime;

    @Size(max = 100, message = "Last updated user must not exceed 100 characters")
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

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardStatusDescription() {
        return cardStatusDescription;
    }

    public void setCardStatusDescription(String cardStatusDescription) {
        this.cardStatusDescription = cardStatusDescription;
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

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
}
