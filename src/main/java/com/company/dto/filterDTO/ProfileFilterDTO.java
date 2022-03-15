package com.company.dto.filterDTO;

import com.company.enums.ProfileOrderStatus;
import com.company.enums.ProfileStatus;
import com.company.enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileFilterDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private ProfileStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ProfileOrderStatus orderBy = ProfileOrderStatus.ID_ASC;
}
