package com.company.service;

import com.company.dto.RegionDTO;
import com.company.entity.RegionEntity;
import com.company.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    @Autowired
    RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto){
        RegionEntity entity = new RegionEntity();
        entity.setName(dto.getName());

        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<RegionDTO> getAll(){
        return regionRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public RegionDTO getById(Integer id){
        return regionRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new RuntimeException("REGION NOT EXIST"));
    }

    public void deleteById(Integer id){
        regionRepository.deleteById(id);
    }

    public String update(RegionDTO dto){
        RegionDTO old = getById(dto.getId());

        if (dto.getName() != null && dto.getName() != old.getName())
            regionRepository.updateNameById(dto.getId(), dto.getName());

        return "Succesfully :)";
    }


    public RegionDTO toDto(RegionEntity entity){
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

}
