# Autorzy: Kevin Makarewicz, Julia Nocuń

e = 0.000001

def oblicz(x, macierz, n):
    #n = macierz.shape[0]
    for i in range(n - 1, -1, -1):
        if abs(macierz[i][i]) < e:
            if abs(macierz[i][i + 1]) < e:
                print("Układ jest nieoznaczony")    #gdy element na przekątnej i element znajdujący się pod nim są blikie lub równe 0
            else:
                print("Układ jest sprzeczny")
            return None  #zwracamy None w przypadku niepowodzenia

        nowe_rozwiaznie = macierz[i][-1]
        for j in range(n - 1, i, -1):
            nowe_rozwiaznie -= macierz[i][j] * x[j]
        x[i] = round(nowe_rozwiaznie / macierz[i][i], 2)

    return x