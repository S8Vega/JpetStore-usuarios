package com.jpet_store.users.domain.service;

import com.jpet_store.users.infrastructure.persistence.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends CrudServiceInterface<Account, Long>, UserDetailsService {
    Account findByEmail(String email);
}
