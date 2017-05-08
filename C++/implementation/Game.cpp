#include "Game.h"
#include "Turn.h"
#include "BoardView.h"
#include "UserInterface.h"
#include <iostream>
#include <fstream>
#include <sstream>
using namespace std;

/*************************
 Function Name: Game
 Purpose: Class default constructor. Initialize member variables.
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm: Initialize strings to "N/A"
 Assistance Recieved: None
 *************************/
Game::Game()
{
    firstmove = "N/A";
    currentturn = "N/A";
}

/*************************
 Function Name: runGame
 Purpose: execute a game
 Parameters: 
            Handicap, a pointer to a Handicap structure
            Tourn, Tournament object used to access scores when saving
            filename, string of file to load
 Return Value: 
            Winner, winner structure containing neccessary information about the game's outcome
 Local Variables: 
            Board, the game's board
            temp, a temporary board used to initialize a board if no gameboard is being loaded
            points, an int to hold the points earned by the winner of a game
 Algorithm:
            check if file needs to be loaded
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
 *************************/
Winner Game::runGame(Handicap* Handicap, Tournament Tourn, string filename)
{
    Board Board;
    //check if file needs to be loaded
    if (filename == "") {
        //new game, get size of board from user and create the board
        class Board temp( UserInterface::askSize(), Handicap );
        Board = temp;
        //decide who goes first
        rollForFirst();
    }
    else
    {
        //game needs to be loaded
        Board = copyFromFile(filename);
        if (Board.isGameWon()) {
            //game was won
            BoardView::drawBoard( Board );
            //set current turn to the winner
            switchTurn(Board);
            int points = calcScore( Board );
            UserInterface::declareGameWon( getCurrentTurn(), points );
            return Winner( getCurrentTurn(), getFirstMove(), points );
        }
    }
    BoardView::drawBoard( Board );
    do
    {
        Turn::startTurn( Board, getCurrentTurn() );
        if ( UserInterface::askToSave() )
        {
            //save game
            //set the current turn to the next player to go
            switchTurn(Board);
            saveGame(Board, Tourn);
            exit(0);
        }
        if ( !Board.isGameWon() )
        {
            //game not won
            switchTurn(Board);
            BoardView::drawBoard( Board );
        }
    } while ( !Board.isGameWon() );
    //game won
    int points = calcScore( Board );
    UserInterface::declareGameWon( getCurrentTurn(), points );
    return Winner( getCurrentTurn(), getFirstMove(), points );
}

/*************************
 Function Name: getFirstMove
 Purpose: gets which player went first;
 Parameters: None
 Return Value: a string, the player that went first
 Local Variables: None
 Algorithm:
            return the member variable
 Assistance Recieved: None
 *************************/
string Game::getFirstMove() const
{
    return firstmove;
}

/*************************
 Function Name: getCurrentTurn
 Purpose: get whose turn it is
 Parameters: None
 Return Value: a string, the player whose turn it is
 Local Variables: None
 Algorithm:
            return the member variable
 Assistance Recieved: None
 *************************/
string Game::getCurrentTurn() const
{
    return currentturn;
}

/*************************
 Function Name: setFirstMove
 Purpose: changes value of variable firstmove
 Parameters: string, who goes first
 Return Value: None
 Local Variables:
 Algorithm:
 set firstmove to the passed string
 Assistance Recieved: None
 *************************/
void Game::setFirstMove( string x )
{
    firstmove = x;
}

/*************************
 Function Name: setCurrentTurn
 Purpose: changes value of variable currentturn
 Parameters: string, whose turn it is
 Return Value: None
 Local Variables:
 Algorithm:
 set currentturn to the passed string
 Assistance Recieved: None
 *************************/
void Game::setCurrentTurn( string x )
{
    currentturn = x;
}

/*************************
 Function Name: rollForFirst
 Purpose: roll dice to decide who has first move
 Parameters: None
 Return Value: None
 Local Variables:
                humanroll, an int to hold human's total dice roll
                comroll, an int to hold com's total dice roll
 Algorithm:
            tell user players will roll to decide whose first
            enter loop
            roll dice until one is higher
            set the first player
 Assistance Recieved: None
 *************************/
void Game::rollForFirst()
{
    UserInterface::playersRoll();
    int humanroll;
    int comroll;
    do
    {
        humanroll = UserInterface::rollResult("Human", Game::rollDie(), Game::rollDie());
        comroll = UserInterface::rollResult("Computer", Game::rollDie(), Game::rollDie());
    } while ( humanroll == comroll );
    setFirstMove( decideFirst(humanroll, comroll) );
    setCurrentTurn( getFirstMove() );
}

/*************************
 Function Name: decideFirst
 Purpose: decides which player goes first each game
 Parameters: 
            humanroll, an int to hold human's total dice roll
            comroll, an int to hold com's total dice roll
 Return Value: string, the player with the higher roll
 Local Variables: None
 Algorithm:
            compare rolls and assign firstmove corresponding value
            return player with higher roll
 Assistance Recieved: None
 *************************/
string Game::decideFirst(int humanroll, int comroll)
{
    if ( humanroll > comroll )
    {
        cout << "Human will go first.\n";
        return "Human";
    }
    else if ( comroll > humanroll )
    {
        cout << "Computer will go first.\n";
        return "Computer";
    }
    return "";
}

/*************************
 Function Name: rollDie
 Purpose: emulate the rolling of a die
 Parameters: None
 Return Value: an int 1-6, the roll of a die
 Local Variables: None
 Algorithm:
 create random number between 1-6
 return that value
 Assistance Recieved: None
 *************************/
int Game::rollDie()
{
    /* use this for testing
    int roll;
    cout << "Enter roll: ";
    cin >> roll;
    return roll;
     */
    
    return (rand() % 6) + 1;
}

/*************************
 Function Name: switchTurn
 Purpose: switch the current turn to the next player
 Parameters:
            Board, passed by reference to set the turnflag
 Return Value: void
 Local Variables: None
 Algorithm:
 Assistance Recieved: None
 *************************/
void Game::switchTurn(Board &Board)
{
    if ( getCurrentTurn() != getFirstMove() ) {
        Board.setTurnFlag();
    }
    if ( getCurrentTurn() == "Human" )
    {
        setCurrentTurn("Computer");
    }
    else
    {
        setCurrentTurn("Human");
    }
}

/*************************
 Function Name: saveGame
 Purpose: save the game
 Parameters:
 Return Value:
 Local Variables:
 Algorithm:
 Assistance Recieved: None
 *************************/
void Game::saveGame(Board Board, Tournament Tourn)
{
    string filename;
    cout << "Name the save file: ";
    cin >> filename;
    filename += ".txt";
    
    ofstream file;
    file.open(filename);
    file << "Computer: \n\tSquares: ";
    for (int i = 0; i < Board.getBoardSize(); i++) {
        if ( Board.testSquare( i , Board.getComBoard(), true ) )
        {
            file << "* ";
        }
        else
        {
            file << i+1 << " ";
        }
    }
    file << "\n\tScore: " << Tourn.getComScore();
    file << "\n\nHuman: \n\tSquares: ";
    for (int i = 0; i < Board.getBoardSize(); i++) {
        if ( Board.testSquare( i , Board.getHumanBoard(), true ) )
        {
            file << "* ";
        }
        else
        {
            file << i+1 << " ";
        }
    }
    file << "\n\tScore: " << Tourn.getHumanScore();
    file << "\n\nFirst Turn: " << getFirstMove();
    file << "\nNext Turn: " << getCurrentTurn();
    file.close();
    cout << "Game saved as " << filename << endl;
}

int Game::calcScore(Board Board)
{
    bool test = Board.getComBoard()[0];
    for (int i = 1; i < Board.getBoardSize(); i++) {
        if ( Board.getComBoard()[i] != test )
        {
            return addsquares( Board.getComBoard(), Board.getBoardSize(), !Board.getHumanBoard()[0] );
        }
    }
    return addsquares( Board.getHumanBoard(), Board.getBoardSize(), !test );
}

int Game::addsquares(vector<bool> board, int size, bool covered )
{
    int sum = 0;
    for (int i = 0; i < size; i++) {
        if ( board[i] == covered )
        {
            sum += i + 1;
        }
    }
    return sum;
}

Board Game::copyFromFile(string filename)
{
    int size = 0;
    vector<int> comsquares;
    vector<int> humansquares;
    string firstmove;
    string currentturn;
    string input;
    ifstream file;
    file.open(filename);
    if (file.is_open())
    {
        string line;
        while ( getline(file, line) )
        {
            if ( line.find("Computer:", 0) != string::npos )
            {
                getline(file, line);
                istringstream ss(line);
                ss >> input;
                int square = 1;
                while(ss >> input)
                {
                    if (input == "*") {
                        comsquares.push_back(square);
                    }
                    size++;
                    square++;
                }
            }
            if ( line.find("Human:", 0) != string::npos )
            {
                getline(file, line);
                istringstream ss(line);
                ss >> input;
                int square = 1;
                while(ss >> input)
                {
                    if (input == "*") {
                        humansquares.push_back(square);
                    }
                    square++;
                }
            }
            if ( line.find("First ", 0) != string::npos )
            {
                istringstream ss(line);
                ss >> firstmove;
                ss >> firstmove;
                ss >> firstmove;
            }
            if ( line.find("Next ", 0) != string::npos )
            {
                istringstream ss(line);
                ss >> currentturn;
                ss >> currentturn;
                ss >> currentturn;
            }
        }
    }
    Board Board( size );
    Board.updateBoard(comsquares, "Computer", true);
    Board.updateBoard(humansquares, "Human", true);
    Board.setTurnFlag();
    setFirstMove(firstmove);
    setCurrentTurn(currentturn);
    return Board;
}


