#Autor: Julia Nocuń 247744

import matplotlib.pyplot as plt
import numpy as np

class Plots:

    ## --WYKRES 1-- ##
    @staticmethod
    def plot_training_mse4(history, epochs, filename="training_mse4.png"):
        plt.figure(figsize=(12, 8))
        colors = ['blue', 'red', 'green']

        for i, (key, value) in enumerate(history.items()):
            loss_history = value['loss_history']
            plt.plot(range(3, epochs + 1), loss_history[2:], label=f"Model z ilością warstw ukrytych = {i+1}", color=colors[i])

        plt.title("Błąd MSE - dane treningowe", pad=10)
        plt.xlabel("Epoka", labelpad=10)
        plt.ylabel("Błąd MSE", labelpad=10)
        plt.ylim(15000, 80000)
        plt.legend(fontsize="12")
        plt.grid(True)
        plt.savefig(filename)
        plt.show()

    ## --WYKRES 2-- ##
    @staticmethod
    def plot_testing_mse4(testing_histories, reference_mse, epochs, filename="testing_mse4.png"):
        plt.figure(figsize=(12, 8))
        colors = ['blue', 'red', 'green']
        for i, testing_loss_history in enumerate(testing_histories):
           plt.plot(range(3, epochs + 1), testing_loss_history[2:], label=f"Model z ilością warstw ukrytych = {i+1}", color=colors[i])

        plt.axhline(y=reference_mse, color='black', linestyle='--', label='Wartość błędu MSE dla danych testowych')

        plt.title("Błąd MSE - dane testowe", pad=10)
        plt.xlabel("Epoka", labelpad=10)
        plt.ylabel("Błąd MSE", labelpad=10)
        plt.legend(fontsize="12")
        plt.ylim(15000, 80000)
        plt.legend()
        plt.grid(True)
        plt.savefig(filename)
        plt.show()

    ## --WYKRES 3-- ##
    @staticmethod
    def plot_errors_cdf(models, testing_input, testing_output, filename="errors_cdf.png"):
        plt.figure(figsize=(16, 12))
        colors = ['blue', 'red', 'green']

        for i, model in enumerate(models):
            predictions = model.predict(testing_input)
            errors = np.linalg.norm(predictions - testing_output, axis=1)
            sorted_errors = np.sort(errors)
            ps = np.arange(len(sorted_errors)) / float(len(sorted_errors))
            plt.plot(sorted_errors, ps, label=f"Model z iloscia warstw ukrytych =  {i+1}", color=colors[i])

        all_errors = np.linalg.norm(testing_input - testing_output, axis=1)
        all_sorted_errors = np.sort(all_errors)
        ps_all_errors = np.arange(len(all_sorted_errors)) / float(len(all_sorted_errors))
        plt.plot(all_sorted_errors, ps_all_errors, label='Wszystkie dane testowe', color='black', linestyle='--')

        plt.title("Dystrybuanta błędów dla pomiarów dynamicznych")
        plt.xlabel("Błąd [mm]")
        plt.ylabel("Prawdopodobieństwo skumulowane")
        plt.xscale('log')
        
        xmin = np.min(np.concatenate([sorted_errors, all_sorted_errors]))
        xmax = np.max(np.concatenate([sorted_errors, all_sorted_errors])) + 10
        plt.xlim(xmin, xmax)

        plt.legend()
        plt.grid(True)
        plt.savefig(filename)
        plt.show()

    ## --WYKRES 4-- ##
    @staticmethod
    def plot_point_chart(actual_values, measured_values, corrected_values, filename="plot4.png"):
        plt.figure(figsize=(12,8))
        plt.rcParams.update({'font.size':12})

        plt.scatter(actual_values[:,0], actual_values[:, 1], color='blue', label='Wartości rzeczywiste', zorder=4)
        plt.scatter(corrected_values[:,0], corrected_values[:, 1], color='red', label='Wartości skorygowane', zorder=3)
        plt.scatter(measured_values[:,0], measured_values[:, 1], color='green', label='Wartości zmierzone', zorder=2)

        plt.title("Porównanie wartości rzeczywistych, zmierzonych i skorygowanych")
        plt.xlabel("x [mm]")
        plt.ylabel("y [mm]")
        plt.legend()
        plt.grid(True)
        plt.savefig(filename)
        plt.show()