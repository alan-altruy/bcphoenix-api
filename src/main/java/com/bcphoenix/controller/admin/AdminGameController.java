package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.GameRequest;
import com.bcphoenix.dto.response.admin.AdminGameResponse;
import com.bcphoenix.model.Game;
import com.bcphoenix.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/games")
public class AdminGameController {
    private final GameService gameService;

    public AdminGameController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping
    public List<AdminGameResponse> findAll(){
        List<Game> games = gameService.findAll();
        return games.stream().map(AdminGameResponse::from).toList();
    }
    @GetMapping("/{id}")
    public AdminGameResponse findById(@PathVariable Integer id){
        return AdminGameResponse.from(gameService.findById(id));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminGameResponse create(@Valid @RequestBody GameRequest request){
        return AdminGameResponse.from(gameService.create(request));
    }
    @PutMapping("/{id}")
    public AdminGameResponse update (@Valid @RequestBody GameRequest request, @PathVariable Integer id){
        return AdminGameResponse.from(gameService.update(request, id));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        gameService.delete(id);
    }

}
