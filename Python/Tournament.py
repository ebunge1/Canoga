from Game import Game
from BoardView import BoardView
from UserInterface import UserInterface

class Tournament:
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
        self._humanscore = 0
        self._comscore = 0

    """""""""""""""""""""""""""""""""""""""
     Function Name: humanscore
     Purpose: property (getter)
     Parameters: None
     Return Value: humanscore
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def humanscore(self):
        return self._humanscore

    """""""""""""""""""""""""""""""""""""""
     Function Name: comscore
     Purpose: property (getter)
     Parameters: None
     Return Value: comscore
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    @property
    def comscore(self):
        return self._comscore

    """""""""""""""""""""""""""""""""""""""
     Function Name: updateHumanScore
     Purpose: update the human's score
     Parameters: points, int
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def updateHumanScore(self, points):
        self._humanscore += points

    """""""""""""""""""""""""""""""""""""""
     Function Name: updateComScore
     Purpose: update the com's score
     Parameters: points, int
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def updateComScore(self, points):
        self._comscore += points

    """""""""""""""""""""""""""""""""""""""
     Function Name: playTournament
     Purpose: execute a Tournament
     Parameters: filename, string, file to load from
     Return Value: None
     Local Variables: None
     Algorithm:
            check if save file needs to be loaded
                if so, load the file
            enter loop
                run a game
                update the score
                calculate new handicap
                display new score
            ask to play again
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def playTournament(self, filename):
        if filename != "":
            self.copyFromFile(filename + ".txt")
            BoardView.displayScore(self.humanscore, self.comscore)
        handicap = dict()
        while True:
            game = Game()
            scores = {"human" : self.humanscore, "computer" : self.comscore}
            winner = game.runGame(handicap, scores, filename)
            self.updateScores(winner)
            handicap = self.calcHandicap(winner)
            BoardView.displayScore(self.humanscore, self.comscore)
            filename = ""
            if not UserInterface.newGame():
                break
        UserInterface.declareTournWon( self.final() )

    """""""""""""""""""""""""""""""""""""""
     Function Name: updateScores
     Purpose: update the score of the winner
     Parameters:
                winner, string
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def updateScores(self, winner):
        if winner["winner"] == "Human":
            self.updateHumanScore( winner["points"] )
        else:
            self.updateComScore( winner["points"] )

    """""""""""""""""""""""""""""""""""""""
     Function Name: calcHandicap
     Purpose: calculate the handicap for the next game
     Parameters:
                winner, string
     Return Value: handicap, dictionary
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def calcHandicap(self, winner):
        handicap = dict()
        if winner["winner"] != winner["firstmove"]:
            handicap["player"] = winner["winner"]
        else:
            if winner["winner"] == "Human":
                handicap["player"] = "Computer"
            else:
                handicap["player"] = "Human"
        temp = winner["points"]
        while True:
            #get one's digit
            handicap["square"] = temp % 10
            #add ten's digit
            handicap["square"] += (temp - handicap["square"]) / 10
            temp = handicap["square"]
            if handicap["square"] <= 9:
                break
        return handicap

    """""""""""""""""""""""""""""""""""""""
     Function Name: final
     Purpose: get who won
     Parameters:
     Return Value: string, the winner
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def final(self):
        if self.humanscore > self.comscore:
            return "The Human"
        elif self.comscore > self.humanscore:
            return "The Computer"
        else:
            return "It's a draw. Neither player"

    """""""""""""""""""""""""""""""""""""""
     Function Name: copyFromFile
     Purpose: read scores from file
     Parameters: filename, string, file to read
     Return Value: None
     Local Variables: None
     Algorithm:
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def copyFromFile(self, filename):
        f = open(filename, 'r')
        for line in f:
            tokens = line.split()
            if tokens:
                if (tokens[0]) == "Computer:":
                    f.readline()
                    line = f.readline()
                    tokens = line.split()
                    self.updateComScore( int(tokens[1]) )
                if (tokens[0]) == "Human:":
                    f.readline()
                    line = f.readline()
                    tokens = line.split()
                    self.updateHumanScore( int(tokens[1]) )
        f.close()
