#Autor: Julia Nocuń 247744

import pandas as pd

class DataReader:
    def __init__(self, static_data_directory, dynamic_data_directory):
        self.static_data_directory = static_data_directory
        self.dynamic_data_directory = dynamic_data_directory

    def load_and_prepare_data(self):
        training_input_X = []
        training_input_Y = []
        training_output_X = []
        training_output_Y = []

        static_data = pd.read_csv(self.static_data_directory, header=None, na_values=[''])
        static_data = static_data.dropna()      #pomijamy wiersze, w których znajdują sie puste kolumny
        training_input_X.append(static_data.iloc[:, 0])
        training_input_Y.append(static_data.iloc[:, 1])
        training_output_X.append(static_data.iloc[:, 2])
        training_output_Y.append(static_data.iloc[:, 3])

        training_input = pd.DataFrame({
        0: pd.concat(training_input_X), 
        1: pd.concat(training_input_Y)})

        training_output = pd.DataFrame({
        0: pd.concat(training_output_X), 
        1: pd.concat(training_output_Y)})

        testing_input_X = []
        testing_input_Y = []
        testing_output_X = []
        testing_output_Y = []

        dynamic_data = pd.read_csv(self.dynamic_data_directory, header=None, na_values=[''])
        dynamic_data = dynamic_data.dropna()
        testing_input_X.append(dynamic_data.iloc[:, 0])
        testing_input_Y.append(dynamic_data.iloc[:, 1])
        testing_output_X.append(dynamic_data.iloc[:, 2])
        testing_output_Y.append(dynamic_data.iloc[:, 3])

        testing_input = pd.DataFrame({
        0: pd.concat(testing_input_X), 
        1: pd.concat(testing_input_Y)})

        testing_output = pd.DataFrame({
        0: pd.concat(testing_output_X), 
        1: pd.concat(testing_output_Y)})

        return training_input, training_output, testing_input, testing_output 

