package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
// Extending the JpaRepository automatically provides methods like save(), 
// findById(), findAll(), etc. The only cutom method we need to add is findByUsername()
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // find account by username to check for duplicates during registration
    Account findByUsername(String username);
}
