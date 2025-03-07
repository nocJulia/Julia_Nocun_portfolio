# Autorzy: Julia Nocuń, Kevin Makarewicz

def silnia(liczba):     #rekurencyjne wyznaczanie silni
    
    s = liczba
    if liczba > 1:
        return s*silnia(s-1)
    elif liczba == 1 or liczba == 0:
        return 1
    else:
        print("Podano złą wartość!")
        return None