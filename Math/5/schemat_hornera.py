# Autorzy: Julia Nocuń, Kevin Makarewicz

def schemat_hornera(wspolczynniki, x):      #funkcja wykorzystywana do oblicznia wartości wielomianów
    wynik = 0
    for i in (wspolczynniki):    
        wynik = wynik * x + i
    return wynik  