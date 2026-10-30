package com.bcphoenix.dto.response.admin;

import com.bcphoenix.model.Affiliate;

import java.time.LocalDate;

public record AdminAffiliateResponse(Integer id, String firstName, String lastName,
                                     String licenseNumber, LocalDate birthDate, boolean active,
                                     Integer userId) {
    public static AdminAffiliateResponse from(Affiliate affiliate) {
        return new AdminAffiliateResponse(
                affiliate.getId(),
                affiliate.getFirstName(),
                affiliate.getLastName(),
                affiliate.getLicenseNumber(),
                affiliate.getBirthDate(),
                affiliate.isActive(),
                affiliate.getUser() == null ? null : affiliate.getUser().getId());
    }
}
