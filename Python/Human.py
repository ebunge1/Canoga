from Player import Player
from UserInterface import UserInterface

class Human:
    """""""""""""""""""""""""""""""""""""""
     Function Name: chooseDice
     Purpose: choose the number of dice to throw
     Parameters: Board, this game's board
     Return Value: int, the number of dice to throw
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def chooseDice(self, Board):
        if UserInterface.howManyDice(Board) == 2:
            return 2
        else:
            return 1

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
        can_cover = Player.checkForMove(Board.humanboard, roll, False)
        can_uncover = False
        if Board.turnflag:
            can_uncover = Player.checkForMove(Board.comboard, roll, True)

        return UserInterface.willCover(can_cover, can_uncover, Board, roll)

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
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def chooseSquares(self, Board, coverself, roll):
        if coverself:
            side = Board.humanboard
        else:
            side = Board.comboard
        while(1):
            squares = UserInterface.pickSquares(side, coverself, roll)
            if UserInterface.validateSquares(squares, roll):
                return squares

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
            if Board.testSquare(square, Board.humanboard, False):
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
        if Player.checkForMove( Board.humanboard, roll, False ) or Player.checkForMove( Board.comboard, roll, True ):
            return True
        return False
