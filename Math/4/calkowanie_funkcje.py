#Autorzy: Kevin Makarewicz, Julia Nocuń

import numpy as np
from wykorzystane_funkcje import wartosci_wykorzystanych_funkcji

def waga_funkcji(wybor_funkcji, x):         #obliczamy wartość iloczynu wybranej funkcji i wagi 1/sqrt(1-x^2) w punkcie x 
    waga = wartosci_wykorzystanych_funkcji(wybor_funkcji, x) * (1/np.sqrt(1 - x*x))
    return waga


def calka_gauss(wybor_funkcji, ilosc_wezlow):   #obliczamy całkę korzystając z kwadratury Gaussa-Czebyszewa
    calka = 0.0
    for i in range(1, ilosc_wezlow + 1):    #iterujemy po liczbie węzłów
        aktualna_waga = np.pi/ilosc_wezlow    #wyliczamy aktualną wagę
        aktualny_wezel = -np.cos(((2*i-1)*np.pi)/(2*ilosc_wezlow))    #wyliczamy aktualny węzeł
        calka += aktualna_waga * wartosci_wykorzystanych_funkcji(wybor_funkcji, aktualny_wezel) #wyznaczamy wartość całki
    return calka

def calka_simpson(wybor_funkcji, poczatek_przedzialu, koniec_przedzialu, dokladnosc):
    podprzedzial = 1  #ustawiamy początkową liczbę podprzedziałów na 1
    ostatnia_calka = 0  

    while True:  
        
        calka = 0  
        podprzedzial *= 2   #w każdej iteracji dwukrotnie zwiększamy ilość podprzedziałów
        dlugosc = (koniec_przedzialu-poczatek_przedzialu)/podprzedzial  #obliczamy długość podprzedziału

        #obliczamy wartość funkcji w punktach krańcowych i dodajemy je do całki
        calka += waga_funkcji(wybor_funkcji, poczatek_przedzialu) + waga_funkcji(wybor_funkcji, koniec_przedzialu)

        #obliczamy resztę wartości funkcji na podprzedziałach i sumujemy je z odpowiednimi wagami
        for i in range(1, podprzedzial // 2):
            calka += 4 * waga_funkcji(wybor_funkcji, poczatek_przedzialu + (2*i-1)*dlugosc)
            calka += 2 * waga_funkcji(wybor_funkcji, poczatek_przedzialu + (2*i)*dlugosc)

        calka *= dlugosc/3  #dokonujemy korekty wyniku zgodnie z metodą Simpsona

        if abs(ostatnia_calka - calka) <= dokladnosc:   #jeśli wynik zmienił się o mniej niż dokładność zadana przez użytkownika oznacza to, że
            return calka                                #dokładność całki na podanym przedziale została obliczona z zadaną dokładnością

        ostatnia_calka = calka  #aktualizujemy wartość poprzedniej całki

def simpson_granica(wybrana_funkcja, dokladnosc):
    poczatek_przedzialu = 0  
    koniec_przedzialu = 0.5  
    granica = 0  

    #granica na przedziale [0, 1]
    temp = calka_simpson(wybrana_funkcja, poczatek_przedzialu, koniec_przedzialu, dokladnosc)
    while abs(temp) > dokladnosc:
        granica += temp
        poczatek_przedzialu = koniec_przedzialu
        koniec_przedzialu = koniec_przedzialu + ((1 - koniec_przedzialu) * 1 / 2)
        temp = calka_simpson(wybrana_funkcja, poczatek_przedzialu, koniec_przedzialu, dokladnosc)

    #granica na przedziale [-1, 0)
    poczatek_przedzialu = -0.5
    koniec_przedzialu = 0
    temp = calka_simpson(wybrana_funkcja, poczatek_przedzialu, koniec_przedzialu, dokladnosc)

    while abs(temp) > dokladnosc:
        granica += temp
        koniec_przedzialu = poczatek_przedzialu
        poczatek_przedzialu = poczatek_przedzialu - ((1 - abs(koniec_przedzialu)) * 1 / 2)
        temp = calka_simpson(wybrana_funkcja, poczatek_przedzialu, koniec_przedzialu, dokladnosc)

    return granica  #zwracamy obliczoną granicę