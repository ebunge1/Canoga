package com.evanbunge.canoga;

public abstract class Player {
    Player() {
    }

    public abstract int chooseDice(BoardModel Board);

    public abstract boolean decideCover(BoardModel Board, int roll);

    public abstract int[] chooseSquares( BoardModel Board, boolean coverself, int roll );

    public abstract boolean canThrowOne(BoardModel Board);

    public abstract boolean canPlay(BoardModel Board, int roll);

}
