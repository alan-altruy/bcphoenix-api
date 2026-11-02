package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.model.Game;

import java.util.List;

public record AffiliateGamesResponse(AffiliateResponse affiliate, List<GameScoreResponse> games) {

    public static AffiliateGamesResponse from(List<Game> games){
        return new AffiliateGamesResponse(AffiliateResponse.from(games.getFirst().getAffiliate()),games.stream().map(GameScoreResponse::from).toList());
    }
}
