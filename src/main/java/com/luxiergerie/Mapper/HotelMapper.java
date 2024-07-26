package com.luxiergerie.Mapper;

import com.luxiergerie.DTO.HotelDTO;
import com.luxiergerie.Model.Entity.Hotel;

public class HotelMapper {

    public static HotelDTO MappedHotelFrom(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setImage(hotel.getImage());
        hotelDTO.setColors(hotel.getColors());
        hotelDTO.setBackgroundImage(hotel.getBackgroundImage());

        return hotelDTO;
    }

    public static Hotel MappedHotelFrom(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setName(hotelDTO.getName());
        hotel.setImage(hotelDTO.getImage());
        hotel.setColors(hotelDTO.getColors());
        hotel.setBackgroundImage(hotelDTO.getBackgroundImage());

        return hotel;
    }
}
