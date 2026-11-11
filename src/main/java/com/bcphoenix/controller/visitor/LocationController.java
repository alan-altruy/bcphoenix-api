package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.response.LocationResponse;
import com.bcphoenix.model.Location;
import com.bcphoenix.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationResponse> findAll() {
        List<Location> locations = locationService.findAll();
        return locations.stream().map(LocationResponse::from).toList();
    }

    @GetMapping("/{id}")
    public LocationResponse findById(@PathVariable Integer id){
        return LocationResponse.from(locationService.findById(id));
    }

}
