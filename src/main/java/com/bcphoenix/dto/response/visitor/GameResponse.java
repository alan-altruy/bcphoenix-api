package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.model.Game;

public record GameResponse(
        Integer id,
        Integer score,
        Integer gameNumber,
        Integer eventId,
        AffiliateResponse affiliate) {
    public static GameResponse from(Game game){
        return new GameResponse(
                game.getId(),
                game.getScore(),
                game.getGameNumber(),
                game.getEvent().getId(),
                AffiliateResponse.from(game.getAffiliate()));
    }
}
