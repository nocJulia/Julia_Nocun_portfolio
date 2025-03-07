# Autorzy Julia Nocuń, Kevin Makarewicz
# Metody 0. bisekcji, 2. siecznych w wariancie B

def metoda_bisekcji_iteracje(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, max_iteracje):

    x = 0  
    iterator = 0
    
    while iterator < max_iteracje:
        if wybrana_funkcja(przedzial_poczatek) == 0:
            print(f"Początek podanego przedziału jest miejscem zerowym.")
            return przedzial_poczatek 
        elif wybrana_funkcja(przedzial_koniec) == 0:
            print(f"Koniec podanego przedziału jest miejscem zerowym.")
            return przedzial_koniec   
        else:
            x = (przedzial_poczatek + przedzial_koniec) / 2
            if wybrana_funkcja(x) == 0:
                print(f"Miejsce zerowe znalezione metodą bisekcji: {x} w {iterator + 1} iteracjach.")
                return x, iterator + 1
            elif wybrana_funkcja(x) * wybrana_funkcja(przedzial_poczatek) < 0:
                przedzial_koniec = x
            else:
                przedzial_poczatek = x

            iterator += 1

    print(f"Miejsce zerowe znalezione metodą bisekcji: {x} w {iterator} iteracjach. Dokładność wyniku: {abs(wybrana_funkcja(x))}.")

    return x
    
def metoda_bisekcji_dokladnosc(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, epsilon):

        x = 0
        iterator = 0

        while abs(wybrana_funkcja(x)) >= epsilon:
            if wybrana_funkcja(przedzial_poczatek) == 0:
                print(f"Początek podanego przedziału jest miejscem zerowym.")
                return przedzial_poczatek 
            elif wybrana_funkcja(przedzial_koniec) == 0:
                print(f"Koniec podanego przedziału jest miejscem zerowym.")
                return przedzial_koniec 
            x = (przedzial_poczatek + przedzial_koniec) / 2
            if wybrana_funkcja(x) == 0:
                print(f"Miejsce zerowe znalezione metodą bisekcji: {x} w {iterator + 1} iteracjach. Dokładność wyniku: {abs(wybrana_funkcja(x))}.")
                return x, iterator + 1
            elif wybrana_funkcja(x) * wybrana_funkcja(przedzial_poczatek) < 0:
                przedzial_koniec = x
            else:
                przedzial_poczatek = x

            iterator += 1

        print(f"Miejsce zerowe znalezione metodą bisekcji: {x} w {iterator} iteracjach. Dokładność wyniku: {epsilon}.")

        return x

def metoda_siecznych_iteracje(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, max_iteracje):
    
    x = 0
    iterator = 0
    f_x1 = wybrana_funkcja(przedzial_poczatek)
    f_x2 = wybrana_funkcja(przedzial_koniec)

    
    while iterator < max_iteracje:
        if wybrana_funkcja(przedzial_poczatek) == 0:
            print(f"Początek podanego przedziału jest miejscem zerowym.")
            return przedzial_poczatek 
        elif wybrana_funkcja(przedzial_koniec) == 0:
            print(f"Koniec podanego przedziału jest miejscem zerowym.")
            return przedzial_koniec 
        x = przedzial_poczatek - f_x1 * (przedzial_poczatek - przedzial_koniec) / (f_x1 - f_x2)
        f_x0 = wybrana_funkcja(x)
        if f_x0 == 0:
            print(f"Miejsce zerowe znalezione metodą siecznych: {x} w {iterator + 1} iteracjach. Dokładność wyniku: {abs(wybrana_funkcja(x))}.")
            return x
        przedzial_koniec = przedzial_poczatek; f_x2 = f_x1
        przedzial_poczatek = x; f_x1 = f_x0

        iterator +=1

    print(f"Miejsce zerowe znalezione metodą siecznych: {x} w {iterator} iteracjach. Dokładność wyniku: {abs(wybrana_funkcja(x))}.")

    return x

def metoda_siecznych_dokladnosc(wybrana_funkcja, przedzial_poczatek, przedzial_koniec, epsilon):
    
    x = 0
    iterator = 0
    f_x1 = wybrana_funkcja(przedzial_poczatek)
    f_x2 = wybrana_funkcja(przedzial_koniec)

    
    while abs(wybrana_funkcja(x)) >= epsilon:
        if wybrana_funkcja(przedzial_poczatek) == 0:
            print(f"Początek podanego przedziału jest miejscem zerowym.")
            return przedzial_poczatek 
        elif wybrana_funkcja(przedzial_koniec) == 0:
            print(f"Koniec podanego przedziału jest miejscem zerowym.")
            return przedzial_koniec 
        x = przedzial_poczatek - f_x1 * (przedzial_poczatek - przedzial_koniec) / (f_x1 - f_x2)
        f_x0 = wybrana_funkcja(x)
        if f_x0 == 0:
            print(f"Miejsce zerowe znalezione metodą siecznych: {x} w {iterator + 1} iteracjach. Dokładność wyniku: {abs(wybrana_funkcja(x))}.")
            return x, iterator + 1
        przedzial_koniec = przedzial_poczatek; f_x2 = f_x1
        przedzial_poczatek = x; f_x1 = f_x0

        iterator +=1

    print(f"Miejsce zerowe znalezione metodą siecznych: {x} w {iterator} iteracjach. Dokładność wyniku: {epsilon}.")

    return x
    




