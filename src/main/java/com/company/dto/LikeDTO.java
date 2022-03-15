package com.company.dto;

import com.company.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDTO {
    private Integer id;

    private Integer articleId;

    private Integer commentId;

    private Integer profileId;

    private LocalDateTime createdDate;

    private LikeStatus status;
}
