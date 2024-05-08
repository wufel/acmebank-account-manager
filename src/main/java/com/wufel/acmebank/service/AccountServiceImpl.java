package com.wufel.acmebank.service;

import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.exception.InsufficientFundException;
import com.wufel.acmebank.exception.InvalidTransferAmountException;
import com.wufel.acmebank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public void fundTransfer(String sourceId, String destinationId, BigDecimal transferAmount) {
        Account sourceAccount = getAccount(sourceId);
        Account destinationAccount = getAccount(destinationId);
        if (transferAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransferAmountException(sourceId, destinationId, transferAmount);
        }
        if (sourceAccount.getBalance().subtract(transferAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundException(sourceId, destinationId, transferAmount);
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
        accountRepository.saveAll(List.of(sourceAccount, destinationAccount));
    }
}
