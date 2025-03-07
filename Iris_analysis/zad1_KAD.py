import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv("data.csv", names=['sep_len', 'sep_wid', 'pet_len', 'pet_wid', 'species'])
setosa, versicolor, virginica = 0, 0, 0

# zliczamy liczebności poszczególnych gatunków
for array in data.values:
    if array[4] == 0:
        setosa += 1
    if array[4] == 1:
        versicolor += 1
    if array[4] == 2:
        virginica += 1

# histogram dla długości działki kielicha
data['sep_len'].plot.hist(width=0.5, range=(4.0, 8.0), bins=8, edgecolor='black')
plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Długość działki kielicha")
plt.ylim(0, 35)
plt.show()

# wyznaczenie danych o długości kielicha dla każdego gatunku
setosa_data = data[data['species'] == 0]['sep_len']
versicolor_data = data[data['species'] == 1]['sep_len']
virginica_data = data[data['species'] == 2]['sep_len']

# wykres pudełkowy dla długości działki kielicha
plt.boxplot([setosa_data, versicolor_data, virginica_data], labels=['setosa', 'versicolor', 'virginica'])
plt.xlabel("Gatunek")
plt.ylabel("Długość (cm)")
plt.yticks(range(4, 9, 1))
plt.show()

# histogram dla długości działki kielicha z rozróżnieniem na gatunki
plt.hist([data[data['species'] == 0]['sep_len'], data[data['species'] == 1]['sep_len'], data[data['species'] == 2]['sep_len']],
         bins=8, range=(4.0, 8.0), color=['red', 'blue', 'green'], edgecolor='black', label=['setosa', 'versicolor', 'virginica'])

plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Długość działki kielicha")
plt.ylim(0, 30)
plt.legend()
plt.show()

# histogram dla szerokości działki kielicha
data['sep_wid'].plot.hist(width=0.5, range=(2.0, 4.5), bins=5, edgecolor='black')
plt.xlabel("Szerokość (cm)")
plt.ylabel("Liczebność")
plt.title("Szerokość działki kielicha")
plt.ylim(0, 70)
plt.show()

# wyznaczenie danych o szerokości kielicha dla każdego gatunku
setosa_data = data[data['species'] == 0]['sep_wid']
versicolor_data = data[data['species'] == 1]['sep_wid']
virginica_data = data[data['species'] == 2]['sep_wid']

# wykres pudełkowy dla szerokości działki kielicha
plt.boxplot([setosa_data, versicolor_data, virginica_data], labels=['setosa', 'versicolor', 'virginica'])
plt.xlabel("Gatunek")
plt.ylabel("Szerokość (cm)")
plt.ylim(1.5, 4.5)
plt.show()

# histogram dla szerokości działki kielicha z rozróżnieniem na gatunki
plt.hist([data[data['species'] == 0]['sep_wid'], data[data['species'] == 1]['sep_wid'], data[data['species'] == 2]['sep_wid']],
         bins=5, range=(2.0, 4.5), color=['red', 'blue', 'green'], edgecolor='black', label=['setosa', 'versicolor', 'virginica'])

plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Długość działki kielicha")
plt.ylim(0, 30)
plt.legend()
plt.show()

# histogram dla długości płatka
data['pet_len'].plot.hist(width=0.5, range=(1.0, 7.0), bins=12, edgecolor='black')
plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Długość płatka")
plt.ylim(0, 30)
plt.show()

# wyznaczenie danych o długości płatka dla każdego gatunku
setosa_data = data[data['species'] == 0]['pet_len']
versicolor_data = data[data['species'] == 1]['pet_len']
virginica_data = data[data['species'] == 2]['pet_len']

# wykres pudełkowy dla długości płatka
plt.boxplot([setosa_data, versicolor_data, virginica_data], labels=['setosa', 'versicolor', 'virginica'])
plt.xlabel("Gatunek")
plt.ylabel("Długość (cm)")
plt.ylim(0.5, 7)
plt.show()

# histogram dla długości płatka z rozróżnieniem na gatunki
plt.hist([data[data['species'] == 0]['pet_len'], data[data['species'] == 1]['pet_len'], data[data['species'] == 2]['pet_len']],
         bins=12, range=(1.0, 7.0), color=['red', 'blue', 'green'], edgecolor='black', label=['setosa', 'versicolor', 'virginica'])

plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Długość działki kielicha")
plt.ylim(0, 30)
plt.legend()
plt.show()

# histogram dla szerokości płatka
data['pet_wid'].plot.hist(width=0.5, range=(0.0, 3.0), bins=6, edgecolor='black')
plt.xlabel("Szerokość (cm)")
plt.ylabel("Liczebność")
plt.title("Szerokość płatka")
plt.ylim(0, 50)
plt.show()

# wyznaczenie danych o szerokości płatka dla każdego gatunku
setosa_data = data[data['species'] == 0]['pet_wid']
versicolor_data = data[data['species'] == 1]['pet_wid']
virginica_data = data[data['species'] == 2]['pet_wid']

# wykres pudełkowy dla szerokości płatka
plt.boxplot([setosa_data, versicolor_data, virginica_data], labels=['setosa', 'versicolor', 'virginica'])
plt.xlabel("Gatunek")
plt.ylabel("Szerokość (cm)")
plt.ylim(0, 3)
plt.show()

# histogram dla szerokości płatka z rozróżnieniem na gatunki
plt.hist([data[data['species'] == 0]['pet_wid'], data[data['species'] == 1]['pet_wid'], data[data['species'] == 2]['pet_wid']],
         bins=6, range=(0.0, 3.0), color=['red', 'blue', 'green'], edgecolor='black', label=['setosa', 'versicolor', 'virginica'])

plt.xlabel("Długość (cm)")
plt.ylabel("Liczebność")
plt.title("Szerokość płatka")
plt.ylim(0, 50)
plt.legend()
plt.show()

# obliczamy udział procentowy poszczególnych gatunków, zaokrąglony z dokładnością do jednego miejsca po przecinku
liczebnosc_ogolna = setosa + versicolor + virginica
setosa_udzial = setosa / liczebnosc_ogolna
versicolor_udzial = versicolor / liczebnosc_ogolna
virginica_udzial = virginica / liczebnosc_ogolna
versicolor_udzial_procentowy = f"{versicolor_udzial*100:.1f}%"
virginica_udzial_procentowy = f"{virginica_udzial*100:.1f}%"
setosa_udzial_procentowy = f"{setosa_udzial*100:.1f}%"
print(f"Liczebność całkowita: {liczebnosc_ogolna}")
print(f"Liczebność gatunku setosa: {setosa}, udział procentowy gatunku setosa: {setosa_udzial_procentowy}")
print(f"Liczebność gatunku versicolor: {versicolor}, udział procentowy gatunku setosa: {versicolor_udzial_procentowy}")
print(f"Liczebność gatunku virginica: {virginica}, udział procentowy gatunku setosa: {virginica_udzial_procentowy}")

# wyznaczamy ekstrema dla każdej cechy
minimal_sep_len = f"{data['sep_len'].min():.2f}"
maximal_sep_len = f"{data['sep_len'].max():.2f}"
minimal_sep_wid = f"{data['sep_wid'].min():.2f}"
maximal_sep_wid = f"{data['sep_wid'].max():.2f}"
minimal_pet_len = f"{data['pet_len'].min():.2f}"
maximal_pet_len = f"{data['pet_len'].max():.2f}"
minimal_pet_wid = f"{data['pet_wid'].min():.2f}"
maximal_pet_wid = f"{data['pet_wid'].max():.2f}"
print(f"Minimum długości kielicha: {minimal_sep_len}")
print(f"Minimum szerokości kielicha: {minimal_sep_wid}")
print(f"Minimum długości płatka: {minimal_pet_len}")
print(f"Minimum szerokości płatka: {minimal_pet_wid}")
print(f"Maksimum długości kielicha: {maximal_sep_len}")
print(f"Maksimum szerokości kielicha: {maximal_sep_wid}")
print(f"Maksimum długości płatka: {maximal_pet_len}")
print(f"Maksimum szerokości płatka: {maximal_pet_wid}")

# wyznaczamy średnie arytmetyczne dla każdej cechy
arith_mean_sep_len = f"{data['sep_len'].mean():.2f}"
arith_mean_sep_wid = f"{data['sep_wid'].mean():.2f}"
arith_mean_pet_len = f"{data['pet_len'].mean():.2f}"
arith_mean_pet_wid = f"{data['pet_wid'].mean():.2f}"
print(f"Średnia arytmetyczna długości kielicha: {arith_mean_sep_len}")
print(f"Średnia arytmetyczna szerokości kielicha: {arith_mean_sep_wid}")
print(f"Średnia arytmetyczna długości płatka: {arith_mean_pet_len}")
print(f"Średnia arytmetyczna szerokości płatka: {arith_mean_pet_wid}")

# wyznaczamy mediane dla każdej cechy
median_sep_len = f"{data['sep_len'].median():.2f}"
median_sep_wid = f"{data['sep_wid'].median():.2f}"
median_pet_len = f"{data['pet_len'].median():.2f}"
median_pet_wid = f"{data['pet_wid'].median():.2f}"
print(f"Mediana długości kielicha: {median_sep_len}")
print(f"Mediana szerokości kielicha: {median_sep_wid}")
print(f"Mediana długości płatka: {median_pet_len}")
print(f"Mediana szerokości płatka: {median_pet_wid}")

# wyznaczamy odchylenie standardowe dla każdej cechy
standard_dev_sep_len = f"{data['sep_len'].std():.2f}"
standard_dev_sep_wid = f"{data['sep_wid'].std():.2f}"
standard_dev_pet_len = f"{data['pet_len'].std():.2f}"
standard_dev_pet_wid = f"{data['pet_wid'].std():.2f}"
print(f"Odchylenie standardowe długości kielicha: {standard_dev_sep_len}")
print(f"Odchylenie standardowe szerokości kielicha: {standard_dev_sep_wid}")
print(f"Odchylenie standardowe długości płatka: {standard_dev_pet_len}")
print(f"Odchylenie standardowe szerokości płatka: {standard_dev_pet_wid}")

# wyznaczamy dolne i górne kwartyle dla każdej cechy
low_quart_sep_len = f"{data['sep_len'].quantile(0.25):.2f}"
high_quart_sep_len = f"{data['sep_len'].quantile(0.75):.2f}"
low_quart_sep_wid = f"{data['sep_wid'].quantile(0.25):.2f}"
high_quart_sep_wid = f"{data['sep_wid'].quantile(0.75):.2f}"
low_quart_pet_len = f"{data['pet_len'].quantile(0.25):.2f}"
high_quart_pet_len = f"{data['pet_len'].quantile(0.75):.2f}"
low_quart_pet_wid = f"{data['pet_wid'].quantile(0.25):.2f}"
high_quart_pet_wid = f"{data['pet_wid'].quantile(0.75):.2f}"
print(f"Dolny kwartyl długości kielicha: {low_quart_sep_len}, górny kwartyl: {high_quart_sep_len}")
print(f"Dolny kwartyl szerokości kielicha: {low_quart_sep_wid}, górny kwartyl: {high_quart_sep_wid}")
print(f"Dolny kwartyl długości płatka: {low_quart_pet_len}, górny kwartyl: {high_quart_pet_len}")
print(f"Dolny kwartyl szerokości płatka: {low_quart_pet_wid}, górny kwartyl: {high_quart_pet_wid}")
