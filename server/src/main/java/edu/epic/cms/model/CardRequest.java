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
public class CardRequest {

    private Integer requestId;

    @NotBlank(message = "Request reason code is required")
    @Size(max = 4, message = "Request reason code must not exceed 4 characters")
    private String requestReasonCode;

    @Size(max = 255, message = "Remark must not exceed 255 characters")
    private String remark;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Status is required")
    @Size(max = 4, message = "Status must not exceed 4 characters")
    private String status;

    private LocalDateTime createdTime;
}
