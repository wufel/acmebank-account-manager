package com.wufel.acmebank.service;

import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Override
    public Account getAccount(String accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
