package com.company.service;

import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.auth.AuthorizationDTO;
import com.company.dto.MessageDTO;
import com.company.entity.MessageEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import com.company.enums.Role;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.exception.UnauthorizaedException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EmailService emailService;
    @Value("${app.host}")
    private String host;

    public ProfileDTO authorization(AuthorizationDTO dto) {
        String password = DigestUtils.md5Hex(dto.getPassword());

        Optional<ProfileEntity> optional = profileRepository
                .findByLoginAndPassword(dto.getLogin(), password);

        if (!optional.isPresent()) {
            optional = profileRepository
                    .findByLoginAndPassword(dto.getLogin(), dto.getPassword());
            if (!optional.isPresent())
                throw new RuntimeException("Login or Password incorrect");
        }

        if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new UnauthorizaedException("Your email was not verified");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(optional.get().getName());
        profileDTO.setSurname(optional.get().getSurname());
        profileDTO.setJwt(JwtUtil.createJwt(optional.get().getId(), optional.get().getRole()));
        return profileDTO;
    }

    public void registration(RegistrationDTO dto) {


        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email invalid");
        }
        if (profileRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new BadRequestException("Username invalid");
        }

        String password = DigestUtils.md5Hex(dto.getPassword());

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setLogin(dto.getLogin());
        entity.setPassword(password);
        entity.setStatus(ProfileStatus.CREATED);

        entity.setRole(Role.USER_ROLE);

        profileRepository.save(entity);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO = emailService.saveMessage(messageDTO);

        String jwt = JwtUtil.createJwt(messageDTO.getId());

        StringBuilder builder = new StringBuilder();
        builder
                .append("Hello ")
                .append(dto.getName())
                .append(", Firstly, thanks for registration\n")
                .append("To verify your email for Kun.Uz click on link below 👇\n")
                .append("http://")
                .append(host)
                .append(":8080/auth/verification/")
                .append(jwt);

        String title = "Kun.Uz verification";

        messageDTO.setSubject(title);
        messageDTO.setText(builder.toString());
        messageDTO.setEmail(dto.getEmail());

        emailService.sendEmail(messageDTO);
        emailService.saveMessage(messageDTO);
    }

    public void verify(String jwt) {
        Integer id = JwtUtil.decodeAndGetId(jwt);

        MessageEntity message = emailService.get(id);

        ProfileEntity profile = profileRepository.findByEmail(message.getEmail())
                .orElseThrow(() -> new ItemNotFoundException("User Not Found"));

        emailService.setUsed(id);
        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);
    }

}
