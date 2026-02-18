package edu.epic.cms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardRequest {
    private int requestId;
    private String requestReasonCode;
    private String remark;
    private String cardNumber;
    private String status;
    private LocalDateTime createdTime;
}
