package com.bcphoenix.service;

import com.bcphoenix.dto.request.GameRequest;
import com.bcphoenix.model.Affiliate;
import com.bcphoenix.model.Event;
import com.bcphoenix.model.Game;
import com.bcphoenix.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final AffiliateService affiliateService;
    private final EventService eventService;

    public GameService(GameRepository gameRepository, AffiliateService affiliateService, EventService eventService){
        this.gameRepository=gameRepository;
        this.affiliateService=affiliateService;
        this.eventService=eventService;
    }
    public List<Game> findAll(){
        return gameRepository.findAll();
    }
    public Game findById(Integer id){
        return gameRepository.findById(id).orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Game introuvable"));
    }
    public Game create(GameRequest request){
        if(gameRepository.existsByAffiliateIdAndEventIdAndGameNumber(request.affiliateId(), request.eventId(), request.gameNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La partie a déjà été enregistrée");
        }
        Affiliate affiliate = affiliateService.findById(request.affiliateId());
        if (!affiliate.isActive()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"L'affilié choisi est inactif");
        }
        Event event = eventService.findById(request.eventId());

        Game game = new Game();
        game.setAffiliate(affiliate);
        game.setCreatedAt(LocalDateTime.now());
        game.setEvent(event);
        game.setGameNumber(request.gameNumber());
        game.setScore(request.score());
        return gameRepository.save(game);
    }
    public Game update(GameRequest request, Integer id){
        Game game = findById(id);
        if(gameRepository.existsByAffiliateIdAndEventIdAndGameNumberAndIdNot(request.affiliateId(), request.eventId(), request.gameNumber(),id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La partie a déjà été enregistrée");
        }

        Affiliate affiliate = affiliateService.findById(request.affiliateId());
        Event event = eventService.findById(request.eventId());

        game.setScore(request.score());
        game.setGameNumber(request.gameNumber());
        game.setEvent(event);
        game.setAffiliate(affiliate);
        return gameRepository.save(game);
    }
    public void delete(Integer id){
        Game game = findById(id);
        gameRepository.delete(game);
    }
}
