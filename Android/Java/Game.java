package com.evanbunge.canoga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Game extends AppCompatActivity {

    private Random rand = new Random(System.currentTimeMillis());

    private static final int SIZE_CODE = 1;
    private String firstmove;
    private String currentturn;
    private Queue<Integer> dicerolls;
    BoardView boardview;
    BoardModel board;
    int roll;
    Human human;
    Computer computer;
    int placemarker;

    /*********************************************************************
     Function Name: onCreate
     Purpose: setup game
     Parameters: savedInstanceState, Bundle
     Return Value: void
     Local Variables:
        gamesetup, Intent, holds information to setup the game
        serialized_dice, int array, dice rolls read from file
        comboard, boolean array, the computer's board
        humanboard, boolean array, the human's board
        getsize, Intent, starts activity to get boardsize from user
     Algorithm:
        initialize member variables
        test if serializing
            read in file and update information using file
            start the next turn
        start activity to get board size from user
     Assistance Received: none
     *********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard);
        Intent gamesetup = getIntent();
        boardview = new BoardView(this);
        human = new Human();
        computer = new Computer();
        //check if serialized
        int[] serialized_dice = gamesetup.getIntArrayExtra("dicerolls");
        dicerolls = new LinkedList<Integer>();
        if (serialized_dice != null) {
            for (int i = 0; i < serialized_dice.length; i++) {
                dicerolls.add(serialized_dice[i]);
            }
        }
        boolean serialized = gamesetup.getBooleanExtra("serialized", false);
        if (serialized) {
            //set from file
            boolean[] comboard = gamesetup.getBooleanArrayExtra("comboard");
            boolean[] humanboard = gamesetup.getBooleanArrayExtra("humanboard");
            board = new BoardModel(comboard.length);
            boardview.adjustBoardSize(board.getBoardSize());
            int[] comsqrs = new int[comboard.length];
            int[] humansqrs = new int[humanboard.length];
            for (int i = 0; i < board.getBoardSize(); i++) {
                if (comboard[i]) {
                    comsqrs[i] = i+1;
                }
                if (humanboard[i]) {
                    humansqrs[i] = i+1;
                }
            }
            board.updateBoard(comsqrs, "Computer", true);
            board.updateBoard(humansqrs, "Human", true);
            boardview.updateBoard(board);
            setFirstMove(gamesetup.getStringExtra("firstmove"));
            setCurrentTurn(gamesetup.getStringExtra("currentturn"));
            boardview.updateCurrentTurn(getCurrentTurn());
            board.setTurnFlag();
            startTurn();
        } else {
            //no serialization
            Intent getsize = new Intent(this, BoardSelect.class);
            getsize.putExtras( getIntent().getExtras() );
            startActivityForResult(getsize, SIZE_CODE);
        }
    }

    /*********************************************************************
     Function Name: onActivityResult
     Purpose: set up board size
     Parameters:
        requestCode, int
        resultCode, int
        data, Intent
     Return Value: void
     Local Variables:
     Algorithm:
        set size of board and handicap
        roll for first
     Assistance Received: none
     *********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //setup board size
        int size = data.getIntExtra("size", 0);
        board = new BoardModel(size);
        boardview.adjustBoardSize(size);

        String player = data.getStringExtra("handicappedplayer");
        int square = data.getIntExtra("handicap", 0);
        board.setHandicap(player, square);
        boardview.updateBoard(board);
        rollforfirst();
        hold(1);
    }

    /*********************************************************************
     Function Name: startTurn
     Purpose: start the player's turn
     Parameters: none
     Return Value: void
     Local Variables:
        one, two, help
            buttons
     Algorithm:
        clear text and update turn info
        check for whose turn it is
            check if one die can be thrown
                set buttons for input
            make the move
        execute computer turn
     Assistance Received: none
     *********************************************************************/
    private void startTurn() {
        boardview.clearText();
        boardview.updateCurrentTurn(getCurrentTurn());
        if (getCurrentTurn().equals("Human")) {
            // check if one die can be thrown
            if (human.canThrowOne(board)) {
                // can throw one die, player decides how many to throw
                boardview.dicePrompt();
                Button one = (Button) findViewById(R.id.throwone);
                Button two = (Button) findViewById(R.id.throwtwo);
                one.setOnClickListener(dicelistener);
                two.setOnClickListener(dicelistener);

                Button help = (Button) findViewById(R.id.helpbutton);
                help.setVisibility(View.VISIBLE);
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boardview.diceHelp(human.chooseDice(board));
                    }
                });
            } else {
                // cannot throw one die, throws two
                makeMove(2);
            }
        } else {
            computerTurn();
        }
    }

    private View.OnClickListener dicelistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            findViewById(R.id.dicebuttons).setVisibility(View.GONE);
            switch (view.getId()) {
                case R.id.throwone:
                    makeMove(1);
                    break;
                case R.id.throwtwo:
                    makeMove(2);
                    break;
            }
        }
    };

    /*********************************************************************
     Function Name: makeMove
     Purpose: roll and present cover options
     Parameters: numdice, int, how many dice to throw
     Return Value:  void
     Local Variables:
        cover, uncover, help
            buttons
     Algorithm:
        roll dice
        check if moves are available
            set buttons for input
        end turn
     Assistance Received: none
     *********************************************************************/
    public void makeMove(int numdice) {
        if (numdice == 1) {
            roll = rollDie();
        } else {
            roll = rollDie() + rollDie();
        }
        boardview.updateRoll(roll);
        // check for available moves
        if (human.canPlay(board, roll)) {
            // boolean true if covering and false if uncovering
            boolean can_cover = board.checkformove(board.getHumanSide(), roll, false);
            boolean can_uncover = board.checkformove(board.getComSide(), roll, true);
            boardview.coverPrompt(can_cover, can_uncover);
            Button cover = (Button) findViewById(R.id.coverbutton);
            Button uncover = (Button) findViewById(R.id.uncoverbutton);
            cover.setOnClickListener(coverlistener);
            uncover.setOnClickListener(coverlistener);

            Button help = (Button) findViewById(R.id.helpbutton);
            help.setVisibility(View.VISIBLE);
            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boardview.coverHelp(human.decideCover(board, roll));
                }
            });
        } else {
            findViewById(R.id.helpbutton).setVisibility(View.GONE);
            boardview.noMoves();
            //no moves available, end turn
            hold(2);
        }
    }

    private View.OnClickListener coverlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            findViewById(R.id.coverchoicewidgets).setVisibility(View.GONE);
            switch (view.getId()) {
                case R.id.coverbutton:
                    human.setCoverSelf(true);
                    break;
                case R.id.uncoverbutton:
                    human.setCoverSelf(false);
                    break;
            }
            pickSquares(human.getCoverSelf());
        }
    };

    /*********************************************************************
     Function Name: picksquares
     Purpose: pick the squares to play
     Parameters: coverself, boolean to cover own board or not. final to use in setOnClickListener()
     Return Value: void
     Local Variables:
        choice1, choice2, choice3, choice4
            spinners
     Algorithm:
        fill spinners with choices
        set spinners to get square choices from user
        set help button
     Assistance Received:none
     *********************************************************************/
    public void pickSquares(final boolean coverself) {
        boardview.setCoverChoice( human.getCoverSelf() );
        findViewById(R.id.boxchoicewidgets).setVisibility(View.VISIBLE);
        boolean[] side;
        if (coverself) {
            side = board.getHumanSide();
        } else {
            side = board.getComSide();
        }
        ArrayAdapter<Integer> newadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getSpinnerList(side, coverself));
        Spinner choice1 = (Spinner) findViewById(R.id.boxchoice1);
        Spinner choice2 = (Spinner) findViewById(R.id.boxchoice2);
        Spinner choice3 = (Spinner) findViewById(R.id.boxchoice3);
        Spinner choice4 = (Spinner) findViewById(R.id.boxchoice4);
        choice1.setAdapter(newadapter);
        choice2.setAdapter(newadapter);
        choice3.setAdapter(newadapter);
        choice4.setAdapter(newadapter);
        choice1.setOnItemSelectedListener(testinputs);
        choice2.setOnItemSelectedListener(testinputs);
        choice3.setOnItemSelectedListener(testinputs);
        choice4.setOnItemSelectedListener(testinputs);

        findViewById(R.id.helpbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardview.squareHelp(human.chooseSquares(board, coverself, roll));
            }
        });
    }

    /*********************************************************************
     Function Name: endGame
     Purpose: end the game and return the result
     Parameters: none
     Return Value: void
     Local Variables:
        points, int, the points won
        gameresults, Intent, holds information about the game
     Algorithm:
     Assistance Received:
     *********************************************************************/
    private void endGame() {
        int points = board.calcScore();
        Intent gameresults = new Intent();
        gameresults.putExtra("firstmove", firstmove);
        gameresults.putExtra("winner", getCurrentTurn());
        gameresults.putExtra("points", points);
        gameresults.putExtra("serialized", false);
        int[] serialized_dice = new int[dicerolls.size()];
        for (int i = 0; i < serialized_dice.length; i++) {
            serialized_dice[i] = dicerolls.poll();
        }
        gameresults.putExtra("dicerolls", serialized_dice);
        setResult(RESULT_OK, gameresults);
        finish();
    }

    /*********************************************************************
     Function Name: saveprompt
     Purpose: prompt the user to save
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void savePrompt() {
        boardview.savePrompt();
        findViewById(R.id.savebutton).setOnClickListener(save_continue);
        findViewById(R.id.continuebutton).setOnClickListener(save_continue);
    }

    private View.OnClickListener save_continue = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            findViewById(R.id.savebutton).setVisibility(View.GONE);
            findViewById(R.id.continuebutton).setVisibility(View.GONE);
            switch (view.getId()) {
                case R.id.continuebutton:
                    startTurn();
                    break;
                case R.id.savebutton:
                    Intent savegame = new Intent();
                    savegame.putExtra("comside", board.getComSide());
                    savegame.putExtra("humanside", board.getHumanSide());
                    savegame.putExtra("firstmove", firstmove);
                    savegame.putExtra("currentturn", currentturn);
                    savegame.putExtra("serialized", true);
                    setResult(RESULT_OK, savegame);
                    finish();
                    break;
            }
        }
    };

    /*********************************************************************
     Function Name: switchTurn
     Purpose: switch which player's turn it is
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void switchTurn() {
        board.setTurnFlag();
        if (getCurrentTurn().equals("Human")) {
            setCurrentTurn("Computer");
        } else {
            setCurrentTurn("Human");
        }
    }

    /*********************************************************************
     Function Name: rollDie
     Purpose: get the roll
     Parameters: none
     Return Value: int, the roll made
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private int rollDie() {
        if (!dicerolls.isEmpty()) {
            //get from queue
            return dicerolls.poll();
        } else {
            int roll = rand.nextInt(6) + 1;
            return roll;
        }
    }

    /*********************************************************************
     Function Name:rollforfirst
     Purpose: decide which player goes first
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void rollforfirst() {
        int humanroll;
        int comroll;
        do {
            humanroll = rollDie() + rollDie();
            comroll = rollDie() + rollDie();
        } while (humanroll == comroll);
        if (humanroll > comroll) {
            firstmove = "Human";
        } else {
            firstmove = "Computer";
        }
        currentturn = firstmove;
        boardview.declareFirst(firstmove, humanroll, comroll);
    }

    /*********************************************************************
     Function Name: computerTurn
     Purpose: execute the computer's turn
     Parameters: none
     Return Value: void
     Local Variables:
     Algorithm:
        check if one die can be thrown
        roll dice
        check if a move is available
            choose to cover or uncover
            choose squares
            update board and display the turn
        endturn
     Assistance Received:none
     *********************************************************************/
    private void computerTurn() {
        int numdice;
        int roll;
        if (computer.canThrowOne(board)) {
            // can throw one die, player decides how many to throw
            numdice = computer.chooseDice(board);
        } else {
            // cannot throw one die, throws two
            numdice = 2;
        }
        if (numdice == 1) {
            roll = rollDie();
        } else {
            roll = rollDie() + rollDie();
        }
        boardview.updateRoll(roll);
        // check for available moves
        if (computer.canPlay(board, roll)) {
            // boolean true if covering and false if uncovering
            boolean coverself = computer.decideCover(board, roll);
            // vector of squares that are being changed
            int[] squares = computer.chooseSquares(board, coverself, roll);
            board.updateBoard(squares, getCurrentTurn(), coverself);
            boardview.updateBoard(board);
            boardview.presentComTurn(coverself, squares);
            hold(1);
        } else {
            boardview.noMoves();
            //no moves available, end turn
            hold(2);
        }
    }

    /*********************************************************************
     Function Name: hold
     Purpose: wait for user to continue
     Parameters: mark
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void hold(int mark) {
        placemarker = mark;
        Button cont = (Button) findViewById(R.id.continuebutton);
        cont.setVisibility(View.VISIBLE);
        cont.setOnClickListener(holdlistener);
    }

    private View.OnClickListener holdlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boardview.clearText();
            Button cont = (Button) findViewById(R.id.continuebutton);
            cont.setVisibility(View.GONE);
            if ( !board.isGameWon() ) {
                switch (placemarker) {
                    case 1:
                        startTurn();
                        break;
                    case 2:
                        switchTurn();
                        savePrompt();
                        break;
                }
            } else {
                endGame();
            }
        }
    };

    /*********************************************************************
     Function Name: setCurrentTurn
     Purpose: set whose turn it is
     Parameters: currentTurn, string, the player whose turn it is
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void setCurrentTurn(String currentTurn) {
        this.currentturn = currentTurn;
    }

    /*********************************************************************
     Function Name: setFirstMove
     Purpose: set who went first
     Parameters: firstmove, string, the player who goes first
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void setFirstMove(String firstmove) {
        this.firstmove = firstmove;
    }

    /*********************************************************************
     Function Name: getCurrentTurn
     Purpose: get whose turn it is
     Parameters: none
     Return Value: String, whose turn it is
     Local Variables:
     Algorithm:
     Assistance Received:none
     *********************************************************************/
    public String getCurrentTurn() {
        return currentturn;
    }

    /*********************************************************************
     Function Name: getFirstMove
     Purpose: get who went first
     Parameters: none
     Return Value: string, player who went first
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    public String getFirstMove() {
        return firstmove;
    }

    /*********************************************************************
     Function Name: getSpinnerList
     Purpose: find available squares
     Parameters:
        side, boolean array, the board
        coverself, boolean, whether to cover
     Return Value: Integer array, the available squares
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private Integer[] getSpinnerList(boolean[] side, boolean coverself) {
        Queue<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < side.length; i++) {
            if (side[i] != coverself) {
                queue.add(i+1);
            }
        }
        Integer[] list = new Integer[queue.size() + 1];
        list[0] = 0;
        int count = queue.size();
        for (int j = 1; j <= count; j++) {
            list[j] = queue.poll();
        }
        return list;
    }

    private Spinner.OnItemSelectedListener testinputs = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            Spinner choice1 = (Spinner) findViewById(R.id.boxchoice1);
            Spinner choice2 = (Spinner) findViewById(R.id.boxchoice2);
            Spinner choice3 = (Spinner) findViewById(R.id.boxchoice3);
            Spinner choice4 = (Spinner) findViewById(R.id.boxchoice4);
            int a = (int) choice1.getSelectedItem();
            int b = (int) choice2.getSelectedItem();
            int c = (int) choice3.getSelectedItem();
            int d = (int) choice4.getSelectedItem();
            // check if squares add up to the roll and are not the same
            int[] squares = new int[]{a, b, c, d};
            if (a + b + c + d == roll) {
                boolean sameflag = false;
                for (int i = 0; i < squares.length; i++) {
                    for (int j = i + 1; j < squares.length; j++) {
                        if (squares[i] == squares[j] && squares[i] != 0) {
                            sameflag = true;
                        }
                    }
                }
                if (sameflag) {
                    findViewById(R.id.play).setVisibility(View.GONE);
                    human.setMoveMade(null);
                } else {
                    Button play = (Button) findViewById(R.id.play);
                    play.setVisibility(View.VISIBLE);
                    human.setMoveMade(squares);
                    play.setOnClickListener(playsquares);
                }
            } else {
                findViewById(R.id.play).setVisibility(View.GONE);
                human.setMoveMade(null);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // function not used
        }
    };

    private View.OnClickListener playsquares = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            board.updateBoard(human.getMoveMade(), getCurrentTurn(), human.getCoverSelf());
            boardview.updateBoard(board);
            findViewById(R.id.boxchoicewidgets).setVisibility(View.GONE);
            findViewById(R.id.helpbutton).setVisibility(View.GONE);
            if (!board.isGameWon()) {
                startTurn();
            } else {
                endGame();
            }
        }
    };
}



