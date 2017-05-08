from Board import Board

class Player:
    """""""""""""""""""""""""""""""""""""""
     Function Name: checkForMove
     Purpose: choose which squares to play
     Parameters:
                side, which side to test
                roll, int, the roll made
                test, boolean value to compare to
     Return Value: list of squares
     Local Variables: None
     Algorithm:
            test if the square that equals the roll (if any) is open
            test if two open squares can add up to the roll
            test if three open squares can add up to the roll
            test if four open squares can add up to the roll
     Assistance Recieved: None
     """""""""""""""""""""""""""""""""""""""
    def checkForMove(side, roll, test):
        if roll <= len(side):
            if( Board.testSquare(roll, side, test) ):
                return True
        for a in range(roll-1, int(roll/2), -1):
            if a <= len(side):
                if Board.testSquare(a, side, test):
                    b = roll - a
                    if Board.testSquare(b, side, test):
                        if a != b:
                            return True
        if roll > 5:
            for a in range(1, 4):
                for b in range(a+1, 5):
                    c = roll - (a + b)
                    if c > 0 and c != a and c != b:
                        if Board.testSquare(a, side, test):
                            if Board.testSquare(b, side, test):
                                if Board.testSquare(c, side, test):
                                    return True
        if roll > 9:
            if Board.testSquare(1, side, test):
                if Board.testSquare(2, side, test):
                    if Board.testSquare(3, side, test):
                        if Board.testSquare(4, side, test):
                            return True
                        elif Board.testSquare(5, side, test):
                            return True
                        elif Board.testSquare(6, side, test):
                            return True
                    elif Board.testSquare(4, side, test):
                        if Board.testSquare(5, side, test):
                            return True
        return False
