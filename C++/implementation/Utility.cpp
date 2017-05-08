#include "Utility.h"

/*************************
 Function Name: Handicap
 Purpose: initialize variables for handicap, a structure to keep track of parameters needed to calculate the handicap
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Handicap::Handicap()
{
    player = "";
    square = 0;
}

/*************************
 Function Name: Handicap
 Purpose: custom constructor
 Parameters:
            player, string for the player who gets the handicap
            square, int decribing which square is handicapped
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Handicap::Handicap(string player, int square)
{
    this->player = player;
    this->square = square;
}

/*************************
 Function Name: Winner
 Purpose: intitialize variables for winner, a structure to keep track of the winner of a game
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Winner::Winner()
{
    player = "";
    firstmove = "";
    points = 0;
}

/*************************
 Function Name: Winner
 Purpose: custom constructor
 Parameters:
            player, a string for the winning player of the game
            firstmove, string to show which player went first
            points, int to provide the score the winner recieved
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Winner::Winner(string player, string firstmove, int points)
{
    this->player = player;
    this->firstmove = firstmove;
    this->points = points;
}
