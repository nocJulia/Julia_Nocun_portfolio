# Autorzy Julia Nocuń, Kevin Makarewicz

import numpy as np
from schemat_hornera import schemat_hornera

def funkcja_wykladnicza(x):
    return 2 * np.exp(x) + np.sin(x)

def wielomian(x):
    return schemat_hornera([1, 4, 2, -3], x)                    #x**3 + 4*(x**2) + 2*x - 3

def funkcja_trygonometryczna(x):
    return 3*np.sin(x) + x - 6

def g(x):
    return np.cos(x)

def f(x):
    return np.exp(x)

def funkcja_zlozona(x):
    return f(g(x)) - 2