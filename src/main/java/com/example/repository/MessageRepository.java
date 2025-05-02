package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // includes save() needed for create message us#3
    // includes findAll() needed for get all messages us#4
    // includes findById() needed for retrieving messages by messageId us#5
    // includes deleteById() and existsById() both needed for user story #6
    // includes save() and findById() needed for user story #7
    // user story 8 requires a custom method not provided by JpaReposity
    List<Message> findByPostedBy(Integer accountId);
}
