package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
    private Integer id;

    @NotNull(message = "O'zi faqat name bor uni yam yozmabsan")
    @NotBlank(message = "O'zi faqat name bor uni yam yozmabsan")
    private String name;
}
