package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.enums.Role;
import com.company.service.ArticleService;
import com.company.service.ProfileService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/article")
@Api(tags = "Article Controller")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;

    @PostMapping
    @ApiOperation(value = "Create a New Article", notes = "Create a New Article for Mederator")
    public ResponseEntity create(@Valid @RequestBody ArticleDTO dto,
                                 HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.MODERATOR_ROLE);
        ArticleDTO response = articleService.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @ApiOperation(value = "Create a New Article", notes = "Create a New Article for Mederator")
    public ResponseEntity update(@Valid @RequestBody ArticleDTO dto,
                                 HttpServletRequest request) {

        JwtUtil.getProfile(request, Role.MODERATOR_ROLE);
        String response = articleService.update(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/publish/{id}")
    @ApiOperation(value = "Publishing Article", notes = "for PUBLISHER_ROLE")
    public ResponseEntity publish(@PathVariable Integer id,
                                  HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.PUBLISHER_ROLE);

        return ResponseEntity.ok(articleService.publish(id, jwtDTO.getId()));

    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deleting Article", notes = "By Id")
    public ResponseEntity deleteById(@PathVariable("id") Integer id,
                                     HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.MODERATOR_ROLE);

        articleService.deleteById(id);
        return ResponseEntity.ok("Success...");
    }

    @GetMapping
    @ApiOperation(value = "Get All Articles", notes = "Auth required")
    public ResponseEntity getAll(HttpServletRequest request) {
        JwtUtil.getProfile(request);
        return ResponseEntity.ok(articleService.getAll());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get an Article", notes = "By Id")
    public ResponseEntity getById(@PathVariable Integer id,
                                  HttpServletRequest request) {

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PostMapping("/filter")
    @ApiOperation(value = "Filtering", notes = "By Any Params")
    public ResponseEntity filter(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestBody ArticleFilterDTO dto) {

        return ResponseEntity.ok(articleService.filterSpecification(page, size, dto));
    }

}
