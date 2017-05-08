class Board:
     """""""""""""""""""""""""""""""""""""""
     Function Name: __init__
     Purpose: constructor. intitialize member variables
     Parameters:
            size, int for boardsize
            handicap, dict containing information about handicap
     Return Value: None
     Local Variables:
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def __init__(self, size, handicap):
        self._boardsize = size
        self._turnflag = False
        self._humanboard = dict()
        self._comboard = dict()
        for x in range(1, size + 1):
            self.humanboard[x] = False
            self.comboard[x] = False
        if handicap:
            if handicap["player"] == "Human":
                self.setSquare(self.humanboard, handicap["square"], True)
            else:
                self.setSquare(self.comboard, handicap["square"], True)

    """""""""""""""""""""""""""""""""""""""
     Function Name: humanboard
     Purpose: property (getter)
     Parameters: None
     Return Value: the humanboard as a dict
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def humanboard(self):
        return self._humanboard

    """""""""""""""""""""""""""""""""""""""
     Function Name: comboard
     Purpose: property (getter)
     Parameters: None
     Return Value: the comboard as a dict
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def comboard(self):
        return self._comboard

    """""""""""""""""""""""""""""""""""""""
     Function Name: boardsize
     Purpose: property (getter)
     Parameters: None
     Return Value: the humanboard as a dict
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def boardsize(self):
        return self._boardsize

    """""""""""""""""""""""""""""""""""""""
     Function Name: turnflag
     Purpose: property (getter)
     Parameters: None
     Return Value: a boolean flag to determine if the first turn was made
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def turnflag(self):
        return self._turnflag

    """""""""""""""""""""""""""""""""""""""
     Function Name: setTurnFlag
     Purpose: setter
     Parameters: None
     Return Value: set the turnflag
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def setTurnFlag(self):
        self._turnflag = True

    """""""""""""""""""""""""""""""""""""""
     Function Name: testSquare
     Purpose: test if square is covered / uncovered
     Parameters:
            square, int for the square to test
            side, the side of the board
            test, boolean value to compare to
     Return Value: boolean if square matches test
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @staticmethod
    def testSquare(square, side, test):
        return side[square] == test

    """""""""""""""""""""""""""""""""""""""
     Function Name: setSquare
     Purpose: set a square
     Parameters:
                square, int for the square to test
                side, the side of the board
                covered, boolean value to set as
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def setSquare(self, side, square, covered):
        side[square] = covered

    """""""""""""""""""""""""""""""""""""""
     Function Name: updateBoard
     Purpose: update the board after a turn
     Parameters:
                squares, the squares to update
                player, string for this turn's player
                coverself, whether the player is covering or uncovering
     Return Value:  None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def updateBoard(self, squares, player, coverself):
        if player == "Human":
            if coverself:
                for sqr in squares:
                        self.setSquare(self.humanboard, sqr, True)
            else:
                for sqr in squares:
                        self.setSquare(self.comboard, sqr, False)
        else:
            if coverself:
                for sqr in squares:
                        self.setSquare(self.comboard, sqr, True)
            else:
                for sqr in squares:
                        self.setSquare(self.humanboard, sqr, False)

    """""""""""""""""""""""""""""""""""""""
     Function Name: isGameWon
     Purpose: test if the game was won
     Parameters: None
     Return Value: boolean if game is won or not
     Local Variables: None
     Algorithm:
            test if either board is completely covered or uncovered
            using a two loops and setting a flag if a square is different
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def isGameWon(self):
        humcovered = True
        comcovered = True
        for square in range(1, self.boardsize + 1):
            if self.testSquare(square, self.humanboard, False):
                humcovered = False
            if self.testSquare(square, self.comboard, False):
                comcovered = False
        if humcovered or comcovered:
            return True
        for square in range(1, self.boardsize + 1):
            if self.testSquare(square, self.humanboard, True):
                humcovered = True
            if self.testSquare(square, self.comboard, True):
                comcovered = True
        if not humcovered and self.turnflag:
            return True
        if not comcovered and self.turnflag:
            return True
        return False
