package com.mygdx.game.Managers;



import java.io.Serializable;

public class GameData implements Serializable {

    private static final long serialVersionUID = 201809111959L;

    private final int MAX_SCORES = 10;

    private long tentativeScore = 50;

    public long getTentativeScore() {
        return tentativeScore;
    }

    public void setTentativeScore(long l) {
        this.tentativeScore = l;
    }
}
