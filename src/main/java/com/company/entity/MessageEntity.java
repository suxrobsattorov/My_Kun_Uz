package com.company.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "message")
@Setter
@Getter
@NoArgsConstructor
public class MessageEntity extends BaseEntity {
    @Column(name = "email")
    private String email;
    @Column(name = "subject")
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT NOT NULL")
    private String content;
    @Column(name = "used")
    private Boolean used = false;
    @Column(name = "used_date")
    private LocalDateTime usedDate;

}
