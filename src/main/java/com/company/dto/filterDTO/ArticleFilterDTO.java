package com.company.dto.filterDTO;

import com.company.enums.ArticleStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArticleFilterDTO {

    private Integer id;
    private String title;
    private Integer profileId;
    private ArticleStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;

}
