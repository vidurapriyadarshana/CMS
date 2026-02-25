package edu.epic.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestResponseDTO {
    private Integer requestId;
    private String requestReasonCode;
    private String remark;
    private String cardNumber;
    private String encryptedCardNumber;
    private LocalDateTime createdTime;
    private String approvedUser;
    private String requestedUser;
}
