#ifndef Canoga_Human_h
#define Canoga_Human_h

#include "Player.h"

class Human: public Player
{
public:
    Human();
    virtual int chooseDice(Board Board);
    virtual bool decideCover(Board Board, int roll);
    virtual vector<int> chooseSquares( Board Board, bool coverself, int roll );
    virtual bool canThrowOne(Board Board);
    virtual bool canPlay(Board Board, int roll);
};

#endif
