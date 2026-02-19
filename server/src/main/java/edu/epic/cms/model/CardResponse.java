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
public class CardResponse {

    private String cardNumber;
    private String encryptedCardNumber;
    private String expireDate;
    private String cardStatus;
    private Integer creditLimit;
    private Integer cashLimit;
    private Integer availableCreditLimit;
    private Integer availableCashLimit;
    private LocalDateTime lastUpdateTime;

}
