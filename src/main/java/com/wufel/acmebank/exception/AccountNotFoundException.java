package com.wufel.acmebank.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String accountId) {
        super(String.format("Account for id %s cannot be found!", accountId));
    }
}
