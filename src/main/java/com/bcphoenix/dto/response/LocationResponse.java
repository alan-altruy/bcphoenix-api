package com.bcphoenix.dto.response;

import com.bcphoenix.model.Location;

public record LocationResponse(
        Integer id,
        String name,
        String city,
        String address,
        String imageUrl,
        Integer imageId,
        String websiteUrl
) {
    public static LocationResponse from(Location location){
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getCity(),
                location.getAddress(),
                location.getImageUrl(),
                location.getImageId(),
                location.getWebsiteUrl()
        );
    }
}
