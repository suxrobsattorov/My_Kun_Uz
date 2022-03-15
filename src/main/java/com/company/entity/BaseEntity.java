package com.company.entity;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
