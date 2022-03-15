package com.company;

import com.company.dto.ArticleDTO;
import com.company.dto.CommentDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.dto.filterDTO.CommentFilterDTO;
import com.company.dto.filterDTO.ProfileFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.repository.custom.ArticleCustomRepository;
import com.company.service.ArticleService;
import com.company.service.CommentService;
import com.company.service.ProfileService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.company.enums.CommentOrderStatus.*;
import static com.company.enums.ProfileOrderStatus.*;

@SpringBootTest
class Lesson62ApplicationTests {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;
    @Autowired
    private CommentService commentService;

    @Test
    void createProfile() {
        ArticleFilterDTO dto = new ArticleFilterDTO();
        dto.setTitle("l");
        articleCustomRepository.filterCriteria(dto).stream()
                .map(ArticleEntity::getId)
                .forEach(System.out::println);
    }

    @Test
    public void createArticle() {
        ArticleDTO dto = new ArticleDTO();
        dto.setTitle("Dollar kursi");
        dto.setContent("Dollar kursi pasaymoqda. Xa");
        dto.setProfileId(1);

        articleService.create(dto, 1);
    }

    @Test
    public void commentFilterTest(){
        CommentFilterDTO dto = new CommentFilterDTO();
        dto.setToDate(LocalDate.now());
        dto.setOrderBy(PROFILE_ID_DESC);
        commentService.filter(1, 4, dto).getContent()
                .stream().map(CommentDTO::getProfileId)
                .forEach(System.out::println);
    }

    @Test
    public void profileFilterTest(){
        ProfileFilterDTO dto = new ProfileFilterDTO();
        dto.setToDate(LocalDate.now());
        dto.setOrderBy(NAME_DESC);
        profileService.filter(2, 2, dto).getContent()
                .stream().map(ProfileDTO::getName)
                .forEach(System.out::println);
    }

}
