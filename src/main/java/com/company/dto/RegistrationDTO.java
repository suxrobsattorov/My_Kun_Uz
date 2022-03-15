package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {

    @NotBlank(message = "NAME ?")
    private String name;

    @NotBlank(message = "SURNAME ?")
    @Size(min = 3, max = 15, message = "3-15 uka")
    private String surname;

    @Email
    private String email;

    private String login;

    @Size(min = 3, max = 15)
    private String password;


}
