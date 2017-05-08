from Board import Board

class BoardView:
    """""""""""""""""""""""""""""""""""""""
     Function Name: drawBoard
     Purpose: draw the board
     Parameters: Board, the board to draw
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @staticmethod
    def drawBoard( Board ):
        print("**************************************************************")
        print("--------------------------------------------------------\n| ", end = "")
        for square in range(1, Board.boardsize + 1):
            if Board.comboard[square]:
                print("X", end = "")
            else:
                print( str(square), end = "" )
            print(" | ", end = "")
        print("Computer")
        print("--------------------------------------------------------")
        print("--------------------------------------------------------\n| ", end = "")
        for square in range(1, Board.boardsize + 1):
            if Board.humanboard[square]:
                print("X", end = "")
            else:
                print( str(square), end = "" )
            print(" | ", end = "")
        print("Human")
        print("--------------------------------------------------------\n")

    """""""""""""""""""""""""""""""""""""""
     Function Name: setSquare
     Purpose: set a square
     Parameters:
                humanscore, int, the human's score
                comscore, int, the comscore's score
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @staticmethod
    def displayScore( humanscore, comscore ):
        print("Computer Score: " + str(comscore) + "          Human Score: " + str(humanscore) )
