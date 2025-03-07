import numpy as np

def odczyt_pliku():
    try:
       # n = int(input("Podaj liczbę równań: "))
        sciezka_pliku = input("Podaj nazwę pliku z współczynnikami (bez rozszerzenia): ")
        sciezka_pliku += ".txt"     #do nazwy dodajemy rozszerzenie pliku, tak aby użytkownik nie musiał go wprowadzać

        with open(sciezka_pliku, 'r') as plik:
            linie = plik.readlines()            #wczytanie wierszy ze współczynnikami 
            wspolczynniki = [list(map(float, linia.strip().split())) for linia in linie]    #wczytanie poszczególnych współczynników

            macierz_poczatkowa = np.array(wspolczynniki)
        
        return macierz_poczatkowa              #zwrócenie macierzy, którą odczytano z pliku

    except (FileNotFoundError, ValueError):
        print("Błąd przy wczytywaniu pliku lub podczas przetwarzania danych wejściowych.")
        return 

            