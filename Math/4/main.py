#Autorzy: Kevin Makarewicz, Julia Nocuń

from calkowanie_funkcje import calka_gauss, simpson_granica

def main():
	
    program = True
	
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

            poprawna_dokladnosc = False

            while not poprawna_dokladnosc:
                
                dokladnosc = float(input("Podaj dokładność obliczeń: "))

                if dokladnosc <= 0:
                    print("Błąd: Dokładnośc musi być większa od zera!. Wprowadź ponownie: ")
                else:
                    poprawna_dokladnosc = True

            for i in range(2, 6):       #obliczamy całkę dla 2, 3, 4 oraz 5 węzłów
                print("Liczba wezlow:", i)
                print("Wartość całki wyznaczona metodą Gaussa-Czebyszewa:", calka_gauss(wybor_funkcji, i), "\n")

            print("\nWartość całki wyznaczona metodą Newtona-Cotesa:", simpson_granica(wybor_funkcji, dokladnosc), "\n")

        else:
            print("Podano złą opcję! Wybierz ponownie: ")

if __name__ == "__main__":
    main()
