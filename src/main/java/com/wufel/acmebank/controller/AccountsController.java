package com.wufel.acmebank.controller;

import com.wufel.acmebank.api.AccountsApi;
import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.model.AccountBalanceResponse;
import com.wufel.acmebank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountsController implements AccountsApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountBalanceResponse> accountsBalancesIdGet(String id) {
        log.info("get account balance received for accountId={}", id);
        Account account = accountService.getAccount(id);
        AccountBalanceResponse accountBalanceResponse = new AccountBalanceResponse(account.getAccountId(), account.getBalance());
        return ResponseEntity.ok(accountBalanceResponse);
    }
}
