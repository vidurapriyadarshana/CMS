package edu.epic.cms.api;

import java.time.LocalDateTime;

public class CardReportDTO {
    private String cardNumber;
    private String expireDate;
    private String cardStatus;
    private String cardStatusDescription;
    private Integer creditLimit;
    private Integer cashLimit;
    private Integer availableCreditLimit;
    private Integer availableCashLimit;
    private LocalDateTime lastUpdateTime;
    private String lastUpdatedUser;
    private String lastUpdatedUserName;

    public CardReportDTO() {}

    public CardReportDTO(String cardNumber, String expireDate, String cardStatus, String cardStatusDescription, Integer creditLimit, Integer cashLimit, Integer availableCreditLimit, Integer availableCashLimit, LocalDateTime lastUpdateTime, String lastUpdatedUser, String lastUpdatedUserName) {
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cardStatus = cardStatus;
        this.cardStatusDescription = cardStatusDescription;
        this.creditLimit = creditLimit;
        this.cashLimit = cashLimit;
        this.availableCreditLimit = availableCreditLimit;
        this.availableCashLimit = availableCashLimit;
        this.lastUpdateTime = lastUpdateTime;
        this.lastUpdatedUser = lastUpdatedUser;
        this.lastUpdatedUserName = lastUpdatedUserName;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getExpireDate() { return expireDate; }
    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }
    public String getCardStatusDescription() { return cardStatusDescription; }
    public void setCardStatusDescription(String cardStatusDescription) { this.cardStatusDescription = cardStatusDescription; }
    public Integer getCreditLimit() { return creditLimit; }
    public void setCreditLimit(Integer creditLimit) { this.creditLimit = creditLimit; }
    public Integer getCashLimit() { return cashLimit; }
    public void setCashLimit(Integer cashLimit) { this.cashLimit = cashLimit; }
    public Integer getAvailableCreditLimit() { return availableCreditLimit; }
    public void setAvailableCreditLimit(Integer availableCreditLimit) { this.availableCreditLimit = availableCreditLimit; }
    public Integer getAvailableCashLimit() { return availableCashLimit; }
    public void setAvailableCashLimit(Integer availableCashLimit) { this.availableCashLimit = availableCashLimit; }
    public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
    public String getLastUpdatedUser() { return lastUpdatedUser; }
    public void setLastUpdatedUser(String lastUpdatedUser) { this.lastUpdatedUser = lastUpdatedUser; }
    public String getLastUpdatedUserName() { return lastUpdatedUserName; }
    public void setLastUpdatedUserName(String lastUpdatedUserName) { this.lastUpdatedUserName = lastUpdatedUserName; }
}
