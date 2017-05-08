#ifndef Canoga_Game_h
#define Canoga_Game_h

#include "Tournament.h"
#include "Board.h"
#include <string>
#include <vector>
using namespace std;

class Game
{
public:
    Game();
    Winner runGame(Handicap* Handicap, Tournament Tourn, string filename);
    string getFirstMove() const;
    string getCurrentTurn() const;
    void setFirstMove( string x );
    void setCurrentTurn( string x );
    void rollForFirst();
    static int rollDie();
    void saveGame(Board Board, Tournament Tourn);
    void switchTurn(Board &Board);
    int calcScore(Board Board);
    Board copyFromFile(string filename);
    
private:
    string firstmove;
    string currentturn;
    string decideFirst(int humanroll, int comroll);
    int addsquares(vector<bool> board, int size, bool covered);
};

#endif
