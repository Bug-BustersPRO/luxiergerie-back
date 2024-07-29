package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.SectionDTO;
import com.luxiergerie.Model.Entity.Section;

public class SectionMapper {
    public static SectionDTO MappedSectionFrom(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(section.getId());
        sectionDTO.setName(section.getName());
        sectionDTO.setImage(section.getImage());
        sectionDTO.setDescription(section.getDescription());
        sectionDTO.setTitle(section.getTitle());
        sectionDTO.setCategories(section.getCategories());
        return sectionDTO;
    }

    public static Section MappedSectionFrom(SectionDTO sectionDTO) {
        Section section = new Section();
        section.setName(sectionDTO.getName());
        section.setImage(sectionDTO.getImage());
        section.setDescription(sectionDTO.getDescription());
        section.setTitle(sectionDTO.getTitle());
        section.setCategories(sectionDTO.getCategories());
        return section;
    }

}