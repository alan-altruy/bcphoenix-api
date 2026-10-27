package com.bcphoenix.service;

import com.bcphoenix.dto.EventRequest;
import com.bcphoenix.model.Event;
import com.bcphoenix.model.Location;
import com.bcphoenix.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;

    public EventService (EventRepository eventRepository, LocationService locationService){
        this.eventRepository = eventRepository;
        this.locationService = locationService;
    }

    public List<Event> findAll(){
        return eventRepository.findAll();
    }
    public Event findById(Integer id){
        return eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event introuvable"));
    }
    public Event create(EventRequest request){
        Location location = locationService.findById(request.locationId());
        Event event = new Event();
        event.setEventDate(request.eventDate());
        event.setEventType(request.type());
        event.setNotes(request.notes());
        event.setLocation(location);
        return eventRepository.save(event);

    }
    public Event update(EventRequest request, Integer id){
        Event event=findById(id);
        Location location = locationService.findById(request.locationId());
        event.setLocation(location);
        event.setNotes(request.notes());
        event.setEventType(request.type());
        event.setEventDate(request.eventDate());
        return eventRepository.save(event);
    }
    public void delete(Integer id){
        Event event = findById(id);
        eventRepository.delete(event);
    }
}
