package com.jpet_store.users.domain.service;

import com.jpet_store.users.infrastructure.persistence.dao.AccountDao;
import com.jpet_store.users.infrastructure.persistence.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements CrudServiceInterface<Account, Long>{

    @Autowired
    private AccountDao accountDao;

    @Transactional(readOnly = true)
    @Override
    public Account findById(Long id) {
        return accountDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> findAll() {
        return (List<Account>) accountDao.findAll();
    }

    @Transactional
    @Override
    public Account save(Account account) {
        account.setPassword(DigestUtils.md5DigestAsHex(account.getPassword().getBytes()));
        return accountDao.save(account);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        accountDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Account findByAlias(String alias) {
        return accountDao.findByEmail(alias);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountDao.findByEmail(email);
        List<GrantedAuthority> roles = new ArrayList<>();
        //roles.add(new SimpleGrantedAuthority(account.getRol().toString()));
        return new User(account.getEmail(), "{noop}" + account.getPassword(), roles);
    }
}
