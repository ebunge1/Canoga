#ifndef Canoga_Tournament_h
#define Canoga_Tournament_h

#include <string>
#include <queue>
#include "Utility.h"

class Tournament
{
public:
    Tournament();
    void playTournament(string filename);
    int getHumanScore() const;
    int getComScore() const;
    void updateScore(Winner Winner); 
    Handicap calcHandicap(Winner Winner);
    string final(int humanscore, int comscore);
    
private:
    int humanscore;
    int comscore;
    void copyFromFile(string filename);
};

#endif