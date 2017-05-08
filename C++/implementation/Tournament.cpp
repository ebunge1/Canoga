#include "Tournament.h"
#include "Game.h"
#include "BoardView.h"
#include "UserInterface.h"
#include <fstream>
#include <sstream>

/*************************
 Function Name: Tournament
 Purpose: Class default constructor. Initialize member variables.
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
*************************/
Tournament::Tournament()
{
    humanscore = 0;
    comscore = 0;
}

/*************************
 Function Name: playTournament
 Purpose: executes a tournnament
 Parameters: 
            filename, a string to hold the filename of the game to load
 Return Value: Void
 Local Variables:
            empty, a Handicap structure to be used as a placeholder for the first game (which uses no handicap)
            Game, a Game object. initializes each game
            Handicap, Handicap pointer that allows for easy reassigning of handicaps each new game and holds value after iterations of loop
            Winner, a Winner structure that recieves the results of each game
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
 *************************/
void Tournament::playTournament(string filename)
{
    // empty filename implies no file is to be loaded
    if (filename != "")
    {
        copyFromFile(filename);
    }
    // create a handicap pointer an empty handicap to point to
    // this is needed in order to properly update the handicap each game without losing information after each loop
    Handicap empty;
    Handicap* Handicap = new class Handicap( empty );
    do
    {
        Game Game;
        // structure to hold the winner and score of the game
        Winner Winner = Game.runGame(Handicap, *this, filename);
        updateScore(Winner);
        // calculate handicap from game's winner
        Handicap = new class Handicap( calcHandicap( Winner ) );
        BoardView::displayScore(getHumanScore(), getComScore());
        // clear filename to prevent load errors in new game
        filename = "";
    } while ( UserInterface::newGame() );
    delete Handicap;
    Handicap = nullptr;
    UserInterface::declareTournWon( final( getHumanScore(), getComScore() ) );
}

/*************************
 Function Name: calcHandicap
 Purpose: calculates handicap for next game
 Parameters:
            Winner, a winner structure to provide who went first, the winner, and points earned
 Return Value:
            Handicap structure containing the player and square
 Local Variables:
            temp, an int
 Algorithm:
            decide who gets the handicap
            calculate handicap
                add each digit of points earned until the result is a single digit
 Assistance Recieved: None
 *************************/
Handicap Tournament::calcHandicap(Winner Winner)
{
    Handicap Handicap;
    if (Winner.player != Winner.firstmove) {
        // winning player did not get first move. gets handicap
        Handicap.player = Winner.player;
    }
    else
    {
        // winning player had first move, opponent gets handicap
        if (Winner.player == "Human") {
            Handicap.player = "Computer";
        }
        else
        {
            Handicap.player = "Human";
        }
    }
    int temp = Winner.points;
    do{
        //get one's digit
        Handicap.square = temp % 10;
        //add ten's digit
        Handicap.square += (temp - Handicap.square) / 10;
        temp = Handicap.square;
    } while (Handicap.square > 9);
    return Handicap;
}

/*************************
 Function Name: final
 Purpose: gets the winner of the tournament
 Parameters:
            humanscore, an int holding the human's score
            comscore, an int holding the computer's score
 Return Value:
            a string containing the winner or declaring a draw
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
string Tournament::final(int humanscore, int comscore)
{
    if (humanscore > comscore) {
        return "The Human";
    }
    else if (comscore > humanscore )
    {
        return "The Computer";
    }
    else
    {
        return "It's a draw. Neither player ";
    }
}

/*************************
 Function Name: getComScore
 Purpose: get the computer's score
 Parameters: None
 Return Value: 
            an int containing the score
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
int Tournament::getComScore() const
{
    return comscore;
}

/*************************
 Function Name: getHumanScore
 Purpose: get the human's score
 Parameters: None
 Return Value:
            an int containing the score
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
int Tournament::getHumanScore() const
{
    return humanscore;
}

/*************************
 Function Name: updateScore
 Purpose: update the score of last game's winner
 Parameters:
            Winner, a winner structure
 Return Value: Void
 Local Variables: None
 Algorithm:
            Add the points earned to the winners overall score
 Assistance Recieved: None
 *************************/
void Tournament::updateScore(Winner Winner)
{
    if (Winner.player == "Human")
    {
        humanscore += Winner.points;
    }
    else
    {
        comscore += Winner.points;
    }
}

/*************************
 Function Name: copyFromFile
 Purpose: load the scores of each player from a file
 Parameters:
            filename, string containing the filename to read from
 Return Value: void
 Local Variables:
            file, the file stream to read
            line, string to hold each line as it's read
            ss, a stringstream to read each entry per line
            input, string to hold each entry from the stringstream
 Algorithm:
            open file
            check of file is open, close if file not found
            read each line
                look for the computer's score or human's score
                read in score as string
                convert score to int and update member variable
 Assistance Recieved: None
 *************************/
void Tournament::copyFromFile(string filename)
{
    ifstream file;
    file.open(filename);
    //check if file is open
    if (file.is_open()) {
        string line;
        //read in each line
        while ( getline(file, line) )
        {
            //search for computer's score
            if ( line.find("Computer:", 0) != string::npos )
            {
                //skip line for computer's board
                getline(file, line);
                getline(file, line);
                if ( line.find("Score:", 0) != string::npos )
                {
                    string input;
                    istringstream ss(line);
                    //read in score from line skipping the word "Score:"
                    ss >> input;
                    ss >> input;
                    //convert to integer
                    comscore = stoi(input);
                }
            }
            //search for human's score
            if ( line.find("Human:", 0) != string::npos )
            {
                //skip line for human's board
                getline(file, line);
                getline(file, line);
                if ( line.find("Score:", 0) != string::npos )
                {
                    string input;
                    istringstream ss(line);
                    //read in score from line skipping the word "Score:"
                    ss >> input;
                    ss >> input;
                    //convert to integer
                    humanscore = stoi(input);
                }
                
            }
        }
    }
    else
    {
        //exit if found could not open
        cout << "Could not open file.\n";
        exit(1);
    }
}
