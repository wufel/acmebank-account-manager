package com.wufel.acmebank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="ACCOUNTS")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    //set accountId as string to provide flexibility if accountId number became unnecessarily big or long or decided to switch format in the future
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "balance")
    private BigDecimal balance;

}
