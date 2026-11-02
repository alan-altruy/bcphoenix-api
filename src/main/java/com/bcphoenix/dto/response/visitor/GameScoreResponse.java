package com.bcphoenix.dto.response.visitor;

import com.bcphoenix.model.Game;

public record GameScoreResponse(Integer id, Integer gameNumber, Integer score) {

    public static GameScoreResponse from(Game game){
        return new GameScoreResponse(game.getId(), game.getGameNumber(),  game.getScore() );
    }
}
