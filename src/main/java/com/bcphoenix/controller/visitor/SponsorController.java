
package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.response.SponsorResponse;
import com.bcphoenix.service.SponsorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {

    private final SponsorService sponsorService;

    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @GetMapping
    public List<SponsorResponse> findAll() {
        return sponsorService.findAll()
                .stream()
                .map(SponsorResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public SponsorResponse findById(@PathVariable Integer id) {
        return SponsorResponse.from(sponsorService.findById(id));
    }
}
