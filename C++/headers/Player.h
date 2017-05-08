#ifndef Canoga_Player_h
#define Canoga_Player_h

#include "Board.h"
#include <vector>

class Player
{
public:
    Player();
    virtual ~Player();
    virtual bool canPlay(Board Board, int roll) = 0;
    virtual bool canThrowOne(Board Board) = 0;
    virtual int chooseDice(Board Board) = 0;
    virtual bool decideCover(Board Board, int roll) = 0;
    virtual vector<int> chooseSquares( Board Board , bool coverself, int roll ) = 0;

protected:
    bool checkformove(vector<bool> side, int roll, bool test);
};

#endif
