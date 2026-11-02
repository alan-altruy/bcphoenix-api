package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.response.visitor.EventScoresResponse;
import com.bcphoenix.dto.response.visitor.GameResponse;
import com.bcphoenix.model.EventType;
import com.bcphoenix.model.Game;
import com.bcphoenix.service.GameService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameResponse> findAll(){
        List<Game> games = gameService.findAll();
        return games.stream().map(GameResponse::from).toList();
    }
    @GetMapping("/{id}")
    public GameResponse findById(@PathVariable Integer id){
        return GameResponse.from(gameService.findById(id));
    }

    @GetMapping("/latest")
    public EventScoresResponse findLastGamesByEventType(@RequestParam EventType eventType){
        return EventScoresResponse.from(gameService.findLastGamesByEventType(eventType));
    }

    @GetMapping("/event/{eventId}")
    public EventScoresResponse findByEventId(@PathVariable Integer eventId){
        return EventScoresResponse.from(gameService.findByEventId(eventId));
    }
    @GetMapping("/search")
    public EventScoresResponse findByEventTypeAndDate(
            @RequestParam EventType eventType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return EventScoresResponse.from(gameService.findByEventTypeAndDate(eventType, date));
    }

}
