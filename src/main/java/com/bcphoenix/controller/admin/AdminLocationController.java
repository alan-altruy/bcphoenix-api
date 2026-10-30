package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.LocationRequest;
import com.bcphoenix.dto.response.LocationResponse;
import com.bcphoenix.model.Location;
import com.bcphoenix.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/locations")
public class AdminLocationController {
    private final LocationService locationService;

    public AdminLocationController(LocationService locationService) {
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse create(@Valid @RequestBody LocationRequest request){
        return LocationResponse.from(locationService.create(request));
    }

    @PutMapping("/{id}")
    public LocationResponse update(@Valid @RequestBody LocationRequest request, @PathVariable Integer id){
        return LocationResponse.from(locationService.update(id,request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        locationService.delete(id);
    }
}
