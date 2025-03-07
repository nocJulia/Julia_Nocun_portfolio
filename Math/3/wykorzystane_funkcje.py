# Autorzy: Julia Nocuń, Kevin Makarewicz

import numpy as np   
from schemat_hornera import schemat_hornera

def wartosci_wykorzystanych_funkcji(wybor_funkcji, x):      #zwraca wartości wykorzystywanych w zadaniu funkcji

    if wybor_funkcji == 1:
        wartosc_funkcji = schemat_hornera([1, 2, -1, -2], x)        #x^(3)+2 x^(2)-x-2

    elif wybor_funkcji == 2:
        wartosc_funkcji = schemat_hornera([-6, 0, 18, 0], x)          #−6x^3+18x
    
    elif wybor_funkcji == 3:
        wartosc_funkcji = abs(x + 2)        #|x + 2|
    
    elif wybor_funkcji == 4:
        wartosc_funkcji = 2*x + 4           #2x+4

    elif wybor_funkcji == 5:
        wartosc_funkcji = 3*np.cos(x) - np.sin(x)       #3cos(x) - sin(x)
    else:
       wartosc_funkcji = abs(np.sin(x - 2) - 0.5)       #|sin(x-2) - 0.5|

    return  wartosc_funkcji    
    
