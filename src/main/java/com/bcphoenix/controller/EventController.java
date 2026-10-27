package com.bcphoenix.controller;

import com.bcphoenix.dto.EventRequest;
import com.bcphoenix.model.Event;
import com.bcphoenix.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController (EventService eventService){
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> findAll(){
        return eventService.findAll();
    }
    @GetMapping("/{id}")
    public Event findById(@PathVariable Integer id){
        return eventService.findById(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody EventRequest eventRequest){
        return eventService.create(eventRequest);
    }
    @PutMapping("/{id}")
    public Event update (@Valid @RequestBody EventRequest eventRequest, @PathVariable Integer id){
        return eventService.update(eventRequest,id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id){
        eventService.delete(id);
    }
}
