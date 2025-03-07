#KAD - Zad2, Kevin Makarewicz, Julia Nocuń

import pandas as pd
import math
import matplotlib.pyplot as plt

column_names = ['sepal_length', 'sepal_width', 'petal_length', 'petal_width', 'class']
data = pd.read_csv("data.csv", names=column_names)

#Funkcja obliczająca współczynnik korelacji Pearsona
def pearson_correlation(x, y):
    n = len(x)
    sum_x = sum(x)
    sum_y = sum(y)
    sum_xy = sum(x[i] * y[i] for i in range(n))
    sum_x_sq = sum(x[i] ** 2 for i in range(n))
    sum_y_sq = sum(y[i] ** 2 for i in range(n))

    numerator = n * sum_xy - sum_x * sum_y    #licznik współczynnika korelacji Pearsona
    denominator = math.sqrt((n * sum_x_sq - sum_x ** 2) * (n * sum_y_sq - sum_y ** 2))   #mianownik współczynnika korelacji Pearsona
    
    if denominator == 0:
        return 0  #unikamy dzielenia przez zero
    else:
        return numerator / denominator

#Funkcja wyznaczająca równanie regresji liniowej
def linear_regression(x, y):
    n = len(x)
    sum_x = sum(x)
    sum_y = sum(y)
    sum_xy = sum(x[i] * y[i] for i in range(n))
    sum_x_sq = sum(x[i] ** 2 for i in range(n))

    slope = (n * sum_xy - sum_x * sum_y) / (n * sum_x_sq - sum_x ** 2)   #współczynnik nachylenia linii regresji
    intercept = (sum_y - slope * sum_x) / n  #przesunięcie linii regresji

    return slope, intercept

#Wyniki dla długości i szerokości działki kielicha 
x = data[('sepal_length')]
y = data[('sepal_width')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między długością i szerokością działki kielicha: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Szerokość działki kielicha (cm)')
plt.xlabel(f'Długość działki kielicha (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x + {intercept:.1f}')
plt.show()

#Wyniki dla długości działki kielicha i długości płatka
x = data[('sepal_length')]
y = data[('petal_length')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między długością działki kielicha i długością płatka: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Długość płatka (cm)')
plt.xlabel(f'Długość działki kielicha (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x {intercept:.1f}')
plt.show()

#Wyniki dla długości działki kielicha i szerokośći płatka
x = data[('sepal_length')]
y = data[('petal_width')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między długością działki kielicha i szerokością płatka: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Szerokość płatka (cm)')
plt.xlabel(f'Długość działki kielicha (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x {intercept:.1f}')
plt.show()

#wyniki dla szerokości działki kielicha i długości płatka
x = data[('sepal_width')]
y = data[('petal_length')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między szerokością działki kielicha i długością płatka: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Długość płatka (cm)')
plt.xlabel(f'Szerokość działki kielicha (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x + {intercept:.2f}')
plt.show()

#wyniki dla szerokości działki kielicha i szerokości płatka
x = data[('sepal_width')]
y = data[('petal_width')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między szerokością działki kielicha i szerokością płatka: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Szerokość płatka (cm)')
plt.xlabel(f'Szerokość działki kielicha (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x + {intercept:.2f}')
plt.show()

#wyniki dla długości i szerokości płatka
x = data[('petal_length')]
y = data[('petal_width')]

#współczynnik korelacji Pearsona
correlation = pearson_correlation(x, y)
print(f'Współczynnik korelacji Pearsona między długością i szerokością płatka: {correlation}')

#równanie regresji liniowej
slope, intercept = linear_regression(x, y)
print(f'Równanie regresji liniowej: y = {slope:.2f}x + {intercept:.2f}')

#wykres punktowy z naniesioną linią regresji
plt.scatter(x, y)
plt.plot(x, [slope * xi + intercept for xi in x], color='red')
plt.ylabel(f'Szerokość płatka (cm)')
plt.xlabel(f'Długość płatka (cm)')
plt.title(f'r = {correlation:.2f}; y = {slope:.1f}x {intercept:.2f}')
plt.show()