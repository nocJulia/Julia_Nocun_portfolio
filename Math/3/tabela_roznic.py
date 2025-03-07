# Autorzy: Kevin Makarewicz, Julia Nocuń

def tabela_roznic(dane):              #wyznacza różnice progresywne
  
    wezly_interpolacji = len(dane)      #ilosc wezlow interpolacyjnych
    delta_y = [[]]
    delta_y[0] = dane       #Do pierwszego wiersza tabeli wpisujemy dane wejściowe
    for iterator in range(1, wezly_interpolacji):
        #Obliczamy różnice i dodajemy je do odpowiednich wierszy tabeli
        delta_y.append([round(delta_y[iterator - 1][i + 1] - delta_y[iterator - 1][i], 2)
                        for i in range(wezly_interpolacji - iterator)])
    return delta_y  #zwracamy obliczone różnice