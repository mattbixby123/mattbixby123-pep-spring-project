package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * creates a new message if validation passes
     * 
     * @param message, the message to be created
     * @return the created message with id if successful, null if validation fails
     */
    public Message createMessage(Message message) {
        // validate messaet text is not blank
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return null;
        }

        // validate message text is not over 255 chars
        if (message.getMessageText().length() > 255) {
            return null;
        }

        // validate the referecned user exisits
        if (message.getPostedBy() == null || accountRepository.findById(message.getPostedBy()).isEmpty()) {
            return null;
        }

        // set the current time if not provided
        if (message.getTimePostedEpoch() == null) {
            message.setTimePostedEpoch(System.currentTimeMillis());
        }

        // all validation passed, save the message
        return messageRepository.save(message);
    }

    /**
     * retrieves all messages
     * 
     * @return List of all messages
     */
    public List<Message> getAllMessages() {
        // simply a call to findAll() method from the JpaReposity
        return messageRepository.findAll();
    }

    /**
     * retrieves a message by its ID
     * 
     * @param messageId the ID of the message to retrieve
     * @return the message if found, null otherwise
     */
    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    /**
     * deletes a message by its ID
     * 
     * @param messageId, the ID of the message to be deleted
     * @return the number of rows affected (1 if deleted, 0 if not found)
     */
    public int deleteMessageById(int messageId) {
        // check if message exists before deletion
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1; // 1 row effected aka deleted successfully
        }
        return 0; // no rows deleted, message didnt exist
    }

    /**
     * update the text of a message by its ID
     * 
     * @param messageId, the id of the message to update
     * @param newMessageText, the new text for the message
     * @return the number of rows afffected (1 if updated, 0 if validation fails)
     */
    public int updateMessageText(int messageId, String newMessageText) {
        // validate message text is not blank
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return 0;
        }

        //validate message text is not over 255 chars
        if (newMessageText.length() > 255) {
            return 0;
        }

        //find the messasge by ID / we use the optional class because we may not find an Id
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isPresent()) {
            //update message text
            Message message = optionalMessage.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1; //message founds, 1 row affected
        }

        return 0; //no rows affected, message not found
    }

    /**
     * retrieves all message posted by a specific account
     * 
     * @param accountId, the id of the user
     * @return List of messages posted by the user
     */
    public List<Message> getMessagesByUser(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
