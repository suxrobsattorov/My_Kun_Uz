package com.company.dto;

import com.company.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileJwtDTO {
    private Integer id;
    private Role role;
}
