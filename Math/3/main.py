# Autorzy: Kevin Makarewicz, Julia Nocuń

import matplotlib.pyplot as plt
import numpy as np
import sympy as sp
from schemat_hornera import schemat_hornera
from wykorzystane_funkcje import wartosci_wykorzystanych_funkcji
from odczyt_pliku import odczyt_pliku
from interpolacja_Newtona import interpolacja_pierwsza_polowa, interpolacja_druga_polowa


argumenty_interpolacyjne = []
wartosci_interpolacyjne = []
program = True


while program is True:

    print("Metoda interpolacji Newtona dla węzłów równoodległych.")
    print("\nWybierz co chcesz zrobić:\n")
    wybor_akcji = int(input("1. Wybór spośród gotowych funkcji\n2. Wczytanie funkcji z pliku\n0. Koniec działania programu\n"))
    if wybor_akcji == 0:
        program = False
        print("Dziękuję za poświęcony czas! Koniec programu.")
    elif wybor_akcji in [1, 2]:
        petla = True
        while petla:
            try:
                poczatek_przedzialu = int(input("Podaj początek przedziału: "))
                petla = False
            except ValueError:
                print("Zła wartość! Proszę ponownie podać początek przedziału:")
        petla = True
        while petla:
            try:
                koniec_przedzialu = int(input("Podaj koniec przedziału: "))
                petla = False
            except ValueError:
                print("Zła wartość! Proszę ponownie podać koniec przedziału:")
        argumenty = list(np.linspace(poczatek_przedzialu, koniec_przedzialu, 100)) #tworzymy liste 100 równomiernie rozłożonych argumentów na podanym przedziale
        if wybor_akcji == 1:
            print("\nWybierz jedną spośród podanych funkcji:")
            print("1. Wielomian: x^(3) + 2x^(2) - x - 2")
            print("2. Wielomian: −6x^(3)+18x")
            print("3. Funkcja: |x + 2| ")
            print("4. Funkcja liniowa: 2x + 4")
            print("5. Funkcja trygonometryczna: 3cos(x) - sin(x)")
            print("6. Funkcja złożona: |sin(x-2) - 0.5| ")

            petla = True
            while petla:
                wybor_funkcji = int(input("Podaj numer wybranej funkcji: "))
                if wybor_funkcji in [1, 2, 3, 4, 5, 6]:
                    petla = False
                else:
                    print("Zły wybór! Wybierz ponownie opcję 1, 2, 3, 4, 5 lub 6: ")

            wartosci_funkcji = []   #wyznaczenie wartości funkcji, której wzór znamy
            for argument in argumenty:
                wartosci_funkcji.append(wartosci_wykorzystanych_funkcji(wybor_funkcji,argument))    #wyznaczenie wartości funkcji dla poszczególnych argumentów
            plt.plot(argumenty, wartosci_funkcji, label='Wykres funkcji f(x)')   #tworzenie wykresu funkcji której wzór znamy

            plt.grid(True)   #pokazanie siatki na wykresie
            plt.legend(loc='upper right')  #legenda wykresu umieszczona w prawym górnym rogu
            plt.xlabel("x")  #opis OX 
            plt.ylabel("y")  #opis OY

            plt.show(block=True)  #wyswietlenie wykresu


            petla = True
            while petla:
                wezly_interpolacyjne = int(input("Podaj liczbę węzłów interpolacyjnych: "))
                if wezly_interpolacyjne > 0:
                    petla = False
                else:
                    print("Liczba węzłów musi być większa od 0! Wprowadź ponownie:")

            wartosci_funkcji = []   #wyznaczenie wartości funkcji, której wzór znamy
            for argument in argumenty:
                wartosci_funkcji.append(wartosci_wykorzystanych_funkcji(wybor_funkcji,argument))    #wyznaczenie wartości funkcji dla poszczególnych argumentów
            plt.plot(argumenty, wartosci_funkcji, label='Wykres funkcji f(x)')   #tworzenie wykresu funkcji której wzór znamy

            plt.grid(True)   #pokazanie siatki na wykresie
            plt.legend(loc='upper right')  #legenda wykresu umieszczona w prawym górnym rogu
            plt.xlabel("x")  #opis OX 
            plt.ylabel("y")  #opis OY

            
            
            argumenty_interpolacyjne = np.linspace(poczatek_przedzialu, koniec_przedzialu, wezly_interpolacyjne) #tworzymy liste równomiernie rozłożonych wezłów na podanym przedziale
            wartosci_interpolacyjne = wartosci_wykorzystanych_funkcji(wybor_funkcji, argumenty_interpolacyjne)  #wyznaczenie wartości dla poszczególnych węzłów interpolacyjnych

        else:   #gdy wybrano skorzystanie ze stablicowanych wartości
            argumenty_interpolacyjne, wartosci_interpolacyjne = odczyt_pliku()

        x = sp.Symbol('x')  #musimu utworzyć zmienną symboliczną x 
        wielomian_pierwsza_polowa = interpolacja_pierwsza_polowa(argumenty_interpolacyjne, wartosci_interpolacyjne)
        wielomian_druga_polowa = interpolacja_druga_polowa(argumenty_interpolacyjne, wartosci_interpolacyjne)

        wspolczynniki_inter_pierwsza = sp.Poly(wielomian_pierwsza_polowa, x).all_coeffs() #tworzymy listę współczynników wielomianu, zaczynając 
        wspolczynniki_inter_druga = sp.Poly(wielomian_druga_polowa, x).all_coeffs()       #od wyrazu przy najwyższej potędze i kończąc na wyrazie wolnym

        wartosci = []
        for argument in argumenty:
            if argument < (argumenty[-1] + argumenty[0]) / 2:
                wartosci.append(schemat_hornera(wspolczynniki_inter_pierwsza, argument))    #wyliczenie wartości dla pierwszej połowy wielomianu interpolacyjnego
            else:
                wartosci.append(schemat_hornera(wspolczynniki_inter_druga, argument))   #wyliczenie wartości dla drugiej połowy wielomianu interpolacyjnego
        plt.scatter(argumenty_interpolacyjne, wartosci_interpolacyjne, label='Węzły interpolacyjne') #rysowanie węzłów interpolacyjnych
        plt.plot(argumenty, wartosci, linestyle=":", label='Wielomian interpolacyjny') #tworzenie wykresu wielomianu interpolacyjnego
        plt.grid(True)   #pokazanie siatki na wykresie
        plt.legend(loc='upper right')  #legenda wykresu umieszczona w prawym górnym rogu
        plt.xlabel("x")  #opis OX 
        plt.ylabel("y")  #opis OY
        
        print(f"""Wielomian interpolacyjny jest postaci:
<{poczatek_przedzialu}:{(poczatek_przedzialu + koniec_przedzialu) / 2}): {wielomian_pierwsza_polowa}
<{(poczatek_przedzialu + koniec_przedzialu) / 2}:{koniec_przedzialu}>: {wielomian_druga_polowa}
""")        #wyświetlenie w konsoli wzorów wielomianów interpolacyjnych uwzględniając podział podanego przedziału na połowy

        plt.show(block=True)  #wyswietlenie wykresu
    else:
        print("Błędny wybór! Wybierz akcję spośród numerów 0, 1 lub 2: ")
