
package com.bcphoenix.dto.response;

import com.bcphoenix.model.Sponsor;

public record SponsorResponse(
        Integer id,
        String name,
        String city,
        String website,
        Integer imageId
) {
    public static SponsorResponse from(Sponsor sponsor) {
        return new SponsorResponse(
                sponsor.getId(),
                sponsor.getName(),
                sponsor.getCity(),
                sponsor.getWebsite(),
                sponsor.getImage() != null
                        ? sponsor.getImage().getId()
                        : null
        );
    }
}
