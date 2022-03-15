package com.company.service;

import com.company.dto.MessageDTO;
import com.company.entity.MessageEntity;
import com.company.exception.ItemNotFoundException;
import com.company.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MessageRepository messageRepository;

    public void sendEmail(MessageDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(dto.getSubject());
        message.setText(dto.getText());
        message.setTo(dto.getEmail());
        mailSender.send(message);

    }

    public MessageDTO saveMessage(MessageDTO dto) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSubject(dto.getSubject());
        messageEntity.setContent(dto.getText());
        messageEntity.setEmail(dto.getEmail());
        messageEntity.setUsed(dto.getUsed());
        messageEntity.setUsedDate(dto.getUsedDate());
        messageEntity.setId(dto.getId());

        messageRepository.save(messageEntity);
        dto.setId(messageEntity.getId());
        return dto;
    }

    public MessageEntity get(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Not found"));
    }

    public void setUsed(Integer id) {
        MessageEntity entity = get(id);
        entity.setUsed(true);
        entity.setUsedDate(LocalDateTime.now());
        messageRepository.save(entity);
    }


    public MessageDTO toDto(MessageEntity entity) {
        MessageDTO dto = new MessageDTO();
        dto.setId(entity.getId());
        dto.setSubject(entity.getSubject());
        dto.setText(entity.getContent());
        dto.setEmail(entity.getEmail());
        dto.setUsed(entity.getUsed());
        dto.setUsedDate(entity.getUsedDate());

        return dto;
    }

    public List<MessageDTO> getAll() {
        return messageRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public MessageDTO getById(Integer id) {
        return messageRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ItemNotFoundException("Message Not Found"));
    }

    public MessageDTO getLast() {
        return messageRepository.findTopByOrderByCreatedDateDesc().map(this::toDto)
                .orElseThrow(() -> new ItemNotFoundException("Message Not Found"));
    }

    public void deleteById(Integer id) {
        messageRepository.deleteById(id);
    }

    public List<MessageDTO> getTodays(Integer profileId) {
        return messageRepository.findTodays()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PageImpl<MessageDTO> getUnused(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<MessageEntity> page = messageRepository.findByUsedIsFalse(pageable);
        List<MessageDTO> dtoList = page.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());
        return new PageImpl<MessageDTO>(dtoList, pageable, page.getTotalElements());
    }

    public PageImpl<MessageDTO> getAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<MessageEntity> page = messageRepository.findAll(pageable);
        List<MessageDTO> dtoList = page.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());
        return new PageImpl<MessageDTO>(dtoList, pageable, page.getTotalElements());
    }


}
