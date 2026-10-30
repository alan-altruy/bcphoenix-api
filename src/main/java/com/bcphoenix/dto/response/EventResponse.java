package com.bcphoenix.dto.response;

import com.bcphoenix.model.Event;
import com.bcphoenix.model.EventType;

import java.time.LocalDate;

public record EventResponse(
        Integer id,
        LocalDate eventDate,
        EventType eventType,
        LocationResponse location,
        String notes
        ) {

        public static EventResponse from(Event event){
                return new EventResponse(
                        event.getId(),
                        event.getEventDate(),
                        event.getEventType(),
                        LocationResponse.from(event.getLocation()),
                        event.getNotes());
        }
}
