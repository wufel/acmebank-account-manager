package com.wufel.acmebank.service;

import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.exception.InsufficientFundException;
import com.wufel.acmebank.exception.InvalidTransferAmountException;
import com.wufel.acmebank.repository.AccountRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Captor
    private ArgumentCaptor<List<Account>> accountsCaptor;
    private AccountService accountService;
    
    private static final String DEFAULT_ACCOUNT = "1";
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100.00);

    @Test
    void testThatGetAccountSuccessfully() {
        Account account = Account.builder().accountId(DEFAULT_ACCOUNT).balance(HUNDRED).build();
        when(accountRepository.findById(DEFAULT_ACCOUNT)).thenReturn(Optional.of(account));
        accountService = new AccountServiceImpl(accountRepository);

        Account accountReturn = accountService.getAccount(DEFAULT_ACCOUNT);
        assertEquals(accountReturn, account);
    }

    @Test
    void testAccountNotFound() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        accountService = new AccountServiceImpl(accountRepository);

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount("1"));
    }

    @Test
    void testFundTransferSuccess() {
        String test = "TEST";
        Account account = Account.builder().accountId(DEFAULT_ACCOUNT).balance(HUNDRED).build();
        Account testAccount = Account.builder().accountId(test).balance(HUNDRED).build();
        when(accountRepository.findById(DEFAULT_ACCOUNT)).thenReturn(Optional.of(account));
        when(accountRepository.findById(test)).thenReturn(Optional.of(testAccount));
        accountService = new AccountServiceImpl(accountRepository);

        accountService.fundTransfer(DEFAULT_ACCOUNT, test, HUNDRED);
        verify(accountRepository).saveAll(accountsCaptor.capture());
        accountsCaptor.getValue()
                .forEach(ac -> assertThat(ac.getBalance(), Matchers.anyOf(Matchers.is(BigDecimal.valueOf(0.00)), Matchers.is(BigDecimal.valueOf(200.00)))));
    }

    @Test
    void testInsufficientFundForFundTransfer() {
        Account account = Account.builder().accountId(DEFAULT_ACCOUNT).balance(BigDecimal.ZERO).build();
        when(accountRepository.findById(DEFAULT_ACCOUNT)).thenReturn(Optional.of(account));
        accountService = new AccountServiceImpl(accountRepository);

        assertThrows(InsufficientFundException.class, () -> accountService.fundTransfer(DEFAULT_ACCOUNT, DEFAULT_ACCOUNT, HUNDRED));
    }

    @Test
    void testInvalidAmountForFundTransfer() {
        BigDecimal negative = BigDecimal.valueOf(-100.00);
        Account account = Account.builder().accountId(DEFAULT_ACCOUNT).balance(BigDecimal.ZERO).build();
        when(accountRepository.findById(DEFAULT_ACCOUNT)).thenReturn(Optional.of(account));
        accountService = new AccountServiceImpl(accountRepository);

        assertThrows(InvalidTransferAmountException.class, () -> accountService.fundTransfer(DEFAULT_ACCOUNT, DEFAULT_ACCOUNT, negative));
    }
}
