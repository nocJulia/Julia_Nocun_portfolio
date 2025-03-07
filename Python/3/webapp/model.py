import numpy as np
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler


class ModelManager:
    def __init__(self):
        self.model = None
        self.scaler = None

    def train_model(self, data_points):
        if len(data_points) < 3:
            self.model = None
            self.scaler = None
            return

        x = np.array([[dp.feature1, dp.feature2] for dp in data_points])
        y = np.array([dp.category for dp in data_points])

        self.scaler = StandardScaler()
        x_normalized = self.scaler.fit_transform(x)

        self.model = KNeighborsClassifier(n_neighbors=3)
        self.model.fit(x_normalized, y)

    def predict_category(self, features):
        if self.model is None or self.scaler is None:
            raise ValueError("Insufficient data to make a prediction. Please add more data points.")

        features_normalized = self.scaler.transform([features])
        return self.model.predict(features_normalized)[0]
