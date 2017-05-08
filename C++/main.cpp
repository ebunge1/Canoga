
#include "Tournament.h"
#include "UserInterface.h"
#include <stdlib.h>
#include <ctime>
#include <fstream>
#include <iostream>


int main()
{
    srand( static_cast<unsigned int>( time(NULL) ) );
    string file = UserInterface::newOrLoad();
    Tournament Tourn;
    Tourn.playTournament(file);
}
