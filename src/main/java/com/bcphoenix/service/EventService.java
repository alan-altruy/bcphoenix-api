package com.bcphoenix.service;

import com.bcphoenix.dto.request.EventRequest;
import com.bcphoenix.model.Event;
import com.bcphoenix.model.Location;
import com.bcphoenix.repository.EventRepository;
import com.bcphoenix.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final GameRepository gameRepository;

    public EventService (EventRepository eventRepository, LocationService locationService, GameRepository gameRepository){
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.gameRepository = gameRepository;
    }

    public List<Event> findAll(){
        return eventRepository.findAll();
    }
    public Event findById(Integer id){
        return eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event introuvable"));
    }

    public Event findLastEvent(){
        return eventRepository.findAll().stream().max((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun évènement trouvé"));
    }

    public Event create(EventRequest request){
        Location location = locationService.findById(request.locationId());
        Event event = new Event();
        event.setEventDate(request.eventDate());
        event.setEventType(request.eventType());
        event.setNotes(request.notes());
        event.setLocation(location);
        return eventRepository.save(event);

    }
    public Event update(EventRequest request, Integer id){
        Event event=findById(id);
        Location location = locationService.findById(request.locationId());
        event.setLocation(location);
        event.setNotes(request.notes());
        event.setEventType(request.eventType());
        event.setEventDate(request.eventDate());
        return eventRepository.save(event);
    }
    public void delete(Integer id){

        Event event = findById(id);
        if(gameRepository.existsByEventId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Il y a des parties attribuées à cet évènement");
        }
        eventRepository.delete(event);
    }
}
