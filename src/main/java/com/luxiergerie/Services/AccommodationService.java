package com.luxiergerie.Services;

import com.luxiergerie.DTO.AccommodationDTO;
import com.luxiergerie.Model.Entity.Accommodation;
import com.luxiergerie.Model.Entity.Category;
import com.luxiergerie.Repository.AccommodationRepository;
import com.luxiergerie.Repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.luxiergerie.Mapper.AccommodationMapper.MappedAccommodationFrom;
import static java.util.Objects.nonNull;

@Service
public class AccommodationService {

    private CategoryRepository categoryRepository;

    private AccommodationRepository accommodationRepository;

    public AccommodationService(CategoryRepository categoryRepository, AccommodationRepository accommodationRepository) {
        this.categoryRepository = categoryRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional
    public AccommodationDTO createAccommodation(String name,
                                                UUID category_id,
                                                String description,
                                                BigDecimal price,
                                                boolean isReservable,
                                                MultipartFile image) throws IOException {
        Optional<Category> categoryOptional = categoryRepository.findById(category_id);
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        CheckSizeAndNotNullImage(image, imageExtension);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            Accommodation accommodation = new Accommodation();
            accommodation.setName(name);
            accommodation.setDescription(description);
            accommodation.setPrice(price);
            accommodation.setReservable(isReservable);
            accommodation.setImage(image.getBytes());

            accommodation.setCategory(category);
            Accommodation savedAccommodation = accommodationRepository.save(accommodation);

            return MappedAccommodationFrom(savedAccommodation);
        }

        throw new RuntimeException("Section not found with id: " + category_id);
    }

    @Transactional
    public AccommodationDTO updateAccommodation(UUID accommodation_id,
                                                UUID category_id,
                                                String name,
                                                String description,
                                                BigDecimal price,
                                                boolean isReservable,
                                                MultipartFile image) throws IOException {
        List<String> imageExtension = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");

        CheckSizeAndNotNullImage(image, imageExtension);

        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(accommodation_id);
        if (accommodationOptional.isPresent()) {
            Accommodation accommodationToUpdate = accommodationOptional.get();
            accommodationToUpdate.setName(name);
            accommodationToUpdate.setDescription(description);
            accommodationToUpdate.setPrice(price);
            if (nonNull(image)) {
                accommodationToUpdate.setImage(image.getBytes());
            }
            accommodationToUpdate.setReservable(isReservable);
            accommodationToUpdate.setCategory(categoryRepository.getReferenceById(category_id));
            Accommodation updatedAccommodation = accommodationRepository.save(accommodationToUpdate);

            return MappedAccommodationFrom(updatedAccommodation);
        } else {
            throw new RuntimeException("Accommodation not found with id: " + accommodation_id);
        }
    }

    @Transactional
    public byte[] getAccommodationImage(UUID accommodation_id) {
        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(accommodation_id);
        if (accommodationOptional.isPresent()) {
            return accommodationOptional.get().getImage();
        } else {
            throw new RuntimeException("Accommodation not found with id: " + accommodation_id);
        }
    }

    private static void CheckSizeAndNotNullImage(MultipartFile image, List<String> imageExtension) {
        if (nonNull(image) && (image.getSize() > 1_000_000 || !imageExtension.contains(image.getContentType()))) {
            throw new IllegalArgumentException("Invalid image file");
        }
    }

}