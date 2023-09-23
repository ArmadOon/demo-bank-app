package com.martinPluhar.Bankapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    @Schema(
            name = "Client Account Name"
    )
    private String accountName;
    @Schema(
            name = "Client Account Balance"
    )
    private BigDecimal accountBalance;
    @Schema(
            name = "Clients Account Number"
    )
    private String accountNumber;
}
