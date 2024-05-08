package com.wufel.acmebank.service;

import com.wufel.acmebank.entity.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account getAccount(String accountId);

    void fundTransfer(String sourceId, String destinationId, BigDecimal transferAmount);
}
