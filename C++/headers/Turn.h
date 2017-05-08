#ifndef Canoga_Turn_h
#define Canoga_Turn_h

#include "Game.h"
#include "Board.h"
#include "Human.h"
#include "Computer.h"
#include <string>
using namespace std;

class Turn: public Game
{
public:
    Turn();
    static void startTurn( Board &Board, string turn );
    
};


#endif
