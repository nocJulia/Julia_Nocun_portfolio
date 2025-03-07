# Autorzy Julia Nocuń, Kevin Makarewicz

import matplotlib.pyplot as plt
import numpy as np

def generuj_wykres(zakres, funkcja, miejsce_zerowe_bisekcja = None, miejsce_zerowe_sieczne = None):
    x = np.linspace(zakres[0], zakres[1], 1000)
    y = funkcja(x)

    plt.plot(x, y, label='Funkcja')

    if miejsce_zerowe_bisekcja is not None and miejsce_zerowe_sieczne is not None:
        plt.scatter(miejsce_zerowe_bisekcja, 0, color='magenta', label='Miejsce zerowe - bisekcja')
        plt.scatter(miejsce_zerowe_sieczne, 0, color='black', label='Miejsce zerowe - sieczna', marker='*')

    plt.axhline(0, color='black', linewidth=0.5, linestyle='--')
    plt.axvline(0, color='black', linewidth=0.5, linestyle='--')

    plt.title('Wykres funkcji')
    plt.xlabel('x')
    plt.ylabel('y')
    plt.legend()
    plt.grid(True)
    plt.show()

