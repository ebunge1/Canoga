#include "Turn.h"
#include "BoardView.h"
#include "UserInterface.h"
#include <vector>

Turn::Turn()
{
}

/*************************
 Function Name: startTurn
 Purpose: executes a turn
 Parameters:
            Board, the current state of the gameboard. passed by referance so it the squares can be updated when covered/uncovered
            string, the player whose turn it is
 Return Value: void
 Local Variables:
                roll, an int to hold the player's roll
 Algorithm:
        in loop
            decide how many dice to roll
            roll for move
            check if moves available
            decide to cover or uncover
            choose squares to play
            update board
        exit when game is won or no moves are available
            draw the board
 Assistance Recieved: None
 *************************/
void Turn::startTurn( Board &Board, string turn )
{
    UserInterface::declareTurn(turn);
    // inheritance and virtual functions used to condense code
    Player* Player;
    if ( turn == "Human" )
    {
        Human Human;
        Player = &Human;
    }
    else
    {
        Computer Computer;
        Player = &Computer;
    }
    int roll;
    do
    {
        // check if one die can be thrown
        if ( Player->canThrowOne(Board) )
        {
            // can throw one die, player decides how many to throw
            roll = Player->chooseDice(Board);
        }
        else
        {
            // cannot throw one die, throws two
            roll = UserInterface::rollResult( turn , Game::rollDie(), Game::rollDie());
        }
        // check for available moves
        if ( Player->canPlay(Board, roll) )
        {
            // boolean true if covering and false if uncovering
            bool coverself = Player->decideCover(Board, roll);
            // vector of squares that are being changed
            vector<int> squares = Player->chooseSquares( Board, coverself, roll );
            
            Board.updateBoard(squares, turn, coverself);
            BoardView::drawBoard(Board);
        }
        else
        {
            //no moves available, end turn
            break;
        }
    } while ( !Board.isGameWon() );
    UserInterface::noMoves();
    Player = nullptr;
}








