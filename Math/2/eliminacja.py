# Autorzy: Kevin Makarewicz, Julia Nocuń

e = 0.000001

def eliminacja(macierz, n):
    if n != macierz.shape[0]:
        print("Podano zły rozmiar macierzy!")
        return 
    else:
        for i in range(n - 1):
            if abs(macierz[i][i]) < e:
                zamieniona = False
                for j in range(i + 1, n):
                    if abs(macierz[j][i]) > e:
                        macierz[[i, j]] = macierz[[j, i]]  # Zamiana wierszy
                        zamieniona = True
                if not zamieniona:
                    continue

            if abs(macierz[i][i]) > e:
                for j in range(i + 1, n):
                    mnoznik = -macierz[j][i] / macierz[i][i]
                    macierz[j] += mnoznik * macierz[i]

        return macierz