package com.company.controller;

import com.company.dto.CommentDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.filterDTO.CommentFilterDTO;
import com.company.exception.BadRequestException;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
@Api(tags = "Comment Controller")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ApiOperation(value = "Creating New Comment", notes = "Auth required")
    private ResponseEntity create(@Valid @RequestBody CommentDTO dto,
                                  HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);

        return ResponseEntity.ok(commentService.create(dto, jwtDTO.getId()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Comment By Id")
    public CommentDTO getById(@PathVariable("id") Integer id,
                              HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);

        return commentService.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "Updating By ID", notes = "For only comments you written")
    public ResponseEntity update(@RequestBody CommentDTO dto,
                                 HttpServletRequest request){

        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        if (!jwtDTO.getId().equals(dto.getProfileId())){
            throw new BadRequestException("You have not written thi Comment!!!");
        }
        return ResponseEntity.ok(commentService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete By ID", notes = "For only comments you written")
    public void deleteById(@PathVariable("id") Integer id,
                           HttpServletRequest request){
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        if (!jwtDTO.getId().equals(commentService.getById(id).getProfileId())){
            throw new BadRequestException("You have not written thi Comment!!!");
        }
        commentService.deleteById(id);
    }

    @GetMapping("/pid/{pid}")
    @ApiOperation(value = "Get By ProfileId", notes = "None required")
    public ResponseEntity getByPid(@PathVariable Integer pid){
        return ResponseEntity.ok(commentService.getByPid(pid));
    }

    @GetMapping("/aid/{aid}")
    @ApiOperation(value = "Get Articles' comments page", notes = "None required")
    public ResponseEntity getByAid(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                   @PathVariable Integer aid){
        return ResponseEntity.ok(commentService.getByAid(page, size, aid));
    }

    @GetMapping
    @ApiOperation(value = "Get All on page")
    public ResponseEntity getAll(@RequestParam("page") int page,
                                   @RequestParam("size") int size){
        return ResponseEntity.ok(commentService.getAll(page, size));
    }

    @PostMapping("/filter")
    @ApiOperation(value = "Filter", notes = "required IS False")
    public ResponseEntity filter(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 @RequestBody CommentFilterDTO dto){
        return ResponseEntity.ok(commentService.filterSpec(page, size, dto));
    }

}
