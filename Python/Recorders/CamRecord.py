#########################################
import cv2
from datetime import datetime


Date = datetime.now().strftime('%m%d%y')
Time = datetime.now().strftime('%H%M%S')
# Record
FourCC = cv2.VideoWriter_fourcc(*'XVID')
Record = cv2.VideoWriter(f'CamRecord_{Date}-{Time}.avi', FourCC, 15, (640, 480))

print("Press <Space> or Close the Program to Save")
print("Closing Program by Task Manager may not Save the record")

# Scan
V_Cap = cv2.VideoCapture(0)
while V_Cap.isOpened():
    ret, frame = V_Cap.read()
    Record.write(frame)
    cv2.imshow('Cam', frame)
    if cv2.waitKey(1) == 32:
        break

Record.release()
V_Cap.release()
cv2.destroyAllWindows()
