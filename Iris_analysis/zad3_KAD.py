import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score, confusion_matrix
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from itertools import combinations
from pandas.plotting import table  #moduł do rysowania tabeli z pandas

#Poniżej implementujemy algorytm k-means

data_train_csv = pd.read_csv('data.csv', header=None)
data_train = data_train_csv.iloc[:, :-1]

#cechy
features_names = ["Długość działki kielicha", "Szerokość działki kielicha", "Długość płatka", "Szerokość płatka"]

#pary cech
f_combinations = list(combinations(range(len(features_names)), 2))

number_of_iterations = {k: [] for k in range(2, 11)}  #liczba iteracji
values_of_wcss = {k: [] for k in range(2, 11)}  #wartość wcss
table_data_list = []  #lista zawierająca dane do tabelki

#Klasteryzacja
for i, (x_f, y_f) in enumerate(f_combinations, 1):
    x = data_train.iloc[:, x_f]
    y = data_train.iloc[:, y_f]

    fig, ax = plt.subplots(figsize=(9, 7))

    #k=3
    wcss_3 = None
    k_values = list(range(2, 11))

    for k in k_values:
        kmeans = KMeans(n_clusters=k, n_init=10)
        kmeans.fit(data_train[[x_f, y_f]])
        iterations = kmeans.n_iter_  
        wcss = kmeans.inertia_ 

        if k == 3:
            clusters = kmeans.predict(data_train[[x_f, y_f]])

            center = kmeans.cluster_centers_

            #Wykres dla k=3
            scatter = ax.scatter(x, y, c=clusters, cmap='plasma')
            ax.set_title(f'Grupowanie dla k=3, wartość WCSS={round(wcss, 2)}')
            for i in range(0, 3):
                ax.scatter(center[i][0], center[i][1], color='red', marker='s')
            ax.set_xlabel(features_names[x_f])
            ax.set_ylabel(features_names[y_f])

            #Wartość WCSS dla k=3
            wcss_3 = wcss  

        table_data_list.append([features_names[x_f], features_names[y_f], k, iterations, round(wcss, 2)])
        number_of_iterations[k].append(iterations)
        values_of_wcss[k].append(wcss)

    plt.show()

#Tworzenie tabelki w formie obrazka
fig, ax = plt.subplots(figsize=(18, 10))
ax.axis('off')  # Ukrywanie osi

#Ustawienia tabeli
table_columns = ['Cecha X', 'Cecha Y', 'k', 'Ilość iteracji', 'WCSS']
table_df = pd.DataFrame(table_data_list, columns=table_columns)

#Rysowanie tabeli
table(ax, table_df, loc='center', colWidths=[0.2] * len(table_columns))

plt.show()

plt.figure(figsize=(9, 7))
plt.plot(list(values_of_wcss.keys()), list(values_of_wcss.values()), marker='s', linestyle='-', label='Wartość WCSS')
plt.xlabel('Liczba klastrów')
plt.ylabel('Wartość WCSS')
plt.title('Wartości WCSS dla wartości klastrów k od 2 do 10')
plt.xticks(range(2, 11))
plt.grid(True)
plt.show()

#Poniżej implementujemy algorytm kNN

data_train = pd.read_csv('data_train.csv', header=None)
data_test = pd.read_csv('data_test.csv', header=None)
data_train.columns = ["Długość działki kielicha", "Szerokość działki kielicha", "Długość płatka", "Szerokość płatka", "Gatunek"]
data_test.columns = ["Długość działki kielicha", "Szerokość działki kielicha", "Długość płatka", "Szerokość płatka", "Gatunek"]

#Dzielimy dane na cechy i etykiety
X_train_features = data_train.iloc[:, :-1].values
y_train_species = data_train.iloc[:, -1].values
X_test_features = data_test.iloc[:, :-1].values
y_test_species = data_test.iloc[:, -1].values

#Normalizacja
scaler = StandardScaler()
X_train_features = scaler.fit_transform(X_train_features)
X_test_features = scaler.transform(X_test_features)

#Pary cech
all_feature_combinations = [(0, 1, 2, 3), (0, 1), (0, 2), (0, 3), (1, 2), (1, 3), (2, 3)]
feature_names = ["Długość działki kielicha", "Szerokość działki kielicha", "Długość płatka", "Szerokość płatka"]
species_names = {0: 'Setosa', 1: 'Versicolor', 2: 'Virginica'}

#Listy do przechowywania wyników
accuracy_results = []
matrices = []

#Klasyfikacja dla różnych k
for features in all_feature_combinations:
    accuracy_results_subset = []
    matrices_subset = []

    for k in range(1, 16):
        knn = KNeighborsClassifier(n_neighbors=k)   #inicjalizacja klasyfikatora

        #Dopasowanie klasyfikatora do danych treningowych
        knn.fit(X_train_features[:, features], y_train_species)
        y_pred = knn.predict(X_test_features[:, features])

        #Dokładność klasyfikacji
        accuracy = accuracy_score(y_test_species, y_pred)
        accuracy_results_subset.append(accuracy)

        #Macierz pomyłek
        cm = confusion_matrix(y_test_species, y_pred)
        matrices_subset.append(cm)

    #Najlepsza wartość k
    k_best = np.argmax(accuracy_results_subset) + 1

    accuracy_results.append(accuracy_results_subset)
    matrices.append(matrices_subset)

    #Wykres dokładności
    plt.figure(figsize=(12, 8))
    plt.plot(range(1, 16), accuracy_results_subset, marker='o')
    plt.title(f'Dokładność klasyfikacji dla: {", ".join([feature_names[i] for i in features])}')
    plt.xlabel('Wartość k')
    plt.ylabel('Dokładność')
    plt.grid(True)
    plt.show()

    #Macierz pomyłek dla najlepszej wartości k
    best_knn = KNeighborsClassifier(n_neighbors=k_best)
    best_knn.fit(X_train_features[:, features], y_train_species)
    y_pred_best = best_knn.predict(X_test_features[:, features])
    confusion_matrix_best = matrices_subset[np.argmax(accuracy_results_subset)]

    classes = np.unique(y_test_species)
    columns = [f'Faktyczna klasa {species_names[c]}' for c in classes]
    index = [f'Wynik rozpoznania {species_names[c]}' for c in classes]
    confusion_df = pd.DataFrame(confusion_matrix_best, index=index, columns=columns)

    #Macierz pomyłek - wyświetlamy
    fig, ax = plt.subplots(figsize=(12, 6))
    ax.axis('off')
    table = ax.table(cellText=confusion_df.values, colLabels=confusion_df.columns, rowLabels=confusion_df.index,
                     loc='center', cellLoc='center', bbox=[0.135, 0, 0.811, 1])
    table.auto_set_font_size(False)
    table.set_fontsize(10)
    plt.title(f'Macierz pomyłek dla danych cech: {", ".join([feature_names[i] for i in features])}, najlepsze k={k_best}')
    plt.show()

    