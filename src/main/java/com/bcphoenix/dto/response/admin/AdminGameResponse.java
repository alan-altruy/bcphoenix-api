package com.bcphoenix.dto.response.admin;

import com.bcphoenix.model.Game;

public record AdminGameResponse(
        Integer id,
        Integer score,
        Integer gameNumber,
        Integer eventId,
        AdminAffiliateResponse affiliate) {
    public static AdminGameResponse from(Game game){
        return new AdminGameResponse(
                game.getId(),
                game.getScore(),
                game.getGameNumber(),
                game.getEvent().getId(),
                AdminAffiliateResponse.from(game.getAffiliate()));
    }
}
