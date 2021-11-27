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
import org.springframework.web.bind.annotation.*;

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
            if (account.getRole() == null) {
                account.setRole("USUARIO");
            }
            accountService.save(account);
            response.put("message", "usuario creado correctamente");
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
            Account account = accountService.findByEmail(request.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails userDetails = accountService.loadUserByUsername(request.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwtUtil.generateToken(userDetails));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountService.findById(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountById(@PathVariable Long id, @RequestBody Account account) {
        Account accountUpdate = accountService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (accountUpdate != null) {
            account.setId(id);
            accountService.save(account);
            response.put("message", "usuario actualizado correctamente");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", jwtUtil.isTokenValid(token) ? "true" : "false");
        response.put("user", jwtUtil.isTokenValid(token) ? accountService.findByEmail(jwtUtil.extractUsername(token)) : "usuario no logeado");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        jwtUtil.removeToken(request.get("jwt"));
        response.put("message", "sesion cerrada correctamente");
        return ResponseEntity.ok(response);
    }
}
