package com.company.service;

import com.company.dto.CommentDTO;
import com.company.dto.filterDTO.CommentFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentRepository;
import com.company.repository.custom.CommentCustomRepository;
import com.company.spec.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentCustomRepository commentCustomRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;

    public CommentDTO create(CommentDTO dto, Integer profileId) {
        dto.setCreatedDate(LocalDateTime.now());

        ArticleEntity articleEntity = articleService.get(dto.getArticleId());
        ProfileEntity profileEntity = profileService.get(profileId);

        CommentEntity entity = new CommentEntity();
        dto.setCreatedDate(LocalDateTime.now());
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setArticle(articleEntity);
        entity.setProfile(profileEntity);

        commentRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public CommentEntity get(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));
    }

    public List<CommentDTO> getAll() {
        return commentRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public CommentDTO getById(Integer id) {
        return commentRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ItemNotFoundException("Comment Not Found"));
    }

    public CommentDTO update(CommentDTO dto) {
        CommentEntity entity = get(dto.getId());
        entity.setContent(dto.getContent());
        commentRepository.save(entity);
        return toDto(entity);
    }

    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getByPid(Integer profileId) {
        return commentRepository.findByProfile_Id(profileId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PageImpl<CommentDTO> getByAid(int pageNum, int size, Integer articleId) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<CommentEntity> page = commentRepository.findByArticle_Id(articleId, pageable);
        List<CommentDTO> dtoList = page.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());
        return new PageImpl<CommentDTO>(dtoList, pageable, page.getTotalElements());
    }

    public PageImpl<CommentDTO> getAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<CommentEntity> page = commentRepository.findAll(pageable);
        List<CommentDTO> dtoList = page.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());
        return new PageImpl<CommentDTO>(dtoList, pageable, page.getTotalElements());
    }

    public CommentDTO toDto(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();

        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfile().getId());
        dto.setArticleId(entity.getArticle().getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    public PageImpl<CommentDTO> filter(int page, int size, CommentFilterDTO dto) {
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<CommentEntity> entityPage = commentCustomRepository.filter(dto, pageable);

        List<CommentDTO> dtos = entityPage.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());

        return new PageImpl<CommentDTO>(dtos, pageable, entityPage.getTotalElements());

    }

    public Page<CommentDTO> filterSpec(int page, int size, CommentFilterDTO dto) {
        String[] split = dto.getOrderBy().getQuery().split(" ");
        ProfileEntity profile = null;
        if (dto.getProfileId() != null) {
            profile = profileService.get(dto.getProfileId());
        }
        ArticleEntity article = null;
        if (dto.getArticleId() != null) {
            article = articleService.get(dto.getArticleId());
        }

        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.fromString(split[1]), split[0]));

        Specification<CommentEntity> spec =
                Specification.where(CommentSpecification.idIsNotNull("id"));
        if (dto.getId() != null) {
            spec.and(CommentSpecification.equal("id", dto.getId()));
        }
        if (dto.getProfileId() != null) {
            spec.and(CommentSpecification.equal("profile", profile));
        }
        if (dto.getArticleId() != null) {
            spec.and(CommentSpecification.equal("article", article));
        }
        if (dto.getFromDate() != null) {
            spec.and(CommentSpecification.fromDate(dto.getFromDate()));
        }
        if (dto.getToDate() != null) {
            spec.and(CommentSpecification.toDate(dto.getToDate()));
        }

        Page<CommentEntity> entityPage = commentRepository.findAll(spec, pageable);
        List<CommentDTO> dtos = entityPage.getContent().stream()
                .map(this::toDto).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());

    }

}
