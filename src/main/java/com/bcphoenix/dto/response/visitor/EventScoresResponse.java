package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.dto.response.EventResponse;
import com.bcphoenix.model.Game;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public record EventScoresResponse(EventResponse eventResponse, List<AffiliateGamesResponse> affiliates) {
    public static EventScoresResponse from(List<Game> games){
        if(games.isEmpty()){
            return new EventScoresResponse(null, List.of());
        }
        List<AffiliateGamesResponse> affiliates = games.stream()
                .collect(Collectors.groupingBy(game -> game.getAffiliate().getId(), LinkedHashMap::new, Collectors.toList()))
                .values().stream().map(AffiliateGamesResponse::from).toList();
        return new EventScoresResponse(EventResponse.from(games.getFirst().getEvent()), affiliates);
    }
}
