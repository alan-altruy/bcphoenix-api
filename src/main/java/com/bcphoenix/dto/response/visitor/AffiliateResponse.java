package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.model.Affiliate;

public record AffiliateResponse(Integer id, String firstName, String lastName) {
    public static AffiliateResponse from(Affiliate affiliate) {
        return new AffiliateResponse(
                affiliate.getId(), affiliate.getFirstName(), affiliate.getLastName());
    }
}
