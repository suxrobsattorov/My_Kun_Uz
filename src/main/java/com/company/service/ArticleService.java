package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.filterDTO.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ArticleStatus;
import com.company.exception.BadRequestException;
import com.company.repository.custom.ArticleCustomRepository;
import com.company.repository.ArticleRepository;
import com.company.spec.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;

    public ArticleDTO create(ArticleDTO dto, Integer userId) {
        ProfileEntity profileEntity = profileService.get(userId);

        if (dto.getTitle() == null || dto.getTitle().isEmpty())
            throw new BadRequestException("Title is empty");

        if (dto.getContent() == null || dto.getContent().isEmpty())
            throw new BadRequestException("Content is empty");

        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfile(profileEntity);
        entity.setStatus(dto.getStatus());

        articleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<ArticleDTO> getAll() {
        return articleRepository.findAll().stream()
                .map(this::ToDto).collect(Collectors.toList());
    }

    public ArticleDTO getById(Integer id) {
        return articleRepository.findById(id).map(this::ToDto)
                .orElseThrow(() -> new RuntimeException("Bunday article NOT EXIST"));
    }

    public String publish(Integer id, Integer userId) {
        ArticleEntity entity = get(id);
        ProfileEntity publisher = profileService.get(userId);
        entity.setPublisher(publisher);
        entity.setPublishedDate(LocalDateTime.now());
        return "Published";
    }

    public String update(ArticleDTO dto) {
        ArticleEntity entity = get(dto.getId());
        entity.setContent(dto.getContent());
        entity.setTitle(dto.getTitle());
        entity.setStatus(ArticleStatus.BLOCKED);
        articleRepository.save(entity);

        return "Successfully";

    }

    public ArticleDTO deleteById(Integer id) {
        ArticleDTO dto = getById(id);
        articleRepository.deleteById(id);
        return dto;
    }


    public ArticleDTO ToDto(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfile().getId());
        if (entity.getPublisher() != null)
            dto.setPublisherId(entity.getPublisher().getId());
        dto.setCreatedDate(entity.getPublishedDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public PageImpl<ArticleDTO> filter(Pageable pageable, ArticleFilterDTO dto) {
        PageImpl<ArticleEntity> entityPage = articleCustomRepository.filter(pageable, dto);

        List<ArticleDTO> dtoList = entityPage.getContent().stream()
                .map(this::ToDto).collect(Collectors.toList());

        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public Page<ArticleDTO> filterSpecification(int page, int size, ArticleFilterDTO dto) {
        Pageable pageable = PageRequest.of(page, size);
        ProfileEntity profile = profileService.get(dto.getProfileId());

        Specification<ArticleEntity> spec = null;
        if (dto.getStatus() != null) {
            spec = Specification.where(ArticleSpecification.status(dto.getStatus()));
        } else {
            spec = Specification.where(ArticleSpecification.status(ArticleStatus.PUBLISHED));
        }
        if (dto.getId() != null) {
            ArticleSpecification.equal("id", dto.getId());
        }
        if (dto.getProfileId() != null) {
            spec.and(ArticleSpecification.equal("profile", profile));
        }
        if (dto.getTitle() != null) {
            spec.and(ArticleSpecification.like("title", dto.getTitle()));
        }
        if (dto.getFromDate() != null) {
            spec.and(ArticleSpecification.greaterThanOrEqualTo("createdDate", dto.getFromDate()));
        }
        if (dto.getToDate() != null) {
            spec.and(ArticleSpecification.lessThanOrEqualTo("createdDate", dto.getToDate()));
        }

        Page<ArticleEntity> entitiyPage = articleRepository.findAll(spec, pageable);
        List<ArticleDTO> content = entitiyPage.getContent().stream()
                .map(this::ToDto).collect(Collectors.toList());

        return new PageImpl<ArticleDTO>(content, pageable, entitiyPage.getTotalElements());
    }

    public ArticleEntity get(Integer id) {
        return articleRepository.getById(id);
    }
}
