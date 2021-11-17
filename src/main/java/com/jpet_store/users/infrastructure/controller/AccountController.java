package com.jpet_store.users.infrastructure.controller;

import com.jpet_store.users.domain.dto.AuthenticationRequest;
import com.jpet_store.users.domain.service.AccountService;
import com.jpet_store.users.infrastructure.persistence.entity.Account;
import com.jpet_store.users.infrastructure.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AccountController {

    @Autowired
    @Qualifier("AccountServiceImpl")
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Account account) {
        Map<String, Object> response = new HashMap<>();
        Account newAccount = accountService.findByEmail(account.getEmail());
        if (newAccount == null) {
            accountService.save(account);
            response.put("mensaje", "usuario creado correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } else {
            response.put("mensaje", "el email del usuario: " + account.getEmail() + " no esta disponible");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest request) {
        try {
            request.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
            Account account = accountService.findByEmail(request.getCorreo());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword()));
            UserDetails userDetails = accountService.loadUserByUsername(request.getCorreo());
            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwtUtil.generateToken(userDetails));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
