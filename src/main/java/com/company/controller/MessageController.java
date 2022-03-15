package com.company.controller;

import com.company.dto.MessageDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.Role;
import com.company.service.EmailService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Api(tags = "Email Message Control")
public class MessageController {

    @Autowired
    private EmailService emailService;


    @GetMapping("/{id}")
    @ApiOperation(value = "@ApiOperation(value = \"get BY ID\")")
    public MessageDTO getById(@PathVariable("id") Integer id,
                              HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);

        return emailService.getById(id);
    }

    @GetMapping("/last")
    @ApiOperation(value = "Value bo'lmasa bo'lmas", notes = "@ApiOperation(notes = \"Oxirgi like By ID\")")
    public MessageDTO getById(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);

        return emailService.getLast();
    }


    @GetMapping("/today")
    @ApiOperation(value = "Bugungi layklar")
    public ResponseEntity getByPid(@PathVariable Integer pid,
                                   HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getTodays(pid));
    }

    @GetMapping("unused")
    @ApiOperation(value = "Bu value", notes = "Bu esa notes", nickname = "bu nickname")
    public ResponseEntity getUnused(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getUnused(page, size));
    }

    @GetMapping
    @ApiOperation(value = "Get Page of All Likes")
    public ResponseEntity getAll(@RequestParam("page") int page,
                                 @RequestParam("size") int size,
                                 HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(emailService.getAll(page, size));
    }

}
