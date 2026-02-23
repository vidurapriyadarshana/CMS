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

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    private String encryptedCardNumber;

    @NotBlank(message = "Status is required")
    @Size(max = 4, message = "Status must not exceed 4 characters")
    private String status;

    private LocalDateTime createdTime;

    private String completionStatus;

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

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
}
