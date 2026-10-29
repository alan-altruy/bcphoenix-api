package com.bcphoenix.controller;

import com.bcphoenix.dto.GameRequest;
import com.bcphoenix.model.Game;
import com.bcphoenix.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> findAll(){
        return gameService.findAll();
    }
    @GetMapping("/{id}")
    public Game findById(@PathVariable Integer id){
        return gameService.findById(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@Valid @RequestBody GameRequest request){
        return gameService.create(request);
    }
    @PutMapping("/{id}")
    public Game update (@Valid @RequestBody GameRequest request, @PathVariable Integer id){
        return gameService.update(request, id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        gameService.delete(id);
    }

}
