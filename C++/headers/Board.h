#ifndef Canoga_Board_h
#define Canoga_Board_h

#include "Utility.h"
#include <vector>

class Board
{
public:
    Board();
    Board( unsigned int size );
    Board( unsigned int size, Handicap* Handicap );
    const vector<bool> getHumanBoard() const;
    const vector<bool> getComBoard() const;
    void setSquare( vector<bool> &side, int square, bool covered );
    static bool testSquare( int square, const vector<bool> side, bool test );
    int getBoardSize() const;
    void updateBoard(const vector<int> squares, string player, bool coverself);
    bool firstTurnMade();
    void setTurnFlag();
    bool isGameWon();
   
private:
    unsigned int boardsize;
    vector<bool> humansquares;
    vector<bool> comsquares;
    bool turnflag;
};


#endif
