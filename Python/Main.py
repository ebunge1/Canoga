from UserInterface import UserInterface
from Tournament import Tournament
import random
import time

random.seed(time.time())
filename = UserInterface.newOrLoad()
Tourn = Tournament()
Tourn.playTournament(filename)
