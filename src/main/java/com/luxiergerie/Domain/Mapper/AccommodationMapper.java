package com.luxiergerie.Domain.Mapper;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Domain.Entity.Accommodation;
import com.luxiergerie.Domain.Entity.Category;

import static java.lang.String.format;

public class AccommodationMapper {

    public static AccommodationDTO MappedAccommodationFrom(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setId(accommodation.getId());
        accommodationDTO.setName(accommodation.getName());
        accommodationDTO.setDescription(accommodation.getDescription());
        accommodationDTO.setImage(accommodation.getImage());
        accommodationDTO.setPrice(accommodation.getPrice());
        String setDisplayPrice = convertPriceToDisplay(accommodation.getPrice());
        accommodationDTO.setPriceToDisplay(setDisplayPrice);
        accommodationDTO.setCategory(accommodation.getCategory().getId());

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

        return accommodation;
    }

    private static String convertPriceToDisplay(Float price) {
        String convertedPrice = format("%.2f", price);
        return convertedPrice + " euros";
    }
}
