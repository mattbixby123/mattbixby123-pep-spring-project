package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * endpoint for user registion
     *  POST localhost:8080/register
     * 
     * @param account, the account information to register
     * @return ResponseEntity containg the registered account with HTTP status code
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registeredAccount = accountService.register(account);

        if (registeredAccount == null) {
            // registration failed due to validation errors (blank username or short password)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (registeredAccount.getAccountId() == null) {
            // registration failed due to dupilcate username
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // registration successful!
            return ResponseEntity.ok(registeredAccount);
        }
    }

    /**
     * endpoint for user login
     * POST localhost:8080/login
     * 
     * @param account, the account information for login
     * @return ResponseEntity containing the authenticated account with HTTP status code
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account authenticatedAccount = accountService.login(account);

        if (authenticatedAccount != null) {
            // login successful
            return ResponseEntity.ok(authenticatedAccount);
        } else {
            // login failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
