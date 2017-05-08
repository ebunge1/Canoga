#ifndef Canoga_Utility_h
#define Canoga_Utility_h

#include <string>
using namespace std;

struct Handicap
{
public:
    string player;
    int square;
    Handicap();
    Handicap(string player, int square);
};


struct Winner
{
public:
    string player;
    string firstmove;
    int points;
    Winner();
    Winner(string player, string firstmove, int points);
};

#endif
