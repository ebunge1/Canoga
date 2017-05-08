#include "Player.h"
#include <iostream>

/*************************
 Function Name: Player
 Purpose: contructor
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Player::Player()
{
}

/*************************
 Function Name: ~Player
 Purpose: destructor. necessary to use player class as a pointer
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
Player::~Player()
{
}

/*************************
 Function Name: checkformove
 Purpose: check if any available moves exist
 Parameters:
            side, vector<bool> representing the side of the board being tested
            roll, int containing the roll made
            test, bool to compare each square to
 Return Value:
            bool, true if a legal move exists
 Local Variables:
            a, b, c, d
            used to represent different squares that could be chosen in a move
 Algorithm:
            test if the square that equals the roll (if any) is open
            test if two open squares can add up to the roll
            test if three open squares can add up to the roll
            test if four open squares can add up to the roll
 Assistance Recieved: None
 *************************/
bool Player::checkformove(vector<bool> side, int roll, bool test)
{
    //test the if there is an open square that equals the roll
    if ( roll <= side.size() ) {
        if ( Board::testSquare( roll-1 , side, test ) )
        {
            return true;
        }
    }
    // test for two squares
    //a starts at top and moves down
    //stops halfway since a and b meet in the middle
    for ( int a = roll-1; a >= (roll/2)+1; a--)
    {
        if ( a <= side.size() ) {
            if ( Board::testSquare( a-1 , side, test ) )
            {
                //b is the corresponding square so that a + b = roll
                int b = roll - a;
                if ( Board::testSquare( b-1 , side, test ) )
                {
                    //check that a and b are not the same square
                    if ( a != b )
                    {
                        return true;
                    }
                }
            }
        }
    }
    // test for three squares
    // no three squares add to anything below 5
    if ( roll > 5 )
    {
        //the first square must be below 3
        for ( int a = 1; a < 3; a ++ )
        {
            //the second square must be greater than the first and less than 5
            for (int b = a + 1; b < 5; b++)
            {
                // third square must add up to the roll
                int c = roll - (a + b);
                // the squares cannot be the same
                if ( c > 0 && c != a && c != b )
                {
                    if ( Board::testSquare( a-1 , side, test ) )
                    {
                        if ( Board::testSquare( b-1 , side, test ) )
                        {
                            if ( Board::testSquare( c-1 , side, test ) )
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }
    // test for four squares
    // no four squares add to anything less than 10
    if ( roll > 9) {
        //first square must be 1
        if ( Board::testSquare( 0 , side, test ) )
        {
            //second square must be 2
            if ( Board::testSquare( 1 , side, test ) )
            {
                //third square must be 3
                if ( Board::testSquare( 2 , side, test ) )
                {
                    // test square 4
                    if ( Board::testSquare( 3 , side, test ) )
                    {
                        return true;
                    }
                    // or test square 5
                    else if ( Board::testSquare( 4 , side, test ) )
                    {
                        return true;
                    }
                }
            }
        }
    }
    // no move found
    return false;
}
