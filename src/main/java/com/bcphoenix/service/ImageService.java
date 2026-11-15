
package com.bcphoenix.service;

import com.bcphoenix.model.Image;
import com.bcphoenix.repository.ImageRepository;
import com.bcphoenix.repository.SponsorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/png", "image/png",
            "image/jpeg", "image/jpeg",
            "image/webp", "image/webp"
    );

    private final ImageRepository imageRepository;
    private final SponsorRepository sponsorRepository;

    public ImageService(
            ImageRepository imageRepository,
            SponsorRepository sponsorRepository
    ) {
        this.imageRepository = imageRepository;
        this.sponsorRepository = sponsorRepository;
    }

    @Transactional
    public Image create(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Le fichier est vide"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Image trop volumineuse (10 Mo maximum)"
            );
        }

        try {
            byte[] data = file.getBytes();
            String contentType = detectContentType(data);

            if (contentType == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Format accepté : PNG, JPEG ou WebP"
                );
            }

            Image image = new Image();
            image.setImageData(data);
            image.setContentType(contentType);

            return imageRepository.save(image);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible de lire le fichier"
            );
        }
    }

    @Transactional
    public void deleteIfUnused(Integer imageId) {
        if (!imageRepository.existsById(imageId)) {
            return;
        }

        // Ajouter ici les vérifications des autres entités
        // qui utiliseront des images.
        if (sponsorRepository.existsByImage_Id(imageId)) {
            return;
        }

        imageRepository.deleteById(imageId);
    }

    private String detectContentType(byte[] data) {
        if (data.length >= 8
                && data[0] == (byte) 0x89
                && data[1] == 0x50
                && data[2] == 0x4E
                && data[3] == 0x47) {
            return "image/png";
        }

        if (data.length >= 3
                && data[0] == (byte) 0xFF
                && data[1] == (byte) 0xD8
                && data[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        if (data.length >= 12
                && data[0] == 'R'
                && data[1] == 'I'
                && data[2] == 'F'
                && data[3] == 'F'
                && data[8] == 'W'
                && data[9] == 'E'
                && data[10] == 'B'
                && data[11] == 'P') {
            return "image/webp";
        }

        return null;
    }
}
