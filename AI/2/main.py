#Autor: Julia Nocuń 247744

import numpy as np
import tensorflow as tf
import pandas as pd

from reader import DataReader
from plots import Plots

class MSE_testing(tf.keras.callbacks.Callback): #klasa dziedzicząca po tf.keras.callbacks.Callback - służ nam do danych testowych 
    def __init__(self, testing_input, testing_output):
        super().__init__()
        self.testing_input = testing_input  #dane testowe wejściowe
        self.testing_output = testing_output    #dane testowe wyjściowe
        self.testing_mse = []   #inicjalizacja listy do przechowywania wartości MSE dla zbioru testowego

    def on_epoch_end(self, epoch, logs=None):   #funkcja wywoływana na końcu każdej epoki
        predictions = self.model.predict(self.testing_input, verbose=0) #przewidywanie wartości dla zbioru testowego
        mse = tf.keras.losses.MeanSquaredError()(self.testing_output, predictions)  #obliczanie MSE dla przewidywanych wartości
        self.testing_mse.append(mse.numpy())

def model_configuration(hidden_layers, learning_rate, initializer_choice, use_momentum, momentum=None):
    model = tf.keras.Sequential()   #model sekwencyjny służący do budowy MLP
    
    # Sposoby inicjalizacji wag
    initializers = {

        #ru - ustawia wagi w warstwie przy użyciu wartości losowych z równomiernego rozkładu (jednostajnego)
        'ru': tf.keras.initializers.RandomUniform(minval=-1, maxval=1),

        #rn -ustawia wagi w warstwie przy użyciu wartości losowych z rozkładu normalnego (Gaussa).
        #mean: średnia wartość rozkładu (w tym przypadku 0.0).
        #stddev: odchylenie standardowe rozkładu (w tym przypadku 0.05)
        'rn': tf.keras.initializers.RandomNormal(mean=0.0, stddev=0.05),    
    }
    
    if initializer_choice not in initializers:
        raise ValueError("Nieprawidłowy sposób inicjalizacji wag.")
    
    initializer = initializers[initializer_choice]

    #warstwa wejściowa, która zawiera 2 neurony
    model.add(tf.keras.layers.InputLayer(input_shape=(2,)))

    activation_options = {
        's': 'sigmoid',
        'i': 'linear',
        't': 'tanh',
        'r': 'relu',
        'e': 'exponential',
    }

    #warstwy ukryte
    for layer in hidden_layers:
        neurons_count, activation_choice = layer[:-1], layer[-1]    #rozdzielenie liczby neuronów i funkcji aktywacji
        activation = activation_options.get(activation_choice)

        #dodanie warstwy ukrytej z odpowiednią liczbą neuronów i funkcją aktywacji
        model.add(tf.keras.layers.Dense(int(neurons_count), activation=activation, kernel_initializer=initializer))

    #warstwa wyjsciowa
    model.add(tf.keras.layers.Dense(2, activation=tf.keras.activations.linear, kernel_initializer=initializer)) #jeśli nie podamy inicjalizatora domyślnie byłoby GlorotUniform

    #Kompilacja modelu z podaną prędkością uczenia i opcjonalnym użyciem momentum
    if use_momentum:
        optimizer = tf.keras.optimizers.SGD(learning_rate=learning_rate, momentum=momentum) #optymalizator SGD(Stochastic gradient descent) z momentum
    else:
        optimizer = tf.keras.optimizers.Adam(learning_rate=learning_rate)   #optymalizator Adam

    model.compile(optimizer=optimizer, loss=tf.losses.MeanSquaredError(), metrics=['mse'])  #kompilacja modelu z MSE jako funkcją straty
    
    return model

def get_hidden_layers():
    activation_options = {
        's': 'sigmoid',
        'i': 'linear',
        't': 'tanh',
        'r': 'relu',
        'e': 'exponential',
    }

    hidden_layers = []
    hidden_layers_count = int(input("Podaj liczbę warstw ukrytych: "))

    for i in range(hidden_layers_count):
        neurons_count = int(input(f"Podaj liczbę neuronów w warstwie {i+1}: "))

        print("Dostępne funkcje aktywacji:")
        for name, abbreviation in activation_options.items():
            print(f"{abbreviation} - {name}")

        activation_choice = input(f"Wybierz funkcję aktywacji dla warstwy {i+1}: ").lower()
        while activation_choice not in activation_options.keys():
            print("Błąd: Nieprawidłowy wybór funkcji aktywacji.")
            activation_choice = input(f"Wybierz funkcję aktywacji dla warstwy {i+1}: ").lower()

        hidden_layers.append(f"{neurons_count}{activation_choice}")

    learning_rate = float(input("Podaj prędkość uczenia (np. 0.001): "))

    use_momentum = input("Czy chcesz wykorzystać momentum? (tak/nie): ").lower()
    while use_momentum not in ['tak', 'nie']:
        print("Błąd: Nieprawidłowa odpowiedź.")
        use_momentum = input("Czy chcesz wykorzystać momentum? (tak/nie): ").lower()

    use_momentum = True if use_momentum == 'tak' else False

    if use_momentum == True:
        momentum = float(input(f"Podaj wartość momentum: "))
    else:
        momentum = None

    print("Dostępne sposoby inicjalizacji wag:")
    initializers = {
        'ru': 'RandomUniform',
        'rn': 'RandomNormal',
    }
    for name, description in initializers.items():
        print(f"{name} - {description}")

    initializer_choice = input("Wybierz sposób inicjalizacji wag: ").lower()
    while initializer_choice not in initializers.keys():
        print("Błąd: Nieprawidłowy wybór sposobu inicjalizacji wag.")
        initializer_choice = input("Wybierz sposób inicjalizacji wag: ").lower()

    #zwrócenie zebranych danych: warstwy ukryte, prędkość uczenia, sposób inicjalizacji wag, użycie momentum, wartość momentum
    return hidden_layers, learning_rate, initializer_choice, use_momentum, momentum

def learn_evaluate_model(model, training_input, training_output, testing_input, testing_output):
    #konwersja danych do tensorów
    training_input_tensor = tf.convert_to_tensor(training_input.astype('float32'))
    training_output_tensor = tf.convert_to_tensor(training_output.astype('float32')) 
    testing_input_tensor = tf.convert_to_tensor(testing_input.astype('float32'))
    testing_output_tensor = tf.convert_to_tensor(testing_output.astype('float32'))

    #zapytanie użytkownika o liczbę epok - kryterium stopu
    epochs = int(input("Podaj liczbę epok: "))

    testing_mse = MSE_testing(testing_input_tensor, testing_output_tensor)  #inicjalizacja callbacku do monitorowania MSE dla zbioru testowego

    #trenowanie modelu oraz testowanie
    history = model.fit(training_input_tensor, training_output_tensor, epochs=epochs, verbose=1, callbacks=[testing_mse])

    predictions = model.predict(testing_input_tensor)   #przewidywanie wartości dla zbioru testowego

    return history.history['loss'], testing_mse.testing_mse, predictions, epochs

def conduct_experiments(training_input, training_output, testing_input, testing_output):

    if isinstance(training_input, pd.DataFrame):
        training_input = training_input.to_numpy()
    if isinstance(training_output, pd.DataFrame):
        training_output = training_output.to_numpy()
    if isinstance(testing_input, pd.DataFrame):
        testing_input = testing_input.to_numpy()
    if isinstance(testing_output, pd.DataFrame):
        testing_output = testing_output.to_numpy()
    
    num_layers_options = [1, 2, 3]
    
    best_models = {}

    for num_layers in num_layers_options:
        print(f"\nBadanie dla {num_layers} warstw ukrytych:")
        best_mse = float('inf')
        best_model_config = None

        for _ in range(3):  #3 warianty sieci dla danej ilosci warstw ukrytych
            print(f"\nKonfiguracja instancji {_+1}:")
            hidden_layers, learning_rate, initializer_choice, use_momentum, momentum = get_hidden_layers()

            model = model_configuration(hidden_layers, learning_rate, initializer_choice, use_momentum, momentum)
            loss_history, testing_loss_history, predictions, epochs = learn_evaluate_model(model, training_input, training_output, testing_input, testing_output)
            final_mse = testing_loss_history[-1]    #pobranie ostatniej wartości MSE

            if final_mse < best_mse:    #sprawdzenie, czy aktualny model jest lepszy od poprzedniego najlepszego
                best_mse = final_mse    #aktualizacja najlepszego MSE
                best_model_config = {   #zapisanie najlepszej konfiguracji modelu
                    'hidden_layers': hidden_layers,
                    'initializer': initializer_choice,
                    'learning_rate': learning_rate,
                    'use_momentum': use_momentum,
                    'final_mse': final_mse,
                    'loss_history': loss_history,
                    'testing_loss_history': testing_loss_history,
                    'model': model,
                    'predictions': predictions
                }

        best_models[f'{num_layers}_layers'] = best_model_config     #zapisanie najlepszego modelu dla danej liczby warstw

    for key, value in best_models.items():
        print(f"\nNajlepszy model dla {key}:")
        print(f"  Warstwy ukryte: {value['hidden_layers']}")
        print(f"  Inicjalizacja wag: {value['initializer']}")
        print(f"  Prędkość uczenia: {value['learning_rate']}")
        print(f"  Momentum: {value['use_momentum']}")
        print(f"  Końcowe MSE: {value['final_mse']}")

    ######### ----WYKRES1---- #########
    Plots.plot_training_mse4(best_models, epochs)

    ######### ----WYKRES2---- #########
    testing_histories = [value['testing_loss_history'] for value in best_models.values()]
    reference_mse = tf.keras.losses.MeanSquaredError()(testing_output, testing_input).numpy()
    Plots.plot_testing_mse4(testing_histories, reference_mse, epochs)

    ######### ----WYKRES3---- #########
    models = [value['model'] for value in best_models.values()]
    Plots.plot_errors_cdf(models, testing_input, testing_output)
    
    ######### ----WYKRES4---- #########
    best_model_config = min(best_models.values(), key=lambda x: x['final_mse']) #spośród 3 najlepszych modeli wybieramy ten o najmniejszym (najlepszym) MSE
    best_predictions = best_model_config['predictions']    
    Plots.plot_point_chart(testing_output, testing_input, best_predictions)

def main():
    # Ścieżki do plików z danymi
    static_data_directory = r'C:\Users\nocun\OneDrive\Desktop\studia\sem4\SISE\laboratorium\zad2_final\data\static\train_data.csv'  
    dynamic_data_directory = r'C:\Users\nocun\OneDrive\Desktop\studia\sem4\SISE\laboratorium\zad2_final\data\dynamic\test_data.csv'

    data_reader = DataReader(static_data_directory, dynamic_data_directory)
    training_input, training_output, testing_input, testing_output = data_reader.load_and_prepare_data()
    if training_input.empty or training_output.empty or testing_input.empty or testing_output.empty:
        print("Błąd: Zbiory danych treningowych lub testowych są puste.")
        return
    
    choice = input("Wybierz opcję:\n 1. Stwórz tylko jedną sieć i wypisz dla niej MSE testowe i treningowe.\n 2. Przeprowadź eksperyment.\n")
    while choice not in ['1', '2']:
        print("Błąd: Wybierz 1 lub 2.")
        choice = input("Wybierz opcję:\n 1. Stwórz tylko jedną sieć i wypisz dla niej MSE testowe i treningowe.\n 2. Przeprowadź eksperyment.\n")

    if choice == '1':
        hidden_layers, learning_rate, initializer_choice, use_momentum, momentum = get_hidden_layers()
        model = model_configuration(hidden_layers, learning_rate, initializer_choice, use_momentum, momentum)
        loss_history, testing_loss_history, predictions, epochs = learn_evaluate_model(model, training_input, training_output, testing_input, testing_output)
        print(f"\nMSE - dane treningowe: {loss_history[-1]}")
        print(f"MSE - dane testowe: {testing_loss_history[-1]}")
    elif choice == '2':
        conduct_experiments(training_input, training_output, testing_input, testing_output)

if __name__ == "__main__":
    main()