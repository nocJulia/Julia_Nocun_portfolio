#Autorzy: Kevin Makarewicz, Julia Nocuń
#wersja z wyborem trybu pracy (na maksymalna ocene 5)

import matplotlib.pyplot as plt
import numpy as np
from aproksymacja import wielomian_czebyszewa, wspolczynniki, czebyszew_wezly, punkty
from wykorzystane_funkcje import wartosci_wykorzystanych_funkcji

def oblicz_blad(X, Y_oryginalne, Y_aproksymacja):
    #obliczamy średni błąd absolutny
    suma = sum(abs(Y_oryginalne[i] - Y_aproksymacja[i]) for i in range(len(X)))
    blad = suma / len(X)
    return blad

def main():
    program = True      #flaga potrzebna do głównej pętli
	
    while program:

        print("Wybierz funkcje:")
        print("1. Wielomian: x^(3)+2 x^(2)-x-2 ")
        print("2. Funkcja liniowa: x+4 ")
        print("3. Funkcja: |x+2| ")
        print("4. Funkcja trygonometryczna: 3cos(x) - sin(x) ")
        print("5. Funkcja złożona: (x+4)*sin(x) ") 
        print("6. Funkcja złożona: |cos(x) - 3| ")     

        wybor_funkcji = int(input("Podaj numer funkcji lub wciśnij 0 aby zakończyć program: "))

        if wybor_funkcji == 0:
            program = False
            print("Dziękuję za poświęcony czas! Koniec programu.")

        elif wybor_funkcji in [1, 2, 3, 4, 5, 6]:

            # Rysowanie wybranej funkcji dla domyślnego przedziału
            X = np.linspace(-15, 15, 1000)  # punktacja do rysowania funkcji
            Y = [wartosci_wykorzystanych_funkcji(wybor_funkcji, x) for x in X]

            #Rysowanie wykresu wybranej funkcji
            plt.figure()
            plt.title("Wybrana funkcja")
            plt.xlabel("Oś x")
            plt.ylabel("Oś y")
            plt.plot(X, Y, label="Wybrana funkcja")
            plt.legend()
            plt.grid(True)
            plt.show()

            przedzial_poczatek = float(input("Podaj początek przedziału aproksymacji: "))
            przedzial_koniec = float(input("Podaj koniec przedziału aproksymacji: "))

            tryb = int(input("\nPodaj tryb pracy programu:\n1. Określony stopień wielomianu \n2. Określona dokładność aproksymacji  "))
            if tryb == 1:
                stopien = int(input("Podaj stopień wielomianu aproksymującego: "))
                #generowanie punktów do wykresu
                X, Y = punkty(przedzial_poczatek, przedzial_koniec, wybor_funkcji)  #punkty dla funkcji aproksymowanej
                c = czebyszew_wezly(stopien, przedzial_poczatek, przedzial_koniec)    #węzły Czebyszewa
                Ynodes = [wartosci_wykorzystanych_funkcji(wybor_funkcji, ci) for ci in c]   #wartosci wezlow czebyszewa
                spr = [(2 * ci - (przedzial_koniec + przedzial_poczatek)) / (przedzial_koniec - przedzial_poczatek) for ci in c]

                wspol = wspolczynniki(spr, Ynodes, stopien)  #obliczenie współczynników wielomianu

                # Rysowanie wykresu
                plt.figure()
                plt.title("Wykres metody aproksymacji opartej o wielomiany Czebyszewa")
                plt.xlabel("Oś x")
                plt.ylabel("Oś y")

                # Wykres funkcji pierwotnej
                plt.plot(X, Y, label="Wykres funkcji pierwotnej")

                #obliczenie i tworzenie wykresu funkcji aproksymującej
                czeX, czeY = [], []
                i = przedzial_poczatek  #inicjalizacja wartości początkowej
                while i <= przedzial_koniec:
                    czeX.append(i)
                    sum = 0
                    for j in range(stopien + 1):
                        sum += wspol[j] * wielomian_czebyszewa((2 * (i - przedzial_poczatek) / (przedzial_koniec - przedzial_poczatek)) - 1, j) #obliczanie wartości funkcji aproksymującej
                    czeY.append(sum)
                    i += 0.01

                # Obliczenie błędu
                Y_oryginalne = [wartosci_wykorzystanych_funkcji(wybor_funkcji, x) for x in czeX]
                blad_mae = oblicz_blad(czeX, Y_oryginalne, czeY)

                print(f"Błąd średni absolutny dla wielomianu aproksymującego stopnia {stopien} wynosi: {blad_mae:.5f}")

                plt.plot(czeX, czeY, label="Wykres funkcji aproksymującej", linestyle = "--")
                plt.legend()
                plt.grid(True)
                plt.show()
                
            else:
                #tryb z zadana przez użytkownika dokładnością aproksymacji

                eps = float(input("Podaj oczekiwaną dokładność aproksymacji: "))
                stopien = 1 #początkowy stopień wielomianu aproksymującego
                epsilon = float('inf')

                #iterujemy do uzyskania pożądanego poziomu błędu
                while epsilon > eps:
                    stopien += 1
                    c = czebyszew_wezly(stopien, przedzial_poczatek, przedzial_koniec)   #wezły czebyszewa
                    Ynodes = [wartosci_wykorzystanych_funkcji(wybor_funkcji, ci) for ci in c]   #wartości w węzłach
                    spr = [(2 * ci - (przedzial_koniec + przedzial_poczatek)) / (przedzial_koniec - przedzial_poczatek) for ci in c]

                    wspol = wspolczynniki(spr, Ynodes, stopien)  #obliczenie współczynników wielomianu

                    #obliczanie średniego błędu aproksymacji
                    bsum = 0
                    ile = 0
                    i = przedzial_poczatek
                    while i <= przedzial_koniec:
                        sum = 0
                        for j in range(stopien + 1):
                            sum += wspol[j] * wielomian_czebyszewa((2 * (i - przedzial_poczatek) / (przedzial_koniec - przedzial_poczatek)) - 1, j)
                        bsum += abs(wartosci_wykorzystanych_funkcji(wybor_funkcji, i) - sum)
                        ile += 1
                        i += 0.2

                    epsilon = bsum / ile

                print(f"Stopień wielomianu aproksymującego, dla którego osiągnięto zadaną dokładność to: {stopien}")

                #rysowanie wykresu
                plt.figure()
                plt.title("Wykres metody aproksymacji opartej o wielomiany Czebyszewa")
                plt.xlabel("Oś x")
                plt.ylabel("Oś y")

                X, Y = punkty(przedzial_poczatek, przedzial_koniec, wybor_funkcji)
                plt.plot(X, Y, label="Wykres funkcji pierwotnej")

                #obliczanie i tworzenie wykresu funkcji aproksymującej
                czeX, czeY = [], []
                i = przedzial_poczatek
                while i <= przedzial_koniec:
                    sum = 0
                    for j in range(stopien + 1):
                        sum += wspol[j] * wielomian_czebyszewa((2 * (i - przedzial_poczatek) / (przedzial_koniec - przedzial_poczatek)) - 1, j)  #obliczanie wartości funkcji aproksymującej
                    czeX.append(i)
                    czeY.append(sum)
                    i += 0.01

                plt.plot(czeX, czeY, label="Wykres funkcji aproksymującej", linestyle ="--" )
                plt.legend()
                plt.grid(True)
                plt.show()

        else:
            print("Podano złą opcję! Wybierz ponownie: ")

if __name__ == "__main__":
    main()

