package com.martinPluhar.Bankapplication.dto;

import lombok.*;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDebitRequest {

    private String accountNumber;
    private BigDecimal amount;

    @Getter
    private String senderAccount;

    @Getter
    private String receiverAccount;


    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }


    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }
}
