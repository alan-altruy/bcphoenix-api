package com.bcphoenix.controller;

import com.bcphoenix.dto.LocationRequest;
import com.bcphoenix.model.Location;
import com.bcphoenix.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<Location> findAll() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public Location findById(@PathVariable Integer id){
        return locationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Location create(@Valid @RequestBody LocationRequest request){
        return locationService.create(request);
    }

    @PutMapping("/{id}")
    public Location update(@Valid @RequestBody LocationRequest request, @PathVariable Integer id){
        return locationService.update(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        locationService.delete(id);
    }
}
