package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.EventRequest;
import com.bcphoenix.dto.response.EventResponse;
import com.bcphoenix.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {
    private final EventService eventService;

    public AdminEventController(EventService eventService){
        this.eventService = eventService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@Valid @RequestBody EventRequest eventRequest){
        return EventResponse.from(eventService.create(eventRequest));
    }
    @PutMapping("/{id}")
    public EventResponse update (@Valid @RequestBody EventRequest eventRequest, @PathVariable Integer id){
        return EventResponse.from(eventService.update(eventRequest,id));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id){
        eventService.delete(id);
    }
}
