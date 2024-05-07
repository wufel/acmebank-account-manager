package com.wufel.acmebank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.model.AccountBalanceResponse;
import com.wufel.acmebank.service.AccountService;
import com.wufel.acmebank.service.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountsControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AccountService accountService;

    @Test
    void testGetAccountBalanceOk() throws Exception {
        String accountId = "1";
        BigDecimal balance = BigDecimal.valueOf(100.00);
        Account account = Account.builder().accountId(accountId).balance(balance).build();
        when(accountService.getAccount(accountId)).thenReturn(account);

        MvcResult result = this.mockMvc.perform(get("/accounts/balances/{id}", accountId))
                .andExpect(status().isOk())
                .andReturn();
        AccountBalanceResponse accountReturn = mapper.readValue(result.getResponse().getContentAsString(), AccountBalanceResponse.class);
        assertEquals(balance, accountReturn.getBalance());
    }

    @Test
    void testGetAccountBalanceNotFound() throws Exception {
        when(accountService.getAccount(any())).thenThrow(new AccountNotFoundException("1"));
        this.mockMvc.perform(get("/accounts/balances/{id}", "1"))
                .andExpect(status().isNotFound());
    }

}
