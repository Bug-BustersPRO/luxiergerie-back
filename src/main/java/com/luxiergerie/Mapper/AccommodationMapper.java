package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Model.Entity.Category;

public class AccommodationMapper {

    public static AccommodationDTO MappedAccommodationFrom(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setId(accommodation.getId());
        accommodationDTO.setName(accommodation.getName());
        accommodationDTO.setDescription(accommodation.getDescription());
        accommodationDTO.setImage(accommodation.getImage());
        accommodationDTO.setPrice(accommodation.getPrice());
        accommodationDTO.setReservable(accommodation.isReservable());
        accommodationDTO.setCategory(accommodation.getCategory().getId());
        accommodationDTO.setQuantity(accommodation.getQuantity());

        return accommodationDTO;
    }

    public static Accommodation MappedAccommodationFrom(AccommodationDTO accommodationDTO) {
        Accommodation accommodation = new Accommodation();
        accommodation.setName(accommodationDTO.getName());
        accommodation.setDescription(accommodationDTO.getDescription());
        accommodation.setImage(accommodationDTO.getImage());
        accommodation.setPrice(accommodationDTO.getPrice());
        accommodation.setCategory(new Category());
        accommodation.setPurchases(accommodationDTO.getPurchases());
        accommodation.setReservable(accommodationDTO.isReservable());
        accommodationDTO.setQuantity(accommodation.getQuantity());

        return accommodation;
    }

}