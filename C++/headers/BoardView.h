#ifndef Canoga_BoardView_h
#define Canoga_BoardView_h

#include "Board.h"

class BoardView
{
public:
    BoardView();
    static void drawBoard( Board Board );
    static void displayScore(int humanscore, int comscore);
};

#endif
