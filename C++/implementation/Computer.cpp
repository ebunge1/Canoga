#include "Computer.h"
#include "UserInterface.h"
#include "Game.h"
#include <iostream>

/*************************
 Function Name: Computer
 Purpose: constructor
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Computer::Computer()
{
}

/*************************
 Function Name: chooseDice
 Purpose: choose number of dice to roll and return that roll
 Parameters:
            Board, the board being played
 Return Value:
            int, the roll made
 Local Variables:
            covered, boolean to flag if a square is covered or not
 Algorithm:
            check if opponent's squares 7+ are covered
            roll one if they are, two if they're not
 Assistance Recieved: None
 *************************/
 int Computer::chooseDice(Board Board)
{
    bool covered = true;
    //start at 6 because test square checks index not square number
    for (int i = 6; i < Board.getBoardSize(); i++)
    {
        if ( Board.testSquare( i, Board.getHumanBoard(), false ) )
        {
            covered = false;
        }
    }
    if ( covered )
    {
        return UserInterface::rollResult( "Computer" , Game::rollDie() );
    }
    return UserInterface::rollResult( "Computer" , Game::rollDie() , Game::rollDie() );
}

/*************************
 Function Name: decideCover
 Purpose: decide to cover own side or uncover opponents
 Parameters:
            Board, the board being played
            roll, int containing the roll made
 Return Value:
            bool, true if covering and false if uncovering
 Local Variables: None
 Algorithm:
            cover if moves available
            if no move to cover and the first turn was made, uncover
 Assistance Recieved: None
 *************************/
bool Computer::decideCover(Board Board, int roll)
{
    
    if ( checkformove( Board.getComBoard(), roll, false ) ) {
        cout << "Computer chooses to cover.\n";
        return true;
    }
    if ( Board.firstTurnMade() )
    {
        cout << "Computer chooses to uncover.\n";
        return false;
    }
    return true;
}

/*************************
 Function Name: chooseSquares
 Purpose: choose which squares to play
 Parameters:
            Board, the board being played
            coverself, bool determining whether covering own side or uncovering opponent's
            roll, int containing the roll made
 Return Value:
            vector<int> containing the squares to play
 Local Variables:
            a, b, c, d
            used to represent different squares that could be chosen in a move
 Algorithm:
            decide which side to test
            check for highest square possible to cover plus squares that would add up to the roll
            uses same algorithm as Player::checkformove
 Assistance Recieved: None
 *************************/
vector<int> Computer::chooseSquares( Board Board, bool coverself, int roll )
{
    vector<int> squares(4,0);
    vector<bool> side;
    bool test;
    //decide which square to test
    if (coverself)
    {
        side = Board.getComBoard();
        test = false;
    }
    else
    {
        side = Board.getHumanBoard();
        test = true;
    }
    //test if roll equals an open square
    if ( roll <= Board.getBoardSize() ) {
        
        if ( Board.testSquare( roll-1 , side, test ) )
        {
            squares[0] = roll;
            UserInterface::computermove(squares);
            return squares;
        }
    }
    //check for highest square that also has a corresponding square to add to roll
    for ( int a = roll-1; a >= (roll/2)+1; a--)
    {
        if ( a <= Board.getBoardSize() ) {
            if ( Board.testSquare( a-1 , side, test ) )
            {
                int b = roll - a;
                if ( Board.testSquare( b-1 , side, test ) )
                {
                    if ( a != b )
                    {
                        squares[0] = a;
                        squares[1] = b;
                        UserInterface::computermove(squares);
                        return squares;
                    }
                }
            }
        }
    }
    //check for combinations of three squares starting on lower end since higher squares must already be covered and middle squares are easier to roll
    if ( roll > 5 )
    {
        for ( int a = 1; a < 3; a++ )
        {
            for (int b = a + 1; b < 5; b++)
            {
                int c = roll - (a + b);
                if ( c > 0 && c != a && c != b )
                {
                    if ( Board.testSquare( a-1 , side, test ) )
                    {
                        if ( Board.testSquare( b-1 , side, test ) )
                        {
                            if ( Board.testSquare( c-1 , side, test ) )
                            {
                                squares[0] = a;
                                squares[1] = b;
                                squares[2] = c;
                                UserInterface::computermove(squares);
                                return squares;
                            }
                        }
                    }
                }
            }
        }
    }
    //check for combinations of four squares
    if ( roll > 9) {
        if ( Board.testSquare( 0 , side, test ) )
        {
            if ( Board.testSquare( 1 , side, test ) )
            {
                if ( Board.testSquare( 2 , side, test ) )
                {
                    squares[0] = 1;
                    squares[1] = 2;
                    squares[2] = 3;
                    if ( Board.testSquare( 3 , side, test ) )
                    {
                        squares[3] = 4;
                        UserInterface::computermove(squares);
                        return squares;
                    }
                    else if ( Board.testSquare( 4 , side, test ) )
                    {
                        squares[3] = 5;
                        UserInterface::computermove(squares);
                        return squares;
                    }
                }
            }
        }
    }
    UserInterface::computermove(squares);
    return squares;
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

bool Computer::canThrowOne(Board Board)
{
    bool covered = true;
    //start at 6 because testsquare checks index not square number
    for (int i = 6; i < Board.getBoardSize(); i++)
    {
        if ( Board.testSquare( i, Board.getComBoard(), false ) )
        {
            covered = false;
        }
    }
    if (covered)
    {
        return true;
    }
    return false;
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
bool Computer::canPlay(Board Board, int roll)
{
    if ( checkformove(Board.getHumanBoard(), roll, true) || checkformove(Board.getComBoard(), roll, false) )
    {
        return true;
    }
    return false;
}

