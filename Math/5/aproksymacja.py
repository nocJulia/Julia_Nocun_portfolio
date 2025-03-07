# #Autorzy: Kevin Makarewicz, Julia Nocuń

from wykorzystane_funkcje import wartosci_wykorzystanych_funkcji
from math import cos, pi

#funkcja do obliczania wielomianów Czebyszewa
def wielomian_czebyszewa(x, stopien):
    if stopien == 0:  #gdy stopień wynosi 0, zwróć wartość 1
        return 1
    elif stopien == 1:  #gdy stopień wynosi 1, zwróć zmienną x
        return x
    else:  #w przypadku większego stopnia wielomianu obliczaj kolejne wartości wielomianów Czebyszewa
        tkm1 = x  #ustawiamy początkową wartość dla stopnia 1
        tkm2 = 1  #ustawiamy początkową wartość dla stopnia 0
        for i in range(2, stopien + 1):  #pętla dla kolejnych stopni wielomianu
            tk = 2 * x * tkm1 - tkm2  #obliczamy wartość dla aktualnego stopnia
            tkm2 = tkm1  
            tkm1 = tk  
        return tkm1

#funkcja do obliczania współczynników aproksymacji
def wspolczynniki(spr, Ywezly, stopien):
    wspol = []  #inicjalizacja listy na współczynniki
    for i in range(stopien + 1):  
        suma1 = 0  #suma iloczynów wartości węzłów i wielomianów
        suma2 = 0  #suma kwadratów wartości wielomianów
        for k in range(len(spr)):
            suma1 += Ywezly[k] * wielomian_czebyszewa(spr[k], i) 
            suma2 += wielomian_czebyszewa(spr[k], i) * wielomian_czebyszewa(spr[k], i)  
        wspol.append(suma1/suma2)  #obliczenie współczynnika i dodanie go do listy
    return wspol

#funkcja do wyznaczania węzłów Czebyszewa
def czebyszew_wezly(stopien, a, b):
    c = []  
    for i in range(stopien + 1):  
        #obliczanie pozycji węzłów Czebyszewa
        ci = (1/2) * ((b - a) * cos(((2 * i + 1) * pi) / (2 * stopien + 2)) + (b + a))
        c.append(ci)
    return c 

#funkcja do generowania punktów funkcji aproksymowanej
def punkty(a, b, wybor):
    X = []  
    Y = []  
    i = a  
    while i <= b:  #pętla iterująca po zadanym przedzale z krokiem co 0.01
        X.append(i)  #dodaj obecną wartość do listy X
        Y.append(wartosci_wykorzystanych_funkcji(wybor, i))  #oblicz wartość funkcji i dodaj do listy Y
        i += 0.01 
    return X, Y  
