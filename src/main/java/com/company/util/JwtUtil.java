package com.company.util;

import com.company.dto.ProfileJwtDTO;
import com.company.enums.Role;
import com.company.exception.ForbiddenException;
import com.company.exception.UnauthorizaedException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    private final static String secretKey = "kalitso'z";

    public static String createJwt(Integer id) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));
        jwtBuilder.setIssuer("kun.uz");
        String jwt = jwtBuilder.compact();
        return jwt;
    }

    public static String createJwt(Integer id, Role role) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));
        jwtBuilder.setIssuer("kun.uz");
        jwtBuilder.claim("role", role.name());

        String jwt = jwtBuilder.compact();
        return jwt;
    }

    public static ProfileJwtDTO decodeJwt(String jwt){
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(secretKey);
        Jws jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = (Claims) jws.getBody();
        String id = claims.getSubject();
        Role role = Role.valueOf((String) claims.get("role"));

        return new ProfileJwtDTO(Integer.parseInt(id), role);
    }

    public static Integer decodeAndGetId(String jwt){
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(secretKey);
        Jws jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = (Claims) jws.getBody();
        String id = claims.getSubject();

        return Integer.parseInt(id);
    }

    public static Integer getCurrentUser(HttpServletRequest request) throws RuntimeException {

        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("METHOD NOT ALLOWED");
        }
        return userId;
    }

    public static ProfileJwtDTO getProfile(HttpServletRequest request, Role requiredRole) {

        ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("jwtDTO");

        if (dto == null) {
            throw new UnauthorizaedException("Not Authorized");
        }

        if (!dto.getRole().equals(requiredRole)) {
            throw new ForbiddenException("403 Forbidden");
        }

        return dto;
    }

    public static ProfileJwtDTO getProfile(HttpServletRequest request) {

        ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("jwtDTO");

        if (dto == null) {
            throw new UnauthorizaedException("Not Authorized");
        }

        return dto;
    }
}
