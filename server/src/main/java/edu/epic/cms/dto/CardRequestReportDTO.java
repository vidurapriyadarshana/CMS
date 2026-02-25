package edu.epic.cms.dto;

import java.time.LocalDateTime;

public class CardRequestReportDTO {
    private Integer requestId;
    private String requestReasonCode;
    private String requestReasonDescription;
    private String remark;
    private String cardNumber;
    private LocalDateTime createdTime;
    private String requestedUser;
    private String requestedUserName;
    private String approvedUser;
    private String approvedUserName;
    private String requestStatus;

    public CardRequestReportDTO() {}

    public CardRequestReportDTO(Integer requestId, String requestReasonCode, String requestReasonDescription, String remark, String cardNumber, LocalDateTime createdTime, String requestedUser, String requestedUserName, String approvedUser, String approvedUserName, String requestStatus) {
        this.requestId = requestId;
        this.requestReasonCode = requestReasonCode;
        this.requestReasonDescription = requestReasonDescription;
        this.remark = remark;
        this.cardNumber = cardNumber;
        this.createdTime = createdTime;
        this.requestedUser = requestedUser;
        this.requestedUserName = requestedUserName;
        this.approvedUser = approvedUser;
        this.approvedUserName = approvedUserName;
        this.requestStatus = requestStatus;
    }

    public Integer getRequestId() { return requestId; }
    public void setRequestId(Integer requestId) { this.requestId = requestId; }
    public String getRequestReasonCode() { return requestReasonCode; }
    public void setRequestReasonCode(String requestReasonCode) { this.requestReasonCode = requestReasonCode; }
    public String getRequestReasonDescription() { return requestReasonDescription; }
    public void setRequestReasonDescription(String requestReasonDescription) { this.requestReasonDescription = requestReasonDescription; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public String getRequestedUser() { return requestedUser; }
    public void setRequestedUser(String requestedUser) { this.requestedUser = requestedUser; }
    public String getRequestedUserName() { return requestedUserName; }
    public void setRequestedUserName(String requestedUserName) { this.requestedUserName = requestedUserName; }
    public String getApprovedUser() { return approvedUser; }
    public void setApprovedUser(String approvedUser) { this.approvedUser = approvedUser; }
    public String getApprovedUserName() { return approvedUserName; }
    public void setApprovedUserName(String approvedUserName) { this.approvedUserName = approvedUserName; }
    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
}
