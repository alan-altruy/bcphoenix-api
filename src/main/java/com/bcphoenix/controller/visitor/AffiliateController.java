package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.request.AffiliateRequest;
import com.bcphoenix.dto.response.visitor.AffiliateResponse;
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
    public List<AffiliateResponse> findAll(){
        List<Affiliate> affiliates = affiliateService.findAll();
        return affiliates.stream().map(AffiliateResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AffiliateResponse findById(@PathVariable Integer id){
        return AffiliateResponse.from(affiliateService.findById(id));
    }

}
