#ifndef Canoga_Computer_h
#define Canoga_Computer_h

#include "Player.h"

class Computer: public Player
{
public:
    Computer();
    virtual int chooseDice(Board Board); 
    virtual bool decideCover(Board Board, int roll);
    virtual vector<int> chooseSquares( Board Board, bool coverself, int roll );
    virtual bool canThrowOne(Board Board);
    bool canPlay(Board Board, int roll);
};

#endif
