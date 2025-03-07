#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

import board

#ilość wierszy i kolumn układanki przechowujemy w osobnym pliku 
#(w postaci zmiennych globalnych), tak aby zapewnić uniwersalność programu, 
#dla róznych wielkości układnek, nawet takich, które nie będą miały tej samej ilości wierszy i kolumn.
#przechowywanie tych zmiennych w osobnym pliku pozwala nam również uniknąć cyklicznych importów,
#które mogą prowadzić do błędów w programie 

ROWS = 0
COLUMNS = 0

SOLVED_BOARD = board.Board

