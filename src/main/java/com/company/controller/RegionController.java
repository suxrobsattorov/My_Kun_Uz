package com.company.controller;

import com.company.dto.ProfileJwtDTO;
import com.company.dto.RegionDTO;
import com.company.enums.Role;
import com.company.service.ProfileService;
import com.company.service.RegionService;
import com.company.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/region")
@Api(tags = "Regions", value = "Bunda ham faqat admin_role")
public class RegionController {

    @Autowired
    private RegionService regionService;
    @Autowired
    private ProfileService profileService;

    @PostMapping
    @ApiOperation("create region")
    public ResponseEntity create(@Valid @RequestBody RegionDTO dto,
                                 HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        RegionDTO response = regionService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @ApiOperation("update region")
    public ResponseEntity update(@RequestBody RegionDTO dto,
                                 HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        return ResponseEntity.ok(regionService.update(dto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("delete region")
    public ResponseEntity deleteById(@PathVariable Integer id,
                                     HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request, Role.ADMIN_ROLE);
        regionService.deleteById(id);
        return ResponseEntity.ok("Success...");
    }

    @GetMapping
    @ApiOperation("get ALL List")
    public ResponseEntity getAll(HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/{id}")
    @ApiOperation("@ApiOperation(value = \"getById methodi\")")
    public ResponseEntity getById(@PathVariable Integer id,
                                  HttpServletRequest request) {
        ProfileJwtDTO jwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(regionService.getById(id));
    }

}
