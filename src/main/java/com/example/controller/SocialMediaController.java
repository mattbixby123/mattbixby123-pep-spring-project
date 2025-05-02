package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * DONE / TESTS passed 38/28:  You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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

    /**
     * endpoint for creating a new message
     * POST localhost:8080/messages
     * 
     * @param message, the message to be created
     * @return ResponseEntity containing the created messsage with HTTP status code
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            // message creation successful
            return ResponseEntity.ok(createdMessage);
        } else {
            // message created failed due to validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * endpooint for retrieving all messages
     * GET localhost:8080/messages
     * 
     * @return ResponseEntity containing a list of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        // always return 200 OK status as specified in the requirements
        return ResponseEntity.ok(messages);
    }

    /**
     * endpoint for retrieving a message by its ID
     * GET localhost:8080/messages/{messageId}
     * 
     * @param messageId, the ID of the message to retrieve
     * @return ResponseEntity containing the message if found
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);

        // always return 200 ok stats as specified in the user story
        // even if the message is not found, the body will return null
        return ResponseEntity.ok(message);
    }

    /**
     * endpoint for deleting a message by its ID
     * DELETE localhost:8080/messages/{messageId}
     * 
     * @param messageId the id of the message to delete
     * @return ResponseEntity containing the number of rows affected
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);

        if (rowsAffected > 0) {
            // always return 200 ok status as specified in the user story
            return ResponseEntity.ok(rowsAffected);
        } else {
            // message did not exist
            return ResponseEntity.ok().build(); // turns the 0 into an empty response body
        }
    }

    /**
     * edpoint for updating the text of a message by its ID
     * 
     * @param messageId, the id of the message to update
     * @param requestBody, JSON containing the new message text
     * @return ResponseEntity containig the number of rows affeceted
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText (@PathVariable int messageId, @RequestBody Map<String, String> requestBody) {
        String newMessageText = requestBody.get("messageText");
        if (newMessageText == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        int rowsAffected = messageService.updateMessageText(messageId, newMessageText);

        if (rowsAffected > 0) {
            // update was successful
            return ResponseEntity.ok(rowsAffected);
        } else {
            //update failecd
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * endpoint for retrieving all message posted by a specific user
     * GET localhost:8080/accounts/{accountId}/messages
     * 
     * @param accountId, the id of the user
     * @return ResponseEntity containing a list of messages
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);

        // always return 200 ok status as specified in the user story
        return ResponseEntity.ok(messages);
    }

}
