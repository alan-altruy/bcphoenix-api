package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.response.EventResponse;
import com.bcphoenix.model.Event;
import com.bcphoenix.service.EventService;
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
    public List<EventResponse> findAll(){
        List<Event> events = eventService.findAll();
        return events.stream().map(EventResponse::from).toList();
    }
    @GetMapping("/{id}")
    public EventResponse findById(@PathVariable Integer id){
        return EventResponse.from(eventService.findById(id));
    }

}
