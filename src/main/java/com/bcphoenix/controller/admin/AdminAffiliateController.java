package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.AffiliateRequest;
import com.bcphoenix.dto.response.admin.AdminAffiliateResponse;
import com.bcphoenix.dto.response.visitor.AffiliateResponse;
import com.bcphoenix.model.Affiliate;
import com.bcphoenix.service.AffiliateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/affiliates")
public class AdminAffiliateController {
    private final AffiliateService affiliateService;

    public AdminAffiliateController(AffiliateService affiliateService){
        this.affiliateService=affiliateService;
    }

    @GetMapping
    public List<AdminAffiliateResponse> findAll(){
        List<Affiliate> affiliates = affiliateService.findAll();
        return affiliates.stream().map(AdminAffiliateResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AdminAffiliateResponse findById(@PathVariable Integer id){
        return AdminAffiliateResponse.from(affiliateService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminAffiliateResponse create(@Valid @RequestBody AffiliateRequest request){
        return AdminAffiliateResponse.from(affiliateService.create(request));
    }
    @PutMapping("/{id}")
    public AdminAffiliateResponse update(@Valid @RequestBody AffiliateRequest request, @PathVariable Integer id){
        return AdminAffiliateResponse.from(affiliateService.update(id,request));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        affiliateService.delete(id);
    }
}
