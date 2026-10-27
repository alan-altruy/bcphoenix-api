package com.bcphoenix.controller;

import com.bcphoenix.dto.AffiliateRequest;
import com.bcphoenix.model.Affiliate;
import com.bcphoenix.service.AffiliateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/affiliates")
public class AffiliateController {
    private final AffiliateService affiliateService;

    public AffiliateController(AffiliateService affiliateService){
        this.affiliateService=affiliateService;
    }

    @GetMapping
    public List<Affiliate> findAll(){
        return affiliateService.findAll();
    }

    @GetMapping("/{id}")
    public Affiliate findById(@PathVariable Integer id){
        return affiliateService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Affiliate create(@Valid @RequestBody AffiliateRequest request){
        return affiliateService.create(request);
    }
    @PutMapping("/{id}")
    public Affiliate update(@Valid @RequestBody AffiliateRequest request, @PathVariable Integer id){
        return affiliateService.update(id,request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        affiliateService.delete(id);
    }
}
