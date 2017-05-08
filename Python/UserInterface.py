from Board import Board
from Help import GetHelp

class UserInterface:
    @staticmethod
    def newOrLoad():
        choice = ""
        while choice != "new" and choice != "load":
            choice = input("\nWould you like to start a 'new' game or 'load' from file? ")
        if choice == "new":
            return ""
        else:
            filename = input("Which file would you like to load? ")
            print()
            return filename

    @staticmethod
    def askSize():
        boardsize = 0
        print()
        while boardsize < 9 or boardsize > 11:
            try:
                boardsize = int(input("Enter size of board to use(9-11): " ))
            except ValueError:
                boardsize = 0
        return boardsize

    @staticmethod
    def playersRoll():
        print("\nPlayers roll to decide first move.")

    @staticmethod
    def rollResult(player, firstroll, secondroll):
        if secondroll != 0:
            print(player + " rolls a " + str(firstroll) + " and a " + str(secondroll) + " for a combined roll of " + str(firstroll + secondroll) + ".")
        else:
            print(player + " rolls a " + str(firstroll) + ".")
        return firstroll + secondroll

    @staticmethod
    def declareFirst(player):
        print( "The " + player.lower() + " will go first.")

    @staticmethod
    def declareTurn(player):
        print("It is the " + player.lower() + "'s turn.")

    @staticmethod
    def howManyDice(board):
        numdice = 0
        while numdice < 1 or numdice > 2:
            choice = input("Would you like to throw '1' or '2' dice? ")
            if choice == "help":
                UserInterface.dicehelp( GetHelp.chooseDice(board) )
            try:
                numdice = int(choice)
            except ValueError:
                numdice = 0
        return numdice

    @staticmethod
    def willCover(can_cover, can_uncover, board, roll):
        if not can_cover:
            print("No move available to cover. Must uncover opponent's squares.")
            return False
        if not can_uncover:
            print("No move available to uncover. Must cover your own squares.")
            return True
        choice = ""
        while choice != "cover" and choice != "uncover":
            choice = input("Would you like to 'cover' your squares or 'uncover' your opponent's? " )
            if choice == "help":
                UserInterface.coverHelp( GetHelp.decideCover(board, roll) )
        if choice == "cover":
            return True
        return False

    @staticmethod
    def declareGameWon(winner, points):
        print("\nThe " + winner + " is the winner of this game and scores " + str(points) + " points.")

    @staticmethod
    def declareTournWon(winner):
        print("\n" + winner + " is the new champion of Canoga.\n\n")

    @staticmethod
    def noMoves():
        print("There are no moves available. End of Turn.\n")

    @staticmethod
    def newGame():
        while True:
            answer = input("\nWould you like to play another game? " )
            if answer == "yes":
                return True
            if answer == "no":
                return False

    @staticmethod
    def pickSquares(side, unavailable, roll):
        count = 0
        squares = []
        print("Enter the squares you would like to play(0 to stop): ")
        while count <= 4:
            choice = input();
            if choice == "help":
                UserInterface.squareHelp( GetHelp.chooseSquares(side, not unavailable, roll) )
            try:
                choice = int( choice );
            except ValueError:
                choice = -1
            if choice > 0 and choice <= len(side):
                if side[choice] == unavailable:
                    print("Square is unavailable. Please reselect.")
                else:
                    uniqueflag = True
                    for sqr in squares:
                        if choice == sqr:
                            uniqueflag = False
                            print("Cannot pick the same square twice.")
                    if uniqueflag:
                         squares.append(choice)
                         count += 1
            elif choice == 0:
                break
            else:
                print("Enter square number '1' through '" + str(len(side)) + "'.")
        return squares

    @staticmethod
    def validateSquares(squares, roll):
        total = 0
        for sqr in squares:
            total += sqr
        if total != roll:
            print("The sum of the squares must equal your roll.")
            return False
        else:
            return True

    @staticmethod
    def computerMove(coverself, squares):
        if coverself:
            print("The computer chose to cover squares: ", end = "")
        else:
            print("The computer chose to uncover squares: ", end = "")
        for sqr in squares:
            print(str(sqr), end = " ")
        print()

    @staticmethod
    def askToSave():
        while True:
            answer = input("Would you like to save and quit the game? " )
            print()
            if answer == "yes":
                return True
            if answer == "no":
                return False

    @staticmethod
    def filePrompt():
        filename = input("What would you like to name the file? ")
        return filename

    @staticmethod
    def dicehelp(numdice):
        if numdice == 2:
            print("Throw 2 dice to hit higher squares.")
        else:
            print("Throw 1 die, there are no high squares available.")

    @staticmethod
    def coverHelp(coverself):
        if coverself:
            print("Cover your squares. It is best to focus on one side of the board.")
        else:
            print("Cannot cover. Uncover your opponent.")

    @staticmethod
    def squareHelp(squares):
        print( "It is best to aim for the highest squares possible. Choose square: ", end = "")
        for sqr in squares:
            print(str(sqr), end = " ")
        print()
