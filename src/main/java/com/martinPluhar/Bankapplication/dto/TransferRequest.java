package com.martinPluhar.Bankapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    String sourceAccountNumber;
    String destinationAccountNumber;
    double amount;
}
