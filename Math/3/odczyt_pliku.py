# Autorzy: Kevin Makarewicz, Julia Nocuń

def odczyt_pliku():
    plik = input("Podaj nazwę pliku ze stablicowanymi wartościami funkcji:\n")
    try:
        with open(plik, 'r') as plik_dane:
            argumenty = plik_dane.readline().split()     #z pierwszej linijki odczytujemy x
            wartosci = plik_dane.readline().split()      #z drugiej linijki odczytujemy y
        argumenty = [float(i) for i in argumenty]
        wartosci = [float(i) for i in wartosci]
    except ValueError:
        print("Błąd w trakcie odczytu pliku!")
    return argumenty, wartosci  # zwracamy odczytane z pliku argumenty i wartości
