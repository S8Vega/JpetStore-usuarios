package com.jpet_store.users.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Account extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String address;

    private String fullName;
}
