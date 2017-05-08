#include "Board.h"

/*************************
 Function Name: Board
 Purpose:   default constructor. intitialize member variables
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
            initialize int to 0
            intitialize pointers to point to nothing
 Assistance Recieved: None
 *************************/
Board::Board()
{
    boardsize = 0;
    turnflag = false;
}

Board::Board( unsigned int size )
{
    boardsize = size;
    turnflag = false;
    for(int i = 0; i < size; i++)
    {
        humansquares.push_back(false);
        comsquares.push_back(false);
    }
}

/*************************
 Function Name: Board
 Purpose: constructor. create boards for each player
 Parameters: size
 Return Value: None
 Local Variables: None
 Algorithm:
            initialize int to size
            intitialize pointers to point to new arrays of parameter size
            fill each array 1-size
 Assistance Recieved: None
 *************************/
Board::Board( unsigned int size, Handicap* Handicap )
{
    boardsize = size;
    turnflag = false;
    for(int i = 0; i < size; i++)
    {
        humansquares.push_back(false);
        comsquares.push_back(false);
    }
    if (Handicap->player == "Human")
    {
        setSquare(humansquares, Handicap->square-1, true );
    }
    else if (Handicap->player == "Computer")
    {
        setSquare(comsquares, Handicap->square-1, true );
    }
}

const vector<bool> Board::getHumanBoard() const
{
    return humansquares;
}

const vector<bool> Board::getComBoard() const
{
    return comsquares;
}

bool Board::testSquare(int square, const vector<bool> side, bool test)
{
    return side[square] == test;
}

int Board::getBoardSize() const
{
    return boardsize;
}

void Board::setSquare(vector<bool> &side, int square, bool covered)
{
    side[square] = covered;
}

void Board::updateBoard(const vector<int> squares, string player, bool coverself)
{
    if (player == "Human")
    {
        if (coverself)
        {
            for (int i = 0; i < squares.size(); i++)
            {
                if (squares[i] != 0)
                {
                    setSquare(humansquares, squares[i]-1, true);
                }
            }
        }
        else
        {
            for (int i = 0; i < squares.size(); i++)
            {
                if (squares[i] != 0)
                {
                    setSquare(comsquares, squares[i]-1, false);
                }
            }
        }
    }
    else
    {
        if (coverself)
        {
            for (int i = 0; i < squares.size(); i++)
            {
                if (squares[i] != 0)
                {
                    setSquare(comsquares, squares[i]-1, true);
                }
            }
        }
        else
        {
            for (int i = 0; i < squares.size(); i++)
            {
                if (squares[i] != 0)
                {
                    setSquare(humansquares, squares[i]-1, false);
                }
            }
        }

    }
}

bool Board::firstTurnMade()
{
    return turnflag;
}

void Board::setTurnFlag()
{
    turnflag = true;
}

bool Board::isGameWon(){
    bool humcovered = true;
    bool comcovered = true;
    //test if either board is covered
    for (int i = 0; i < getBoardSize(); i++) {
        if ( testSquare( i, getHumanBoard(), false ) )
        {
            humcovered = false;
        }
        if ( testSquare( i, getComBoard(), false ) )
        {
            comcovered = false;
        }
    }
    if (humcovered == true || comcovered == true)
    {
        return true;
    }
    //test if either board is uncovered and turnflag set
    for (int i = 0; i < getBoardSize(); i++) {
        if ( testSquare( i, getHumanBoard(), true ) )
        {
            humcovered = true;
        }
        if ( testSquare( i, getComBoard(), true ) )
        {
            comcovered = true;
        }
    }
    if (humcovered == false && firstTurnMade())
    {
        return true;
    }
    if (comcovered == false && firstTurnMade())
    {
        return true;
    }
    return false;
}






