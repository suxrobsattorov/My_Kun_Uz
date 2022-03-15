package com.company.dto;

import com.company.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private String login;

    @Size(min = 3, max = 15)
    private String password;

    @Email
    private String email;

    private Role role;

    private LocalDateTime createdDate;

    private String jwt; // token
}
