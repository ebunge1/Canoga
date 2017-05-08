package com.evanbunge.canoga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;


public class Tournament extends AppCompatActivity implements View.OnClickListener
{

    private static final int GAME_CODE = 0;
    private int humanscore;
    private int comscore;
    private int nexthandicap;
    private String handicappedplayer;
    private int[] dicerolls;

    /*********************************************************************
     Function Name:onCreate
     Purpose: set up the tournament
     Parameters: savedInstanceState, Bundle
     Return Value: void
     Local Variables:
        intent, Intent that may hold a filename
        isserialized, boolean whether or not to load a game
        startgame, Intent to start the game
        file, the file the load
        filescanner, Scanner to read file
        line, String to hold each line read from the file
        tokens, string array to hold each word in line
     Algorithm:
        check if serialized
            read in game info from file
        start game
     Assistance Received: none
     *********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        humanscore = 0;
        comscore = 0;

        boolean isserialized = intent.getBooleanExtra("serialized", false);
        Intent startgame = new Intent(this, Game.class);
        startgame.putExtras(intent.getExtras());
        if (isserialized) {
            //serialization
            File file = getExternalFilesDir( startgame.getStringExtra("filename") + ".txt" );
            try {
                Scanner filescanner = new Scanner(file);
                String line = null;
                //check for end of file
                while( filescanner.hasNextLine() )
                {
                    line = filescanner.nextLine();
                    String[] tokens = line.split("\\s+");
                    //check first word in line
                    if (Objects.equals(tokens[0], "Computer:")) {
                        line = filescanner.nextLine();
                        tokens = line.split("\\s+");
                        boolean[] comboard = new boolean[tokens.length-2];
                        for (int i = 2; i < tokens.length; i++) {
                            if (Objects.equals(tokens[i], "*")) {
                                comboard[i-2] = true;
                            } else {
                                comboard[i-2] = false;
                            }
                        }
                        startgame.putExtra("comboard", comboard);
                        line = filescanner.nextLine();
                        tokens = line.split("\\s+");
                        setComscore( Integer.parseInt(tokens[2]) );
                    }
                    if (Objects.equals(tokens[0], "Human:")) {
                        line = filescanner.nextLine();
                        tokens = line.split("\\s+");
                        boolean[] humanboard = new boolean[tokens.length-2];
                        for (int i = 2; i < tokens.length; i++) {
                            if (Objects.equals(tokens[i], "*")) {
                                humanboard[i-2] = true;
                            } else {
                                humanboard[i-2] = false;
                            }
                        }
                        startgame.putExtra("humanboard", humanboard);
                        line = filescanner.nextLine();
                        tokens = line.split("\\s+");
                        setHumanscore(Integer.parseInt(tokens[2]));
                    }
                    if (tokens[0].equals("First")) {
                        startgame.putExtra("firstmove", tokens[2]);
                    }
                    if (tokens[0].equals("Next")) {
                        startgame.putExtra("currentturn", tokens[2]);
                    }
                    if (tokens[0].equals("Dice:")) {
                        Queue<Integer> dicerolls = new LinkedList<Integer>();
                        int roll;
                        while ( filescanner.hasNext() ) {
                            roll = Integer.valueOf( filescanner.next() );
                            dicerolls.add(roll);
                        }
                        int[] serialized_dice = new int[dicerolls.size()];
                        for (int i = 0; i < serialized_dice.length; i++) {
                            serialized_dice[i] = dicerolls.poll();
                        }
                        startgame.putExtra("dicerolls", serialized_dice);
                    }
                }
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "File read failed: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        startActivityForResult(startgame, GAME_CODE);
    }

    /*********************************************************************
     Function Name: onActivityResult
     Purpose: start new game or save to file
     Parameters:
     Return Value: void
     Local Variables:
     Algorithm:
        check if serializing
            save to file
        update and display score
        set handicap
        set buttons for input
     Assistance Received:none
     *********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if (requestCode == GAME_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                //declare winner
                boolean serialized = data.getBooleanExtra("serialized", false);
                if (!serialized) {
                    setContentView(R.layout.gameresults);
                    TextView winnertext = (TextView) findViewById(R.id.winner);
                    String winner = data.getStringExtra("winner");
                    dicerolls = data.getIntArrayExtra("dicerolls");
                    winnertext.setText(winner);
                    int points = data.getIntExtra("points", 0);
                    updateScore(winner, points);
                    displayScores(winner, points);

                    //set handicap variables for next game
                    String firstmove = data.getStringExtra("firstmove");
                    setHandicap(points);
                    setHandicappedPlayer(winner, firstmove);

                    //create and wait for button press for new game
                    Button nextgame = (Button) findViewById(R.id.nextgamebutton);
                    Button endtourn = (Button) findViewById(R.id.endtournbutton);
                    nextgame.setOnClickListener(this);
                    endtourn.setOnClickListener(this);
                    //check if serializing last game
                } else {
                    setContentView(R.layout.tournresults);
                    findViewById(R.id.tournresult).setVisibility(View.GONE);
                    findViewById(R.id.savewidgets).setVisibility(View.VISIBLE);
                    findViewById(R.id.savefile).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText fileinput = (EditText) findViewById(R.id.fileinput);
                            String filename = fileinput.getText().toString();
                            boolean humanside[] = data.getBooleanArrayExtra("humanside");
                            boolean comside[] = data.getBooleanArrayExtra("comside");
                            try {
                                File file = new File(getExternalFilesDir(null), filename + ".txt");
                                FileOutputStream stream = new FileOutputStream(file);
                                OutputStreamWriter filewriter = new OutputStreamWriter(stream);
                                filewriter.write("Computer:\n\tSquares:");
                                for (int i = 0; i < comside.length; i++) {
                                    if (comside[i] == true) {
                                        filewriter.write(" *");
                                    } else {
                                        filewriter.write( " " + String.valueOf(i+1) );
                                    }
                                }
                                filewriter.write("\n\tScore: " + String.valueOf(humanscore));
                                filewriter.write("\n\nHuman:\n\tSquares:");
                                for (int j = 0; j < humanside.length; j++) {
                                    if (humanside[j] == true) {
                                        filewriter.write(" *");
                                    } else {
                                        filewriter.write( " " + String.valueOf(j+1) );
                                    }
                                }
                                filewriter.write( "\n\tScore: " + String.valueOf(comscore) );
                                filewriter.write("\nFirst Turn: " + data.getStringExtra("firstmove") );
                                filewriter.write("\nNext Turn: " + data.getStringExtra("currentturn") );
                                filewriter.close();
                                Toast.makeText(getBaseContext(), "File saved", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), "File write failed: " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                            finish();
                        }
                    });
                }
            }
        }
    }

    /*********************************************************************
     Function Name: displayScores
     Purpose: display scores on screen
     Parameters:
        winner, string of the winner of the game
        points, int, points won by winner
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received:none
     *********************************************************************/
    private void displayScores(String winner, int points) {
        TextView human = (TextView) findViewById(R.id.humanscore);
        TextView computer = (TextView) findViewById(R.id.comscore);
        human.setText( String.valueOf(humanscore) );
        computer.setText( String.valueOf(comscore) );
        if (winner.equals("Human")) {
            human.append("  (+" + String.valueOf(points) + ")");
        } else {
            computer.append("  (+" + String.valueOf(points) + ")");
        }
    }

    /*********************************************************************
     Function Name:updateScore
     Purpose: update the score based on the previous game
     Parameters:
         winner, string of the winner of the game
         points, int, points won by winner
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void updateScore(String winner, int points) {
        if (winner.equals("Human")) {
            setHumanscore( getHumanscore() + points );
        } else {
            setComscore( getComscore() + points );
        }
    }

    /*********************************************************************
     Function Name: onClick
     Purpose: start new game or display results of the tournament
     Parameters: View
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.nextgamebutton:
                Intent startgame = new Intent(this, Game.class);
                startgame.putExtra("handicappedplayer", handicappedplayer);
                startgame.putExtra("handicap", nexthandicap);
                startgame.putExtra("serialized", false);
                startgame.putExtra("dicerolls", dicerolls);
                startActivityForResult(startgame, GAME_CODE);
                break;
            case R.id.endtournbutton:
                String tournwinner = getTournWinner();
                setContentView(R.layout.tournresults);
                TextView winnertext = (TextView) findViewById(R.id.tournwinner);
                winnertext.setText(tournwinner);
                TextView human = (TextView) findViewById(R.id.humanresults);
                TextView computer = (TextView) findViewById(R.id.comresults);
                human.append(String.valueOf(humanscore));
                computer.append(String.valueOf(comscore));
                break;
        }
    }

    /*********************************************************************
     Function Name: getTournWinner
     Purpose: get the winner of the tournament
     Parameters: none
     Return Value: String, the winner
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private String getTournWinner() {
        if (comscore > humanscore)
        {
            return "Computer";
        }
        else if (humanscore > comscore)
        {
            return "Human";
        }
        else {
            return "Nobody. It's a draw.";
        }

    }

    /*********************************************************************
     Function Name: setHandicap
     Purpose: record the handicapped square
     Parameters: points, int, the points won last game
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void setHandicap(int points)
    {
        int temp = points;
        int handicap;
        do {
            //get one's digit
            handicap = temp % 10;
            //add ten's digit
            handicap += (temp - handicap) / 10;
            temp = handicap;
        } while (handicap > 9);
        nexthandicap = handicap;
    }

    /*********************************************************************
     Function Name: setHandicappedPlayer
     Purpose: record the player who gets the handicap
     Parameters:
        winner, string, the winner
        firstmove, string, who went first
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    private void setHandicappedPlayer(String winner, String firstmove)
    {
        if (!winner.equals(firstmove)) {
            // winning player did not get first move. gets handicap
            handicappedplayer = winner;
        }
        else
        {
            // winning player had first move, opponent gets handicap
            if (winner.equals("Human")) {
                handicappedplayer = "Computer";
            }
            else
            {
                handicappedplayer = "Human";
            }
        }
    }

    public void setHumanscore(int humanscore) {
        this.humanscore = humanscore;
    }

    public int getHumanscore() {
        return humanscore;
    }

    public void setComscore(int comscore) {
        this.comscore = comscore;
    }

    public int getComscore() {
        return comscore;
    }
}
