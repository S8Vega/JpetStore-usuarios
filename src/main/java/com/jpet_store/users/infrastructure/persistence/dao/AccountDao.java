package com.jpet_store.users.infrastructure.persistence.dao;

import com.jpet_store.users.infrastructure.persistence.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account, Long> {
    Account findByEmail(String email);
}
