package com.bcphoenix.service;

import com.bcphoenix.dto.LocationRequest;
import com.bcphoenix.model.Location;
import com.bcphoenix.repository.EventRepository;
import com.bcphoenix.repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository, EventRepository eventRepository){
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
    }

    public List<Location> findAll(){
        return locationRepository.findAll();
    }
    public Location findById(Integer id){
        return locationRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location introuvable"));
    }
    public Location create(LocationRequest request){
        String name= request.name().trim();
        if(locationRepository.existsByNameIgnoreCase(name)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"La localisation: "+name+" existe déjà");
        }
        Location location=new Location();
        location.setName(request.name());
        location.setAddress(request.address());
        location.setCity(request.city());
        return locationRepository.save(location);
    }
    public Location update(Integer id, LocationRequest request){
        Location location=findById(id);
        String name= request.name().trim();
        if(locationRepository.existsByNameIgnoreCaseAndIdNot(name,id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"La localisation: "+name+" existe déjà");
        }


        location.setName(request.name());
        location.setAddress(request.address());
        location.setCity(request.city());
        return locationRepository.save(location);
    }
    public void delete(Integer id){
        Location location=findById(id);
        if(eventRepository.existsByLocationId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ce lieu est utilisé par un évènement");
        }
        locationRepository.delete(location);
    }
}
