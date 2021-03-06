from Player import Player

class Computer:
    """""""""""""""""""""""""""""""""""""""
     Function Name: chooseDice
     Purpose: choose the number of dice to throw
     Parameters: Board, this game's board
     Return Value: int, the number of dice to throw
     Local Variables: None
     Algorithm:
                throw 1 if human's board 7-n is uncovered
                otherwise throw 2
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def chooseDice(self, Board):
        covered = True
        for square in range(7, Board.boardsize + 1):
            if Board.testSquare(square, Board.humanboard, False):
                covered = False
        if covered:
            return 1
        else:
            return 2

    """""""""""""""""""""""""""""""""""""""
     Function Name: decideCover
     Purpose: choose to cover or uncover
     Parameters:
                Board, tis game's board
                roll, int, the roll made
     Return Value: boolean whether to cover or not
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def decideCover(self, Board, roll):
        if Player.checkForMove( Board.comboard, roll, False ):
            return True
        if Board.turnflag:
            return False

    """""""""""""""""""""""""""""""""""""""
     Function Name: chooseSquares
     Purpose: choose which squares to play
     Parameters:
                Board, this game's board
                coverself, boolean, whether to cover
                roll, int, the roll made
     Return Value: list of squares
     Local Variables: None
     Algorithm:
            test if the square that equals the roll (if any) is open
            test if two open squares can add up to the roll
            test if three open squares can add up to the roll
            test if four open squares can add up to the roll
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def chooseSquares(self, Board, coverself, roll):
        if coverself:
            side = Board.comboard
            test =  False
        else:
            side = Board.humanboard
            test = True
        if roll <= Board.boardsize:
            if( Board.testSquare(roll, side, test) ):
                squares = [roll]

                return squares
        for a in range(roll-1, int(roll/2), -1):
            if a <= Board.boardsize:
                if Board.testSquare(a, side, test):
                    b = roll - a
                    if Board.testSquare(b, side, test):
                        if a != b:
                            squares = [a, b]

                            return squares
        if roll > 5:
            for a in range(1, 4):
                for b in range(a+1, 5):
                    c = roll - (a + b)
                    if c > 0 and c != a and c != b:
                        if Board.testSquare(a, side, test):
                            if Board.testSquare(b, side, test):
                                if Board.testSquare(c, side, test):
                                    squares = [a, b, c]

                                    return squares
        if roll > 9:
            if Board.testSquare(1, side, test):
                if Board.testSquare(2, side, test):
                    if Board.testSquare(3, side, test):
                        if Board.testSquare(4, side, test):
                            squares = [1, 2, 3, 4]

                            return squares
                        elif Board.testSquare(5, side, test):
                            squares = [1, 2, 3, 5]

                            return squares
                        elif Board.testSquare(6, side, test):
                            squares = [1, 2, 3, 6]

                            return squares
                    elif Board.testSquare(4, side, test):
                        if Board.testSquare(5, side, test):
                            squares = [1, 2, 4, 5]

                            return squares
        return []

    """""""""""""""""""""""""""""""""""""""
     Function Name: canThrowOne
     Purpose: test if one die can be thrown
     Parameters:
                Board, tis game's board
     Return Value: boolean, whether one die can be thrown
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def canThrowOne( self, Board ):
        covered = True
        for square in range(7, Board.boardsize + 1):
            if Board.testSquare(square, Board.comboard, False):
                covered = False
        if covered:
            return True
        else:
            return False

    """""""""""""""""""""""""""""""""""""""
     Function Name: canPlay
     Purpose: test if any moves are alvailable to play
     Parameters:
                Board, tis game's board
                roll, int, the roll made
     Return Value: boolean whether any moves are available
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def canPlay(self, Board, roll):
        if Player.checkForMove( Board.humanboard, roll, True ) or Player.checkForMove( Board.comboard, roll, False ):
            return True
        return False
