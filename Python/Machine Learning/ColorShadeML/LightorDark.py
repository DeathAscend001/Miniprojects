########################################
# CHECKS IF COLOR IS LIGHTER OR DARKER #
########################################
# Import Classifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn import *
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import sklearn
import pickle

# P-Variables
Epoch = 5000
Labels = []

# Algorithms
def LinearReqression(x_train, x_test, y_train, y_test):
    print('Using Linear Regression...')
    for _ in range(Epoch):
        linear = linear_model.LinearRegression()
        linear.fit(x_train, y_train)
        Score = linear.score(x_test, y_test)
        # Save Data or Load Data
        CurrentBest = 0
        if CurrentBest < Score:
            CurrentBest = Score
            with open('ColorShade.pickle', 'wb') as file:
                pickle.dump(linear, file)
        else:
            saved_model = pickle.load(open('ColorShade.pickle', 'rb'))
    predictions = linear.predict(x_test)
    for x in range(len(predictions)):
        print('Data:', x_test[x], 'Predicted: ', predictions[x], '||', 'Actual: ', y_test[x])
def KNN(x_train, x_test, y_train, y_test, neighbors):
    print('Using KNearestNeighbors...')
    model = KNeighborsClassifier(n_neighbors=neighbors)
    model.fit(x_train, y_train)
    Score = model.score(x_test, y_test)
    predictions = model.predict(x_test)
    for x in range(len(predictions)):
        print('Data: ', x_test[x], 'Predicted: ', Labels[predictions[x]], '||', 'Actual: ', Labels[y_test[x]])
# Functions
def TransformRes(array):
    nList = []
    i = len(Labels)
    for label in array:
        if label not in Labels:
            Labels.append(label)
        nList.append(Labels.index(label))
    return nList

# Data Source
data = pd.read_csv('Data.csv', sep=',')
    # Input
red = list(data['red'])
green = list(data['green'])
blue = list(data['blue'])
X = list(zip(red, green, blue))
    # Output
Y = TransformRes(data['shade'])

# Split Data
x_train, x_test, y_train, y_test = sklearn.model_selection.train_test_split(X, Y, test_size=0.2)
# Train Data
LinearReqression(x_train, x_test, y_train, y_test)
print('--------------------------------------------------')
KNN(x_train, x_test, y_train, y_test, 10)
        
