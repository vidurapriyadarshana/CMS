package edu.epic.cms.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO {

    private String cardNumber;
    private String encryptedCardNumber;
    private String expireDate;
    private String cardStatus;
    private Integer creditLimit;
    private Integer cashLimit;
    private Integer availableCreditLimit;
    private Integer availableCashLimit;
    private LocalDateTime lastUpdateTime;
    private String lastUpdatedUser;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
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
