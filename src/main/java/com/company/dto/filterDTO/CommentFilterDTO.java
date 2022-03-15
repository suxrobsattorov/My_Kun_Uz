package com.company.dto.filterDTO;

import com.company.enums.CommentOrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentFilterDTO {

    private Integer id;

    private Integer articleId;

    private Integer profileId;
    private CommentOrderStatus orderBy = CommentOrderStatus.ID_ASC;
    private LocalDate fromDate;
    private LocalDate toDate;
}
