# Autorzy Julia Nocuń, Kevin Makarewicz

import numpy as np
import matplotlib.pyplot as plt
from wykorzystane_funkcje import funkcja_wykladnicza, funkcja_trygonometryczna, funkcja_zlozona, wielomian
from metody_0_2 import metoda_bisekcji_dokladnosc, metoda_bisekcji_iteracje, metoda_siecznych_iteracje, metoda_siecznych_dokladnosc
from tworzenie_wykresu import generuj_wykres

program = True
epsilon = 0.0
max_iteracje = 0

while program:

    print("Wybierz funkcje:")
    print("1. 2e^x + sin(x) ")
    print("2. x^3 + 4(x^2) + 2x - 3")
    print("3. 3sin(x) + x - 6 ")
    print("4. (e^cos(x)) - 2 ")      

    wybor_funkcji = int(input("Podaj numer funkcji lub wciśnij 0 aby zakończyć program: "))

    if wybor_funkcji == 0:
        program = False
        print("Dziękuję za poświęcony czas! Koniec programu.")

    elif wybor_funkcji in [1, 2, 3, 4]:

        funkcje = [funkcja_wykladnicza, wielomian, funkcja_trygonometryczna, funkcja_zlozona]
        wybrana_funkcja = funkcje[wybor_funkcji - 1]

        poprawny_przedzial = False

        while not poprawny_przedzial:
            
            przedzial_poczatek = float(input("Podaj początek przedziału: "))
            przedzial_koniec = float(input("Podaj koniec przedziału: "))

            if wybrana_funkcja(przedzial_poczatek) * wybrana_funkcja(przedzial_koniec) > 0:
                print("Błąd: Funkcja nie spełnia założenia o przeciwnych znakach na krańcach przedziału. Podaj nowy przedział: ")
            else:
                poprawny_przedzial = True
                
                print("Wybierz warunek stopu:")
                print("1. Dokładność")
                print("2. Ilość iteracji")

                warunek_stopu = int(input("Twój wybór: "))

                if warunek_stopu == 1:
                    epsilon = float(input("Podaj dokładność obliczeń (epsilon): ")) 
                    x_bisekcja_dokladnosc = metoda_bisekcji_dokladnosc(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, epsilon)
                    x_sieczne_dokladnosc = metoda_siecznych_dokladnosc(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, epsilon)
                    generuj_wykres([przedzial_poczatek, przedzial_koniec], wybrana_funkcja, x_bisekcja_dokladnosc, x_sieczne_dokladnosc)
                elif warunek_stopu == 2:
                    max_iteracje = int(input("Podaj maksymalną ilość iteracji: "))
                    x_bisekcja_iteracje = metoda_bisekcji_iteracje(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, max_iteracje)
                    x_sieczne_iteracje = metoda_siecznych_iteracje(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, max_iteracje)
                    generuj_wykres([przedzial_poczatek, przedzial_koniec], wybrana_funkcja, x_bisekcja_iteracje, x_sieczne_iteracje)
                else:
                    print("Błędny wybór. Wybierz liczbę 1 lub 2.")
    else:
        print("Błędny wybór. Wybierz liczbę z zakresu od 1 do 4.")
