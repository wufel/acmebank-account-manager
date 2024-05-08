package com.wufel.acmebank.controller;

import com.wufel.acmebank.api.AccountsApi;
import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.model.AccountBalanceResponse;
import com.wufel.acmebank.model.FundTransferRequest;
import com.wufel.acmebank.model.FundTransferResponse;
import com.wufel.acmebank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountsController implements AccountsApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountBalanceResponse> accountsBalancesIdGet(String id) {
        log.info("get account balance request received for accountId={}", id);
        Account account = accountService.getAccount(id);
        AccountBalanceResponse accountBalanceResponse = new AccountBalanceResponse(account.getAccountId(), account.getBalance());
        return ResponseEntity.ok(accountBalanceResponse);
    }

    @Override
    public ResponseEntity<FundTransferResponse> accountsFundTransfersPost(FundTransferRequest fundTransferRequest) {
        log.info("account transfer request {} received", fundTransferRequest);
        accountService.fundTransfer(fundTransferRequest.getSourceAccountId(), fundTransferRequest.getDestinationAccountId(), fundTransferRequest.getAmount());
        FundTransferResponse fundTransferResponse = new FundTransferResponse(fundTransferRequest.getSourceAccountId(),
                fundTransferRequest.getDestinationAccountId(),
                fundTransferRequest.getAmount(),
                FundTransferResponse.StatusEnum.SUCCESS);
        return ResponseEntity.ok(fundTransferResponse);
    }

}
