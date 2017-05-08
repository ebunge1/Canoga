package com.evanbunge.canoga;


public class Computer extends Player {
    Computer()
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
    public int chooseDice(BoardModel Board)
    {
        boolean covered = true;
        for (int i = 7; i <= Board.getBoardSize(); i++)
        {
            if ( Board.testSquare( i, Board.getHumanSide(), false ) )
            {
                covered = false;
            }
        }
        if ( covered )
        {
            return 1;
        }
        return 2;

    }

    /*************************
     Function Name: decideCover
     Purpose: decide to cover own side or uncover opponents
     Parameters:
         Board, the board being played
         roll, int containing the roll made
     Return Value:
         boolean, true if covering and false if uncovering
     Local Variables: None
     Algorithm:
        cover if moves available
        if no move to cover and the first turn was made, uncover
     Assistance Recieved: None
     *************************/
    public boolean decideCover(BoardModel Board, int roll)
    {

        if ( Board.checkformove(Board.getComSide(), roll, false) ) {
            return true;
        }
        if ( Board.firstTurnMade() )
        {
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
        int array containing the squares to play
     Local Variables:
        a, b, c, d
        used to represent different squares that could be chosen in a move
     Algorithm:
        decide which side to test
        check for highest square possible to cover plus squares that would add up to the roll
        uses same algorithm as BoardModel::checkformove
     Assistance Recieved: None
     *************************/
    public int[] chooseSquares( BoardModel Board, boolean coverself, int roll )
    {
        int[] squares = new int[4];
        boolean[] side;
        boolean test;
        //decide which square to test
        if (coverself)
        {
            side = Board.getComSide();
            test = false;
        }
        else
        {
            side = Board.getHumanSide();
            test = true;
        }
        //test if roll equals an open square
        if ( roll <= Board.getBoardSize() ) {

            if ( Board.testSquare( roll , side, test ) )
            {
                squares[0] = roll;
                return squares;
            }
        }
        //check for highest square that also has a corresponding square to add to roll
        for ( int a = roll-1; a >= (roll/2)+1; a--)
        {
            if ( a <= Board.getBoardSize() ) {
                if ( Board.testSquare( a , side, test ) )
                {
                    int b = roll - a;
                    if ( Board.testSquare( b , side, test ) )
                    {
                        if ( a != b )
                        {
                            squares[0] = a;
                            squares[1] = b;
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
                        if ( Board.testSquare( a , side, test ) )
                        {
                            if ( Board.testSquare( b , side, test ) )
                            {
                                if ( Board.testSquare( c , side, test ) )
                                {
                                    squares[0] = a;
                                    squares[1] = b;
                                    squares[2] = c;
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
            if ( Board.testSquare( 1 , side, test ) )
            {
                if ( Board.testSquare( 2 , side, test ) )
                {
                    if ( Board.testSquare( 3 , side, test ) )
                    {
                        squares[0] = 1;
                        squares[1] = 2;
                        squares[2] = 3;
                        if ( Board.testSquare( 4 , side, test ) )
                        {
                            squares[3] = 4;
                            return squares;
                        }
                        else if ( Board.testSquare( 5 , side, test ) )
                        {
                            squares[3] = 5;
                            return squares;
                        }
                        else if ( Board.testSquare( 6 , side, test ) )
                        {
                            squares[3] = 6;
                            return squares;
                        }
                    }
                }
            }
        }
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

    public boolean canThrowOne(BoardModel Board)
    {
        boolean covered = true;
        for (int i = 7; i <= Board.getBoardSize(); i++)
        {
            if ( Board.testSquare( i, Board.getComSide(), false ) )
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
        boolean, true if a move is available, false if not
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     *************************/
    public boolean canPlay(BoardModel Board, int roll)
    {
        if ( Board.checkformove(Board.getHumanSide(), roll, true) || Board.checkformove(Board.getComSide(), roll, false) )
        {
            return true;
        }
        return false;
    }


}
