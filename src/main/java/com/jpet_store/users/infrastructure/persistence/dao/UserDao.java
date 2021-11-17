package com.jpet_store.users.infrastructure.persistence.dao;

import com.jpet_store.users.infrastructure.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
}