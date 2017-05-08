package com.evanbunge.canoga;


import java.util.Arrays;

public class BoardModel {

    private int boardsize;
    private boolean [] humanside;
    private boolean [] comside;
    private boolean turnflag;
    private int handisquare;

    /*********************************************************************
     Function Name:  BoardModel
     Purpose: Constructor
     Parameters: size, an int to represent the number of squares on the board
     Return Value: none
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public BoardModel(int size) {
        setBoardSize(size);
        humanside = new boolean[size];
        comside = new boolean[size];
        Arrays.fill(humanside, false);
        Arrays.fill(comside, false);
        turnflag = false;
        handisquare = 0;
    }

    /*********************************************************************
     Function Name:  updateBoard
     Purpose: update the current state of the gameboard after a move is made
     Parameters:
        squares, int array containing squares to be changed
        currentturn, string to represent whose turn it is
        coverself, whether to cover the player's squares or uncover opponent's
     Return Value: void
     Local Variables:
     Algorithm:
        check whose turn it is and whether they are covering or uncovering
        use this information to decide which side to update
        use array and loop to update correct squares
     Assistance Received: none
     *********************************************************************/
    public void updateBoard(int[] squares, String currentturn, boolean coverself) {
        if (currentturn.equals("Human"))
        {
            if (coverself)
            {
                for (int square : squares) {
                    if (square != 0) {
                        setSquare(getHumanSide(), square, true);
                    }
                }
            }
            else
            {
                for (int square : squares) {
                    if (square != 0) {
                        setSquare(getComSide(), square, false);
                    }
                }
            }
        }
        else
        {
            if (coverself)
            {
                for (int square : squares) {
                    if (square != 0) {
                        setSquare(getComSide(), square, true);
                    }
                }
            }
            else
            {
                for (int square : squares) {
                    if (square != 0) {
                        setSquare(getHumanSide(), square, false);
                    }
                }
            }

        }

    }

    /*********************************************************************
     Function Name:  isGameWon
     Purpose: check if game was won during turn
     Parameters: none
     Return Value: boolean, whether or not game was won
     Local Variables:
        humcovered, boolean flag to test if the human board is covered
        comcovered, boolean flag to test if the computer board is covered
     Algorithm:
        use loop to test if either board is covered, setting flag if not
        if not, use loop to test if either board is uncovered, setting flag if not
     Assistance Received: none
     *********************************************************************/
    public boolean isGameWon() {
        boolean humcovered = true;
        boolean comcovered = true;
        //test if either board is covered
        for (int i = 1; i <= getBoardSize(); i++) {
            if ( testSquare( i, getHumanSide(), false ) )
            {
                humcovered = false;
            }
            if ( testSquare( i, getComSide(), false ) )
            {
                comcovered = false;
            }
        }
        if (humcovered || comcovered)
        {
            return true;
        }
        //test if either board is uncovered and turnflag set
        for (int i = 1; i <= getBoardSize(); i++) {
            if ( testSquare( i, getHumanSide(), true ) )
            {
                humcovered = true;
            }
            if ( testSquare( i, getComSide(), true ) )
            {
                comcovered = true;
            }
        }
        if (!humcovered && firstTurnMade())
        {
            return true;
        }
        if (!comcovered && firstTurnMade())
        {
            return true;
        }
        return false;

    }

    /*********************************************************************
     Function Name: firstTurnMade
     Purpose: check if it is past the first turn
     Parameters: none
     Return Value: boolean, whether the first turn has been made
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public boolean firstTurnMade() {
        return turnflag;
    }

    /*********************************************************************
     Function Name: setTurnFlag
     Purpose: set flag to show the first turn has been made
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void setTurnFlag() {
        turnflag = true;
    }

    /*********************************************************************
     Function Name: calcScore
     Purpose: calculate the score of a game
     Parameters: none
     Return Value: int, the score calculated
     Local Variables:
     Algorithm:
     Assistance Received:
     *********************************************************************/
    public int calcScore() {
        boolean test = getComSide()[0];
        for (int i = 1; i < getBoardSize(); i++) {
            //test to see if squares are the same
            if ( getComSide()[i] != test )
            {
                //if different then get score from this side
                return addSquares(getComSide(), getBoardSize(), !getHumanSide()[0]);
            }
        }
        //otherwise get score from other side
        return addSquares(getHumanSide(), getBoardSize(), !test);
    }

    /*********************************************************************
     Function Name:addSquares
     Purpose: add up the squares to get the score
     Parameters:
        board, boolean array to represent board
        size, int, size of the board
        covered, boolean to compare board to
     Return Value: int, the total taken from the square numbers
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private int addSquares(boolean[] board, int size, boolean covered )
    {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            if ( board[i] == covered )
            {
                sum += i + 1;
            }
        }
        return sum;
    }

    /*********************************************************************
     Function Name: setHandicap
     Purpose: set the handicap for the next game
     Parameters:
        player, string of the player who gets the handicap
        square, square that is handicapped
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void setHandicap(String player, int square) {
        if ( square != 0 ) {
            if (player.equals("Human")) {
                setSquare(getHumanSide(), square, true);
            } else {
                setSquare(getComSide(), square, true);
            }
            handisquare = square;
        }
    }

    /*********************************************************************
     Function Name: testSquare
     Purpose: test if a square is covered / uncovered
     Parameters:
        square, int of the square to be tested
        side, boolean array of the side to be tested
        test, boolean to compare to
     Return Value: boolean, if the square matched the test variable
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public boolean testSquare(int square, boolean[] side, boolean test)
    {
        if (side[square-1] == test) {
            if ( firstTurnMade() || !(square == handisquare) ) {
                return true;
            }
        }
        return false;
    }

    /*********************************************************************
     Function Name: setSquare
     Purpose: set square to uncovered / covered
     Parameters:
        side, boolean array to represent the board
        square, int, square on board
        covered, boolean value to set the square to
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void setSquare(boolean[] side, int square, boolean covered)
    {
        side[square-1] = covered;
    }

    /*********************************************************************
     Function Name: getBoardSize
     Purpose: get the board size
     Parameters: none
     Return Value: int, the size of the board
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public int getBoardSize() {
        return boardsize;
    }

    /*********************************************************************
     Function Name: setBoardSize
     Purpose: sets the size of the board
     Parameters:
        boardsize, int of the size of the board
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void setBoardSize(int boardsize) {
        this.boardsize = boardsize;
    }

    /*********************************************************************
     Function Name: getComSide
     Purpose: get the computer's side of the board
     Parameters: none
     Return Value: boolean array, the computer's board
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public boolean[] getComSide() {
        return comside;
    }

    /*********************************************************************
     Function Name: getHumanSide
     Purpose: get the human's side of the board
     Parameters: none
     Return Value: boolean array, the human's board
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public boolean[] getHumanSide() {
        return humanside;
    }

    /*********************************************************************
     Function Name: checkformove
     Purpose: check if a move is available
     Parameters:
        side, boolean array to represent the side of the board
        roll, int to represent roll
        test, boolean to compare each square to
     Return Value:
        boolean
     Local Variables:
         a, int to represent a possible square to choose
         b, int to represent a possible square to choose
         c, int to represent a possible square to choose
     Algorithm:
        test if the square that equals the roll is available
        test if two available squares add up to equal roll
        test if three available squares add up to equal roll
        test if four available squares add up to equal roll
     Assistance Received: none
     *********************************************************************/
    public boolean checkformove(boolean[] side, int roll, boolean test)
    {
        //test the if there is an open square that equals the roll
        if ( roll <= side.length ) {
            if ( testSquare( roll , side, test ) )
            {
                return true;
            }
        }
        // test for two squares
        //a starts at top and moves down
        //stops halfway since a and b meet in the middle
        for ( int a = roll-1; a >= (roll/2)+1; a--)
        {
            if ( a <= side.length ) {
                if ( testSquare( a , side, test ) )
                {
                    //b is the corresponding square so that a + b = roll
                    int b = roll - a;
                    if ( testSquare( b , side, test ) )
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
        // no three squares add to anything below 6
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
                        if ( testSquare( a , side, test ) )
                        {
                            if ( testSquare( b , side, test ) )
                            {
                                if ( testSquare( c , side, test ) )
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
            if ( testSquare( 1 , side, test ) )
            {
                //second square must be 2
                if ( testSquare( 2 , side, test ) )
                {
                    //third square must be 3
                    if ( testSquare( 3 , side, test ) )
                    {
                        // test square 4
                        if ( testSquare( 4 , side, test ) )
                        {
                            return true;
                        }
                        // or test square 5
                        else if ( testSquare( 5 , side, test ) )
                        {
                            return true;
                        }
                        // or test square 6
                        else if ( testSquare( 6 , side, test ) )
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
}
