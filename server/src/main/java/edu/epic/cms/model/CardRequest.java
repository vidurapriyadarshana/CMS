package edu.epic.cms.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class CardRequest {

    private Integer requestId;

    @NotBlank(message = "Request reason code is required")
    @Size(max = 4, message = "Request reason code must not exceed 4 characters")
    private String requestReasonCode;

    @Size(max = 255, message = "Remark must not exceed 255 characters")
    private String remark;

    private String encryptedCardNumber;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    private LocalDateTime createdTime;

    @Size(max = 100, message = "Approved user must not exceed 100 characters")
    private String approvedUser;

    @Size(max = 100, message = "Requested user must not exceed 100 characters")
    private String requestedUser;

    private String requestStatus;

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getRequestReasonCode() {
        return requestReasonCode;
    }

    public void setRequestReasonCode(String requestReasonCode) {
        this.requestReasonCode = requestReasonCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public String getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(String requestedUser) {
        this.requestedUser = requestedUser;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
