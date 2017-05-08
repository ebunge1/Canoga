from Board import Board
from BoardView import BoardView
from UserInterface import UserInterface
from Player import Player
from Human import Human
from Computer import Computer
import random
import sys

class Game:
    _dicerolls = []

    """""""""""""""""""""""""""""""""""""""
     Function Name: __init__
     Purpose: constructor
     Parameters: None
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def __init__(self):
        self._firstmove = ""
        self._currentturn = ""

    """""""""""""""""""""""""""""""""""""""
     Function Name: firstmove
     Purpose: property (getter)
     Parameters: None
     Return Value: firstmove
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def firstmove(self):
        return self._firstmove

    """""""""""""""""""""""""""""""""""""""
     Function Name: currentturn
     Purpose: property (getter)
     Parameters: None
     Return Value: currentturn
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def currentturn(self):
        return self._currentturn

    """""""""""""""""""""""""""""""""""""""
     Function Name: dicerolls
     Purpose: property (getter)
     Parameters: None
     Return Value: dicerolls, list of ints
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def dicerolls(self):
        return self._dicerolls

    """""""""""""""""""""""""""""""""""""""
     Function Name: firstmove
     Purpose: property (setter)
     Parameters: player, string
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @firstmove.setter
    def firstmove(self, player):
        self._firstmove = player

    """""""""""""""""""""""""""""""""""""""
     Function Name: currentturn
     Purpose: property (setter)
     Parameters: player, string
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @currentturn.setter
    def currentturn(self, player):
        self._currentturn = player

    """""""""""""""""""""""""""""""""""""""
     Function Name: runGame
     Purpose: execute a game
     Parameters:
                handicap, a dictionary holding info about the handicap
                scores, a dictionary holding the scores of each player
                filename, string of file to read from
     Return Value: None
     Local Variables: None
     Algorithm:
            check if file needs to be loaded
                load game
                check if loaded game has been won
                if so declare winner
            ask for size
            create a board
            figure out who goes first
            draw the board
            enter loop
            execute a turn
            ask to save (if yes, save and exit)
            if game was not won switch player
            exit loop when game is won
            calculate score
            declare winner
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def runGame(self, handicap, scores, filename):
        if filename == "":
            board = Board(UserInterface.askSize(), handicap)
            self.rollForFirst()
        else:
            board = self.copyFromFile(filename + ".txt")
            if board.isGameWon():
                BoardView.drawBoard( board )
                switchTurn( board )
                points = calcScore( board )
                UserInterface.declareGameWon(self.currentturn, points)
                return (self.currentturn, self.firstmove, points)
        BoardView.drawBoard( board )
        while True:
            UserInterface.declareTurn(self.currentturn)
            self.startTurn( board )
            if UserInterface.askToSave():
                self.saveGame(board, scores)
                sys.exit()
            if board.isGameWon():
                break
            else:
                self.switchTurn(board)
                BoardView.drawBoard(board)
        points = self.calcScore( board )
        UserInterface.declareGameWon( self.currentturn, points )
        return {"winner" : self.currentturn, "firstmove" : self.firstmove, "points" : points}

    """""""""""""""""""""""""""""""""""""""
     Function Name: rollForFirst
     Purpose: decide which player goes first
     Parameters: None
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def rollForFirst(self):
        UserInterface.playersRoll()
        humanroll = 0
        comroll = 0
        while humanroll == comroll:
            humanroll = UserInterface.rollResult( "Human", self.rollDie(), self.rollDie() )
            comroll = UserInterface.rollResult( "Computer", self.rollDie(), self.rollDie() )
        if humanroll > comroll:
            self.firstmove = "Human"
        else:
            self.firstmove = "Computer"
        self.currentturn = self.firstmove
        UserInterface.declareFirst(self.firstmove)

    """""""""""""""""""""""""""""""""""""""
     Function Name: canPlay
     Purpose: roll a random die or read die from list
     Parameters: None
     Return Value: int, the roll
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def rollDie(self):
        if self.dicerolls:
            roll = self.dicerolls[0]
            self.dicerolls.remove(roll)
            return roll
        else:
            return random.randint(1, 6)

    """""""""""""""""""""""""""""""""""""""
     Function Name: startTurn
     Purpose: execute a turn
     Parameters:
                Board, tis game's board
     Return Value: None
     Local Variables: None
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
     """""""""""""""""""""""""""""""""""""""
    def startTurn(self, board):
        if self.currentturn == "Human":
            player = Human()
        else:
            player = Computer()
        while True:
            if player.canThrowOne(board):
                numdice = player.chooseDice(board)
            else:
                numdice = 2
            if numdice == 2:
                roll = UserInterface.rollResult( self.currentturn, self.rollDie(), self.rollDie() )
            else:
                roll = UserInterface.rollResult( self.currentturn, self.rollDie(), 0)
            if player.canPlay(board, roll):
                coverself = player.decideCover(board, roll)
                squares = player.chooseSquares(board, coverself, roll)
                if self.currentturn == "Computer":
                    UserInterface.computerMove(coverself, squares)
                board.updateBoard(squares, self.currentturn, coverself)
                BoardView.drawBoard(board)
            else:
                UserInterface.noMoves()
                return
            if board.isGameWon():
                return

    """""""""""""""""""""""""""""""""""""""
     Function Name: switchTurn
     Purpose: switch players
     Parameters: board, the game's board
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def switchTurn(self, board):
        if self.currentturn == "Human":
            self.currentturn = "Computer"
        else:
            self.currentturn = "Human"
        board.setTurnFlag()

    """""""""""""""""""""""""""""""""""""""
     Function Name: calcScore
     Purpose: calculate the winner's score
     Parameters: None
     Return Value: board, this game's board
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def calcScore(self, board):
        test = board.comboard[1]
        for square in board.comboard:
            if board.comboard[square] != test:
                return self.addSquares( board.comboard, not board.humanboard[1] )
        return self.addSquares( board.humanboard, not test )

    """""""""""""""""""""""""""""""""""""""
     Function Name: addSquares
     Purpose: add up the squares to determine the winner's score
     Parameters:
                side, which side to add up
                covered, boolean value to compare to
     Return Value: int, the total
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def addSquares(self, side, covered):
        total = 0
        for square in side:
            if side[square] == covered:
                total += square
        return total

    """""""""""""""""""""""""""""""""""""""
     Function Name: saveGame
     Purpose: write game info to text file
     Parameters:
                Board, this game's board
                scores, dict containing each player's score
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def saveGame(self, board, scores):
        self.switchTurn(board)
        filename = UserInterface.filePrompt()
        f = open(filename + ".txt", 'w')
        f.write("Computer:\n\tSquares: ")
        for square in range(1, board.boardsize + 1):
            if board.comboard[square]:
                f.write("* ")
            else:
                f.write(str(square) + " ")
        f.write("\n\tScore: " + str(scores["computer"]) + "\n\nHuman:\n\tSquares: ")
        for square in range(1, board.boardsize + 1):
            if board.humanboard[square]:
                f.write("* ")
            else:
                f.write(str(square) + " ")
        f.write("\n\tScore: " + str(scores["human"]) )
        f.write("\n\nFirst Turn: " + self.firstmove + "\nNext Turn: " + self.currentturn)
        f.close()
        print("Game Saved.\n")

    """""""""""""""""""""""""""""""""""""""
     Function Name: copyFromFile
     Purpose: read game info from file
     Parameters: filename, string of file to read from
     Return Value: the game's board
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def copyFromFile(self, filename):
        f = open(filename, 'r')
        for line in f:
            tokens = line.split()
            if tokens:
                if tokens[0] == "Computer:":
                    line = f.readline()
                    tokens = line.split()
                    tokens.remove("Squares:")
                    board = Board(len(tokens), {})
                    for square in range(0, len(tokens)):
                        if tokens[square] == "*":
                            board.setSquare(board.comboard, square+1, True)
                if tokens[0] == "Human:":
                    line = f.readline()
                    tokens = line.split()
                    tokens.remove("Squares:")
                    for square in range(0, len(tokens)):
                        if tokens[square] == "*":
                            board.setSquare(board.humanboard, square+1, True)
                if tokens[0] == "First":
                    self.firstmove = tokens[2]
                if tokens[0] == "Next":
                    self.currentturn = tokens[2]
                if tokens[0] == "Dice:":
                    while True:
                        line = f.readline()
                        if line:
                            rolls = line.split()
                            for roll in rolls:
                                self.dicerolls.append( int(roll) )
                        else:
                            break
        f.close()
        board.setTurnFlag()
        return board
