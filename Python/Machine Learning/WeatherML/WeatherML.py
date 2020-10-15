######################################
# Weather Predict From Existing Data #
######################################
from sklearn.tree import DecisionTreeRegressor
from sklearn import *
from CustomClassifier import *
import pandas as pd
import sklearn
import numpy

## CustomKNN is from CustomClassifier{My Custom Classifier}

# Functions
def DTR(x_train, x_test, y_train, y_test):
    print('Using Descision Tree')
    DTR = DecisionTreeRegressor()
    DTR.fit(x_train, y_train)
    # Predict Data
    predicted = DTR.predict(x_test)
    for x in range(len(predicted)):
        print('Data: ', x_test[x], 'Predicted : ', predicted[x], 'Actual: ', y_test[x])
    Score = DTR.score(x_test, y_test)
    print('Score: ', Score)

def CustomKNN(x_train, x_test, y_train, y_test):
    print('Using CustomKNN')
    KNN = Algorithms.KNN()
    KNN.fit(x_train, y_train)
    # Predict Data
    predicted = KNN.predict(x_test)
    for x in range(len(predicted)):
        print('Data: ', x_test[x], 'Predicted : ', predicted[x], 'Actual: ', y_test[x])
    print('Score Not Available')

# Data Source
data = pd.read_csv('Compiled_Data(July,26,2020 - Aug, 9, 2020).csv')

# Label
Label_Encoder = preprocessing.LabelEncoder()

# Fix Inputs and Outputs
DateTime = Label_Encoder.fit_transform(list(data['Timestamp']))
Temp = list(data['Temperature'])
R_Humidity = list(data['Relative Humidity'])
CC = list(data['Cloud Cover'])
S_Rad = list(data['Shortwave Radiation'])
D_Shortwave = list(data['Direct Shortwave Radiation'])
ET = list(data['Evapotranspiration'])
W_Speed = list(data['Wind Speed'])
W_Dir = list(data['Wind Direction'])
S_Moisture = list(data['Soil Moisture'])
Precipitation = list(data['Precipitation'])

# Compile Inputs and Outputs
X = list(zip(DateTime, Temp, R_Humidity, S_Rad, D_Shortwave, CC, ET, W_Speed, W_Dir, S_Moisture))
Y = Precipitation

# Split Data into Train and Test
x_train, x_test, y_train, y_test = sklearn.model_selection.train_test_split(X, Y, test_size=0.15)

# Train
DTR(x_train, x_test, y_train, y_test)
CustomKNN(x_train, x_test, y_train, y_test)
