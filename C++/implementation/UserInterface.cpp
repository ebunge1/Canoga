#include "UserInterface.h"

UserInterface::UserInterface()
{
}

/*************************
 Function Name: askSize
 Purpose: Prompts user for size of board
 Parameters: None
 Return Value: an int 9-11, the size of this game's board
 Local Variables: boardsize
 Algorithm:
            intitialize temporary variable
            prompt user for size in loop
            validates entry for int 9-11
            return user input
 Assistance Recieved: None
 *************************/
int UserInterface::askSize()
{
    int boardsize;
    do {
        cout << "Enter size of board to use(9-11): ";
        cin >> boardsize;
        if (!cin)
        {
            cin.clear();
            cin.ignore(1000, '\n');
            continue;
        }
    } while (boardsize < 9 || boardsize > 11);
    return boardsize;
}

/*************************
 Function Name: playersRoll
 Purpose: explain what's about to happen in the game
 Parameters: None
 Return Value: None
 Local Variables: None
 Algorithm:
            tell the user the players will roll to decide whose first
 Assistance Recieved: None
 *************************/
void UserInterface::playersRoll()
{
    cout << "\nPlayers roll to decide first move.\n";
}

/*************************
 Function Name: rollResult
 Purpose: display the result of a dice roll
 Parameters: 
            string, the player rolling
            firstroll, their first die
            secondroll, their second die
 Return Value: int, the combined sum of the two dice
 Local Variables: None
 Algorithm:
            tell the user the two dice amounts and their sum
            return the sum
 Assistance Recieved: None
 *************************/
int UserInterface::rollResult(string player, int firstroll, int secondroll)
{
    cout << player <<" rolls a " << firstroll << " and a " << secondroll << " for a combined roll of " << firstroll + secondroll << ".\n";
    return firstroll + secondroll;
}

/*************************
 Function Name: rollResult
 Purpose: a version of rollResult with one die thrown
 Parameters: 
            string, the player rolling
            int, the roll
 Return Value: int, the roll of one die
 Local Variables: None
 Algorithm:
            tell user the roll
            return the roll
 Assistance Recieved: None
 *************************/
int UserInterface::rollResult(string player, int firstroll)
{
    cout << player << "rolls a " << firstroll << ".";
    return firstroll;
}

int UserInterface::howManyDice()
{
    int temp;
    do
    {
        cout << "Would you like to throw '1' or '2' dice? ";
        cin >> temp;
        if (!cin)
        {
            cin.clear();
            cin.ignore(1000, '\n');
            continue;
        }
    } while (temp > 2 || temp < 1);
    return temp;
}

bool UserInterface::willCover( bool can_cover, bool can_uncover )
{
    if ( !can_cover )
    {
        cout << "No move available to cover. Must uncover opponent's squares.\n";
        return false;
    }
    if ( !can_uncover )
    {
        cout << "No move available to uncover. Must cover your own squares.\n";
        return true;
    }
    string temp;
    do {
        cout << "Would you like to 'cover' your squares or 'uncover' your opponent's? ";
        cin >> temp;
    } while (temp != "cover" && temp != "uncover");
    if (temp == "cover")
    {
        return true;
    }
    return false;
}

void UserInterface::declareGameWon( string winner, int points )
{
    cout << "\nThe " << winner << " is the winner of this game and scores " << points << " points.\n";
}

void UserInterface::declareTournWon( string winner )
{
    cout << endl << winner << " is the new champion of Canoga.\n";
}

void UserInterface::noMoves()
{
    cout << "There are no moves available. End of Turn.\n";
}

bool UserInterface::newGame(){
    string input;
    do
    {
        cout << "Would you like to play another game? ";
        cin >> input;
        if (input == "yes")
        {
            return true;
        }
        if (input == "no")
        {
            return false;
        }
    } while (1);
}

vector<int> UserInterface::pickSquares(vector<bool> side, bool unavailable)
{
    vector<int> squares(4,0);
    int input;
    int count = 0;
    cout << "Enter the squares you would like to play(0 to stop):\n";
    while (count <= 4)
    {
        cin >> input;
        if (!cin)
        {
            cin.clear();
            cin.ignore(1000, '\n');
            cout << "Enter square number '1' through '" << side.size() << "'.\n";
            continue;
        }
        if ( input > 0 && input <= side.size() )
        {
            if (side[input-1] == unavailable)
            {
                cout << "Square is unavailable. Please reselect.\n";
            }
            else
            {
                bool uniqueflag = true;
                for (int i = 0; i < 4; i++) {
                    if ( input == squares[i]) {
                        uniqueflag = false;
                        cout << "Cannot the same square twice.\n";
                    }
                }
                if (uniqueflag)
                {
                    squares[count++] = input;
                }
            }
        }
        else if (input == 0)
        {
            break;
        }
        else
        {
            cout << "Enter square number '1' through '" << side.size() << "'.\n";
        }
    }
    return squares;
}

bool UserInterface::askToSave()
{
    string input;
    do
    {
        cout << "Would you like to save and quit the game? ";
        cin >> input;
        if (input == "yes")
        {
            return true;
        }
        if (input == "no")
        {
            return false;
        }
    } while (1);
}

bool UserInterface::validatesquares(vector<int> squares, int roll)
{
    int sum = squares[0] + squares[1] + squares[2] + squares[3];
    if (sum != roll)
    {
        cout << "The sum of the squares must equal your roll.\n";
        return false;
    }
    else
    {
        return true;
    }
}

void UserInterface::declareTurn(string player)
{
    cout << endl << player << "'s turn.\n";
}

void UserInterface::computermove(vector<int> squares)
{
    cout << "The computer chose squares: ";
    for (int i = 0; i < 4; i++)
    {
        if (squares[i] != 0)
        {
            cout << squares[i] << " ";
        }
    }
    cout << endl;
}

string UserInterface::newOrLoad()
{
    string choice;
    do {
        cout << "Would you like to start a 'new' game or 'load' from file? ";
        cin >> choice;
    }while (choice != "new" && choice != "load");
    if (choice == "new") {
        return "";
    }
    else
    {
        string filename;
        cout << "Which file would you like to load? ";
        cin >> filename;
        return filename;
    }
}