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
    private int creditLimit;
    private int cashLimit;
    private int availableCreditLimit;
    private int availableCashLimit;
    private LocalDateTime lastUpdateTime;

}
