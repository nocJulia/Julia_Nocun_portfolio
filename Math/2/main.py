# Autorzy: Kevin Makarewicz, Julia Nocuń

import numpy as np
from eliminacja import eliminacja
from podstawianie import oblicz
from odczyt_pliku import odczyt_pliku


def main():

    program = True

    while program:

        print("\nMetoda eliminacji Gaussa.\n")
        print("1. Rozwiąż układ równań. ")
        print("2. Zamknij program.")

        wybor_akcji = int(input("Podaj co chcesz zrobić: "))

        if wybor_akcji == 2:
            program = False
            print("Dziękuję za poświęcony czas! Koniec programu.")

        elif wybor_akcji == 1:

            n = int(input("Podaj liczbę równań: "))
            
            macierz_poczatkowa = odczyt_pliku()

            print("Macierz współczynników przed eliminacją: ")
            print(macierz_poczatkowa)

            eliminacja_macierz = eliminacja(macierz_poczatkowa, n)

            if eliminacja_macierz is not None:
                print("\nMacierz współczynników po eliminacji: \n")
                print(eliminacja_macierz)

                x = np.zeros(eliminacja_macierz.shape[0])

                rozwiazania = oblicz(x, eliminacja_macierz, n)     #zastosowanie funkcji wyznaczającej rozwiazania ukladu rownań

                if rozwiazania is not None:
                    print("\nRozwiązania układu równań: ")
                    for i, sol in enumerate(rozwiazania):
                        print(f"x{i + 1} = {sol}")          #wypisanie poszczególnych rozwiązań
                        
            else:
                program = False

        else:
            print("Błędny wybór. Wybierz opcję 1 lub 2.")

if __name__ == "__main__":
    main()