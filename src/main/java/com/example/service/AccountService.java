package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRespository;

    @Autowired // constructor injection
    public AccountService(AccountRepository accountRespository) {
        this.accountRespository = accountRespository;
    }

    /**
     *  registers a new user account if validation passes
     * 
     * @param account, the account information to register
     * @return the registered account with id if successful
     *          Account with null id if username already existers,
     *          null if validation fails
     */
    public Account register(Account account) {
        // validation for user story 1
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null; // meaning the username is blank
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; // password is less than 4 characters
        }

        // check for duplicate username
        if (accountRespository.findByUsername(account.getUsername()) != null) {
            // special case: return an Account with null ID to signal duplicate username
            Account duplicateAccount = new Account();
            duplicateAccount.setUsername(account.getUsername());
            duplicateAccount.setPassword(account.getPassword());
            return duplicateAccount;
        }
        // if all validations pass, save the account
        return accountRespository.save(account);
    }

    /**
     * authenticates a user by username and password
     * 
     * @param account, the account containing username and password to validate
     * @return the authenticated account with id if successful, null otherwise
     */
    public Account login(Account account) {
        // find the account by username
        Account existingAccount = accountRespository.findByUsername(account.getUsername());

        // check if account esists and password matches
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }

        // authentication failed
        return null;
    }
}
