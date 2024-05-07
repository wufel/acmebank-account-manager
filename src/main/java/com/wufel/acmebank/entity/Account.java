package com.wufel.acmebank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
public class Account {

    //set accountId as string to provide flexibility if accountId number became unnecessarily big or long or decided to switch format in the future
    @Id
    private String accountId;

    private BigDecimal balance;

}
