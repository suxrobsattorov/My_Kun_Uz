package com.company.entity;

import com.company.enums.ProfileStatus;
import com.company.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity{

    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String login;
    @Column
    private String password;
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "last_active_date")
    private LocalDateTime lastActiveDate;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
}
