package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.service.AuthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation(value = "Loginning", notes = "Sign in", nickname = "searching")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ProfileDTO.class, responseContainer = "List") })
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthorizationDTO dto) {
        ProfileDTO response = authService.authorization(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    @ApiOperation(value = "Registration", notes = "without Token")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ProfileDTO.class, responseContainer = "List") })
    public ResponseEntity registration(@Valid @RequestBody RegistrationDTO dto) {
        authService.registration(dto);
        return ResponseEntity.ok("Successfully!!!");
    }

    @GetMapping("/verification/{jwt}")
    @ApiOperation(value = "Verify account by Email")
    public ResponseEntity verify(@ApiParam(value = "id", required = true, example = "This id Id")
                                 @PathVariable String jwt){
        authService.verify(jwt);
        System.out.println(jwt);
        return ResponseEntity.ok("Success");
    }



}
