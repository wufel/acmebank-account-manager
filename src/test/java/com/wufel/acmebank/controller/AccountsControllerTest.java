package com.wufel.acmebank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wufel.acmebank.entity.Account;
import com.wufel.acmebank.exception.AccountNotFoundException;
import com.wufel.acmebank.exception.InsufficientFundException;
import com.wufel.acmebank.exception.InvalidTransferAmountException;
import com.wufel.acmebank.model.AccountBalanceResponse;
import com.wufel.acmebank.model.FundTransferRequest;
import com.wufel.acmebank.model.FundTransferResponse;
import com.wufel.acmebank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        MvcResult result = this.mockMvc.perform(get("/accounts/{id}/balance", accountId))
                .andExpect(status().isOk())
                .andReturn();
        AccountBalanceResponse accountReturn = mapper.readValue(result.getResponse().getContentAsString(), AccountBalanceResponse.class);
        assertEquals(balance, accountReturn.getBalance());
    }

    @Test
    void testGetAccountBalanceNotFound() throws Exception {
        when(accountService.getAccount(any())).thenThrow(new AccountNotFoundException("1"));
        this.mockMvc.perform(get("/accounts/{id}/balance", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFundTransferSuccess() throws Exception {
        String accountId = "1";
        BigDecimal balance = BigDecimal.valueOf(100.00);
        Account account = Account.builder().accountId(accountId).balance(balance).build();
        when(accountService.getAccount(any())).thenReturn(account);

        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, accountId, balance);

        MvcResult result = this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isOk())
                .andReturn();
        FundTransferResponse fundTransferResponse = mapper.readValue(result.getResponse().getContentAsString(), FundTransferResponse.class);
        assertEquals(fundTransferResponse.getStatus(), FundTransferResponse.StatusEnum.SUCCESS);
    }

    @Test
    void testAccountNotFoundOnFundTransfer() throws Exception {
        String accountId = "1";
        BigDecimal amount = BigDecimal.valueOf(100.00);
        Mockito.doThrow(new AccountNotFoundException(accountId)).when(accountService).fundTransfer(accountId, accountId, amount);

        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, accountId, amount);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFundTransferFailedWithInsufficientFund() throws Exception {
        String accountId = "1";
        BigDecimal amount = BigDecimal.valueOf(100.00);
        Mockito.doThrow(new InsufficientFundException(accountId, accountId, amount)).when(accountService).fundTransfer(accountId, accountId, amount);

        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, accountId, amount);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testFundTransferFailedWithNegativeAmount() throws Exception {
        String accountId = "1";
        BigDecimal amount = BigDecimal.valueOf(-100.00);
        Mockito.doThrow(new InvalidTransferAmountException(accountId, accountId, amount)).when(accountService).fundTransfer(accountId, accountId, amount);

        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, accountId, amount);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testFundTransferMissingFieldsSourceAccountId() throws Exception {
        String accountId = "1";
        BigDecimal amount = BigDecimal.valueOf(-100.00);
        FundTransferRequest fundTransferRequest = new FundTransferRequest(null, accountId, amount);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFundTransferMissingFieldsDestinationAccountId() throws Exception {
        String accountId = "1";
        BigDecimal amount = BigDecimal.valueOf(-100.00);
        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, null, amount);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFundTransferMissingAmount() throws Exception {
        String accountId = "1";
        FundTransferRequest fundTransferRequest = new FundTransferRequest(accountId, accountId, null);
        this.mockMvc.perform(post("/accounts/fund-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fundTransferRequest)))
                .andExpect(status().isBadRequest());
    }

}
