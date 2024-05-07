package com.wufel.acmebank.service;

import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {


    @Mock
    private AccountRepository accountRepository;
    private AccountService accountService;

    @Test
    void testThatGetAccountSuccessfully() {
        String accountId = "1";
        Account account = Account.builder().accountId(accountId).balance(BigDecimal.valueOf(100.00)).build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        accountService = new AccountServiceImpl(accountRepository);

        Account accountReturn = accountService.getAccount(accountId);
        assertEquals(accountReturn, account);
    }

    @Test
    void testAccountNotFound() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        accountService = new AccountServiceImpl(accountRepository);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount("1"));
    }
}
