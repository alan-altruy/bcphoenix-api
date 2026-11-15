
package com.bcphoenix.service;

import com.bcphoenix.dto.request.SponsorRequest;
import com.bcphoenix.model.Sponsor;
import com.bcphoenix.model.Image;
import com.bcphoenix.repository.SponsorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final ImageService imageService;

    public SponsorService(
            SponsorRepository sponsorRepository,
            ImageService imageService
    ) {
        this.sponsorRepository = sponsorRepository;
        this.imageService = imageService;
    }

    public List<Sponsor> findAll() {
        return sponsorRepository.findAll();
    }

    public Sponsor findById(Integer id) {
        return sponsorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sponsor introuvable"
                ));
    }

    public Sponsor create(SponsorRequest request) {
        String name = request.name().trim();

        if (sponsorRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le parrain : " + name + " existe déjà"
            );
        }

        Sponsor sponsor = new Sponsor();
        sponsor.setName(name);
        sponsor.setCity(request.city());
        sponsor.setWebsite(request.website());

        return sponsorRepository.save(sponsor);
    }

    public Sponsor update(Integer id, SponsorRequest request) {
        Sponsor sponsor = findById(id);
        String name = request.name().trim();

        if (sponsorRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le parrain : " + name + " existe déjà"
            );
        }

        sponsor.setName(name);
        sponsor.setCity(request.city());
        sponsor.setWebsite(request.website());

        return sponsorRepository.save(sponsor);
    }

    @Transactional
    public void uploadImage(Integer sponsorId, MultipartFile file) {
        Sponsor sponsor = findById(sponsorId);

        Image newImage = imageService.create(file);

        Image oldImage = sponsor.getImage();
        sponsor.setImage(newImage);

        sponsorRepository.save(sponsor);

        // L'ancienne image n'est supprimée que si elle n'est plus utilisée.
        if (oldImage != null) {
            imageService.deleteIfUnused(oldImage.getId());
        }
    }

    @Transactional
    public void deleteImage(Integer sponsorId) {
        Sponsor sponsor = findById(sponsorId);

        Image image = sponsor.getImage();

        if (image == null) {
            return;
        }

        sponsor.setImage(null);
        sponsorRepository.save(sponsor);

        imageService.deleteIfUnused(image.getId());
    }

    public void delete(Integer id) {
        Sponsor sponsor = findById(id);
        sponsorRepository.delete(sponsor);
    }
}
