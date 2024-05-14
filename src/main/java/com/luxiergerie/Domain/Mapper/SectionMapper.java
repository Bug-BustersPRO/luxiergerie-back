package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Domain.Entity.Section;

public class SectionMapper {
    public static SectionDTO MappedSectionFrom(Section section) {
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getId());
        dto.setName(section.getName());
        dto.setImage(section.getImage());
        dto.setDescription(section.getDescription());
        dto.setTitle(section.getTitle());
        dto.setCategories(section.getCategories());
        return dto;
    }

    public static Section MappedSectionFrom(SectionDTO dto) {
        Section section = new Section();
        section.setName(dto.getName());
        section.setImage(dto.getImage());
        section.setDescription(dto.getDescription());
        section.setTitle(dto.getTitle());
        section.setCategories(dto.getCategories());
        return section;
    }

}
