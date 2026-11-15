package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.SponsorRequest;
import com.bcphoenix.dto.response.SponsorResponse;
import com.bcphoenix.service.SponsorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/sponsors")
public class AdminSponsorController {

    private final SponsorService sponsorService;

    public AdminSponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SponsorResponse create(@Valid @RequestBody SponsorRequest request) {
        return SponsorResponse.from(sponsorService.create(request));
    }

    @PutMapping("/{id}")
    public SponsorResponse update(
            @Valid @RequestBody SponsorRequest request,
            @PathVariable Integer id
    ) {
        return SponsorResponse.from(sponsorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        sponsorService.delete(id);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadImage(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file
    ) {
        sponsorService.uploadImage(id, file);
    }

    @DeleteMapping("/{id}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Integer id) {
        sponsorService.deleteImage(id);
    }
}