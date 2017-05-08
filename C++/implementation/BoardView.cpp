#include "BoardView.h"
#include "Board.h"
#include <iomanip>
#include <iostream>
#include <string>

BoardView::BoardView()
{
}

void BoardView::drawBoard( Board Board )
{
    cout << "**************************************************************\n";
    cout << "--------------------------------------------------------\n";
    cout << "| ";
    for (int i = 0; i < Board.getBoardSize(); i++) {
        if (Board.getComBoard()[i])
        {
            cout << "X";
        }
        else
        {
            cout << i+1;
        }
        cout << " | ";
    }
    cout << "Computer";
    cout << "\n--------------------------------------------------------\n";
    cout << "\n--------------------------------------------------------\n";
    cout << "| ";
    for (int i = 0; i < Board.getBoardSize(); i++) {
        if (Board.getHumanBoard()[i])
        {
            cout << "X";
        }
        else
        {
            cout << i+1;
        }
        cout << " | ";
    }
    cout << "Human";
    cout << "\n--------------------------------------------------------\n";
    //cout << "\n";
}

void BoardView::displayScore(int humanscore, int comscore)
{
    cout << "Computer Score: " << comscore;
    cout << "          Human Score: " << humanscore;
    cout << "\n\n";
}