#include "Human.h"
#include "Game.h"
#include "UserInterface.h"
using namespace std;

/*************************
 Function Name: Human
 Purpose: constructor
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Human::Human()
{
}

/*************************
 Function Name: chooseDice
 Purpose: get number of dice to be thrown and return the roll made
 Parameters: 
            Board, the board being played
 Return Value:
            int containing the roll made by the player
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
int Human::chooseDice(Board Board)
{
    //ask user for number of dice
    if ( UserInterface::howManyDice() == 2 )
    {
        return UserInterface::rollResult( "Human" , Game::rollDie() , Game::rollDie() );
    }
    else
    {
        return UserInterface::rollResult( "Human" , Game::rollDie() );
    }
}

/*************************
 Function Name: decideCover
 Purpose: get whether to cover own squares or uncover opponent's
 Parameters:
            Board, the board being played
            roll, the roll being played
 Return Value:
            boolean, true for cover and false for uncover
 Local Variables:
            can_cover, boolean value true if a legal move is available to cover
            can_uncover, boolean value true if a legal move is available to uncover
 Algorithm:
            check for moves to cover and uncover
            prevent move to uncover if this is the first turn
            get the user's decision to cover or uncover
 Assistance Recieved: None
 *************************/
bool Human::decideCover(Board Board, int roll)
{
    bool can_cover = checkformove(Board.getHumanBoard(), roll, false);
    bool can_uncover = false;
    // check if this is the first turn
    if ( Board.firstTurnMade() )
    {
        can_uncover = checkformove(Board.getComBoard(), roll, true);
    }
    //prompt user whether to cover or uncover
    return UserInterface::willCover(can_cover, can_uncover);
}

/*************************
 Function Name: chooseSquares
 Purpose: pick which squares to play
 Parameters:
            Board, the board being played
            coverself, bool to determine which side to play on / whether to cover or uncover
            roll, int containing the roll made
 Return Value: 
            vector<int> containing the squares to play
 Local Variables:
            side, a temporary vector of boolean values to represent the side of the board being played on
            arr, a temporary vector of ints to hold the squares to be played
 Algorithm:
            determine the side being played
            pick the squares and validate if the move is legal
 Assistance Recieved: None
 *************************/
vector<int> Human::chooseSquares( Board Board, bool coverself, int roll )
{
    vector<bool> side;
    //determine the side being play
    if (coverself)
    {
        side = Board.getHumanBoard();
    }
    else
    {
        side = Board.getComBoard();
    }
    //pick and validate squares
    do
    {
        vector<int> arr = UserInterface::pickSquares( side , coverself );
        if ( UserInterface::validatesquares( arr, roll ) )
        {
            return arr;
        }
    }
    while ( 1 );
}

/*************************
 Function Name: canThrowOne
 Purpose: check if one die can be thrown (if own board 7+ is covered)
 Parameters:
            Board, the board being played
 Return Value:
            boolean, true if one die can be thrown and false if not
 Local Variables:
            covered, boolean to flag if a square is covered or not
 Algorithm:
            set flag to true (covered)
            check each square 7 and above
            set flag false if any square is uncovered
 Assistance Recieved: None
 *************************/
bool Human::canThrowOne(Board Board)
{
    bool covered = true;
    //start at 6 because testsquare checks index not square number
    for (int i = 6; i < Board.getBoardSize(); i++)
    {
        if ( Board.testSquare( i, Board.getHumanBoard(), false ) )
        {
            covered = false;
        }
    }
    return covered;
}

/*************************
 Function Name: canPlay
 Purpose: determine if any move is available
 Parameters:
            Board, the board being played
            roll, int containing roll made
 Return Value:
            bool, true if a move is available, false if not
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
bool Human::canPlay(Board Board, int roll)
{
    if ( checkformove(Board.getHumanBoard(), roll, false) || checkformove(Board.getComBoard(), roll, true) )
    {
        return true;
    }
    return false;
}






