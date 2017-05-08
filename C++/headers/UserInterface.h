#ifndef Canoga_UserInterface_h
#define Canoga_UserInterface_h

#include <iostream>
#include <string>
#include <vector>
#include "Board.h"
using namespace std;

class UserInterface
{
public:
    UserInterface();
    static int askSize();
    static void playersRoll();
    static int rollResult(string player, int firstroll, int secondroll);
    static int rollResult(string player, int roll);
    static void declareGameWon( string winner, int points );
    static void declareTournWon( string winner );
    static int howManyDice();
    static bool askToSave();
    static bool willCover( bool can_cover, bool can_uncover );
    static vector<int> pickSquares(vector<bool> board, bool available );
    static void noMoves();
    static bool newGame(); 
    static bool validatesquares(vector<int> side, int roll);
    static void declareTurn(string player);
    static void computermove(vector<int> squares);
    static string newOrLoad();
    static bool serializeDice();
};


#endif
