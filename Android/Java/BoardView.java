package com.evanbunge.canoga;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class BoardView {

    private Activity currentgame;

    /*********************************************************************
     Function Name: BoardView
     Purpose: constructor
     Parameters: currentgame, the current activity
     Return Value: none
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public BoardView(Activity currentgame) {

        this.currentgame = currentgame;

    }

    /*********************************************************************
     Function Name: adjustBoardSize
     Purpose: change the GUI to reflect the boardsize
     Parameters: size, int, the boardsize
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void adjustBoardSize(int size) {
        switch (size) {
            case(9):
                currentgame.findViewById(R.id.combox10).setVisibility(View.INVISIBLE);
                currentgame.findViewById(R.id.humanbox10).setVisibility(View.INVISIBLE);
                currentgame.findViewById(R.id.combox11).setVisibility(View.INVISIBLE);
                currentgame.findViewById(R.id.humanbox11).setVisibility(View.INVISIBLE);
                break;
            case(10):
                currentgame.findViewById(R.id.combox11).setVisibility(View.INVISIBLE);
                currentgame.findViewById(R.id.humanbox11).setVisibility(View.INVISIBLE);
                break;
            case(11):
                break;
            default:
                break;
        }
    }

    /*********************************************************************
     Function Name: updateRoll
     Purpose: display roll on screen
     Parameters: roll, int, the roll made
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void updateRoll(int roll) {
        TextView text = (TextView) currentgame.findViewById(R.id.roll);
        text.setText(String.valueOf(roll));
    }

    /*********************************************************************
     Function Name: updateBoard
     Purpose: change view of squares to reflect the current state of the board
     Parameters: board, BoardModel, the current game board
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void updateBoard(BoardModel board) {
        LinearLayout computer = (LinearLayout) currentgame.findViewById(R.id.comboard);
        for (int i = 0; i < board.getBoardSize(); i++) {
            View box = computer.getChildAt(i+1);
            if (board.getComSide()[i] == true) {
                box.setVisibility(View.INVISIBLE);
            }
            else {
                box.setVisibility(View.VISIBLE);
            }
        }
        LinearLayout human = (LinearLayout) currentgame.findViewById(R.id.humanboard);
        for (int i = 0; i < board.getBoardSize(); i++) {
            View box = human.getChildAt(i+1);
            if (board.getHumanSide()[i] == true) {
                box.setVisibility(View.INVISIBLE);
            }
            else {
                box.setVisibility(View.VISIBLE);
            }
        }
    }

    /*********************************************************************
     Function Name: noMoves
     Purpose: tell user there are no more moves available
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void noMoves() {
        TextView text = (TextView) currentgame.findViewById(R.id.playbyplay);
        text.append("There are no moves available");
    }

    /*********************************************************************
     Function Name: updateCurrentTurn
     Purpose: display which player's turn it is
     Parameters: currentTurn, string, the player whose turn it is
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void updateCurrentTurn(String currentTurn) {
        TextView text = (TextView) currentgame.findViewById(R.id.currentturn);
        text.setText(currentTurn);
    }

    /*********************************************************************
     Function Name: savePrompt
     Purpose: prompt the user to save
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void savePrompt() {
        TextView text = (TextView) currentgame.findViewById(R.id.playbyplay);
        text.setText("Would you like to save?");
        currentgame.findViewById(R.id.continuebutton).setVisibility(View.VISIBLE);
        currentgame.findViewById(R.id.savebutton).setVisibility(View.VISIBLE);
    }

    /*********************************************************************
     Function Name: declareFirst
     Purpose: display which player gets first move
     Parameters:
        firstmove, string, who goes first
        humanroll, int, human's roll
        comroll, int, computer's roll
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void declareFirst(String firstmove, int humanroll, int comroll) {
        TextView text = (TextView) currentgame.findViewById(R.id.playbyplay);
        text.append("Human rolled a " + humanroll + "\n");
        text.append("Computer rolled a " + comroll + "\n");
        text.append(firstmove + " will go first");
    }

    /*********************************************************************
     Function Name: presentComTurn
     Purpose: display what the computer did during their turn
     Parameters:
        coverself, boolean, whether the computer covered their board or uncovered the human's
        squares, int array, which squares it chose
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void presentComTurn(boolean coverself, int[] squares) {
        TextView text = (TextView) currentgame.findViewById(R.id.playbyplay);
        text.setVisibility(View.VISIBLE);
        text.append("Computer decided to ");
        if (coverself) {
            text.append("cover ");
        }
        else {
            text.append("uncover ");
        }
        text.append("the following squares: ");
        for (int square:squares) {
            if(square != 0) {
                text.append(String.valueOf(square) + " ");
            }
        }
    }

    /*********************************************************************
     Function Name: dicePrompt
     Purpose: prompt user for number of dice to throw
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void dicePrompt() {
        currentgame.findViewById(R.id.dicebuttons).setVisibility(View.VISIBLE);
    }

    /*********************************************************************
     Function Name: coverPrompt
     Purpose: prompt user to cover or uncover
     Parameters:
        cover, boolean if move is available to cover
        uncover, boolean if move is available to uncover
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void coverPrompt(boolean cover, boolean uncover) {
        currentgame.findViewById(R.id.coverchoicewidgets).setVisibility(View.VISIBLE);
        Button coverbutton = (Button) currentgame.findViewById(R.id.coverbutton);
        Button uncoverbutton = (Button) currentgame.findViewById(R.id.uncoverbutton);
        coverbutton.setVisibility(View.VISIBLE);
        uncoverbutton.setVisibility(View.VISIBLE);
        if (!cover) {
            coverbutton.setVisibility(View.GONE);
        }
        if (!uncover) {
            uncoverbutton.setVisibility(View.GONE);
        }
    }

    /*********************************************************************
     Function Name: clearText
     Purpose: clear the text on the screen
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void clearText() {
        TextView text = (TextView) currentgame.findViewById(R.id.playbyplay);
        TextView roll = (TextView) currentgame.findViewById(R.id.roll);
        text.setText("");
        roll.setText("");
    }

    /*********************************************************************
     Function Name: setCoverChoice
     Purpose: display if user is covering of uncovering
     Parameters: coverself, boolean whether human is covering their board or uncovering computer's
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void setCoverChoice(boolean coverself) {
        TextView text = (TextView) currentgame.findViewById(R.id.coverchoice);
        if (coverself) {
            text.setText("Cover: ");
        }
        else {
            text.setText("Uncover: ");
        }
    }

    /*********************************************************************
     Function Name: diceHelp
     Purpose: display a tip for choosing number of dice
     Parameters: dice, int, number of dice to throw
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void diceHelp(int dice) {
        Toast.makeText(currentgame, "Choose " + String.valueOf(dice) + " dice", Toast.LENGTH_LONG).show();
    }

    /*********************************************************************
     Function Name: coverHelp
     Purpose: display tip for whether to cover or uncover
     Parameters: coverself, boolean, which choice to make
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void coverHelp(boolean coverself) {
        if (coverself) {
            Toast.makeText(currentgame, "Cover your squares", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(currentgame, "Uncover the computer's squares", Toast.LENGTH_LONG).show();
        }
    }

    /*********************************************************************
     Function Name: squareHelp
     Purpose: display tip for which squares to play
     Parameters: squares, int array, which squares to play
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public void squareHelp(int[] squares) {
        int a = squares[0];
        int b = squares[1];
        int c = squares[2];
        int d = squares[3];
        if (b == 0) {
            Toast.makeText(currentgame, "Choose square " + String.valueOf(a), Toast.LENGTH_LONG).show();
        }
        else if (c == 0) {
            Toast.makeText(currentgame, "Choose squares " + String.valueOf(a) + " and " + String.valueOf(b), Toast.LENGTH_LONG).show();
        }
        else if (d == 0) {
            Toast.makeText(currentgame, "Choose squares " + String.valueOf(a) + ", " + String.valueOf(b) + ", and " + String.valueOf(c), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(currentgame, "Choose squares " + String.valueOf(a) + ", " + String.valueOf(b) + ", " + String.valueOf(c) + ", and" + String.valueOf(d), Toast.LENGTH_LONG).show();
        }
    }
}

