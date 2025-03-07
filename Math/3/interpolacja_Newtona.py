# Autorzy: Kevin Makarewicz, Julia Nocuń

import sympy as sp
from silnia import silnia
from tabela_roznic import tabela_roznic


def interpolacja_pierwsza_polowa(argumenty, wartosci):      #korzystamy z pierwszego wzoru Newtona

    h = argumenty[1] - argumenty[0]     #liczymy wartości odległości między argumentami
    liczba_wezlow = len(wartosci)       #ilość węzłów interpolacyjnych
    delta_y = tabela_roznic(wartosci)   #tworzemy tabele z wartościami różnic
    
    x = sp.Symbol('x')                  #zmienna symboliczna
    
    t = (x - argumenty[0]) / h          #argument interpolacji
    wspolczynniki = [round(delta_y[i][0] / silnia(i), 4) for i in range(liczba_wezlow)] #obliczamy współczynniki wielomianu interpolacyjnego
    
    wielomian = [wspolczynniki[0], t * wspolczynniki[1]]       #inicjalizujemy wielomian interpolacyjny
    
    while wspolczynniki[-1] == 0 and len(wspolczynniki) > 1:   #z końca listy usuwamy współczynniki, które są równe 0 w celu uproszczenia wielomianu
       wspolczynniki.pop()                                     #ofc jesli takie są, jest to generalnie opcjonalne ale upraszcza końcowy wielomian
    
    qt = t                                                  #wyznaczamy wartości wielomianu interpolacyjnego (w pętli)
    for i in range(len(wspolczynniki) - 2):
        qt -= 1
        t *= qt
        wielomian.append(t * wspolczynniki[i + 2])
    
    wartosc = sp.simplify(sum(wielomian))                   #upraszczamy wartość wielomianu tak aby móc go przedstawić 
    return wartosc                                          #w bardziej przystępnej dla użytkownika formie
        

def interpolacja_druga_polowa(argumenty, wartosci):     #korzystamy z drugiego wzoru Newtona
                                                        #kroki identyczne jak przy pierwszej połowie, tylko dopasowane do druguego wzoru
    h = argumenty[1] - argumenty[0]
    liczba_wezlow = len(wartosci)
    delta_y = tabela_roznic(wartosci)

    x = sp.Symbol('x')
    
    t = (x - argumenty[-1]) / h
    wspolczynniki = [round(delta_y[i][-1] / silnia(i), 4) for i in range(liczba_wezlow)]

    wielomian = [wspolczynniki[0], t * wspolczynniki[1]]
    
    while wspolczynniki[-1] == 0 and len(wspolczynniki) > 1:
        wspolczynniki.pop()
    
    qt = t
    for i in range(len(wspolczynniki) - 2):
        qt += 1
        t *= qt
        wielomian.append(t * wspolczynniki[i + 2])
    
    wartosc = sp.simplify(sum(wielomian))
    return wartosc
