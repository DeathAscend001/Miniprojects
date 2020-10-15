#######################################################

# Screen Recorder
import cv2
import numpy as np
from PIL import ImageGrab
from datetime import datetime

Date = datetime.now().strftime('%m%d%y')
Time = datetime.now().strftime('%H%M%S')
FPS = 15
FourCC = cv2.VideoWriter_fourcc(*'XVID')
Record = cv2.VideoWriter(f'ScreenRecord_{Date}-{Time}.avi', FourCC, FPS, (1920, 1080))

print("Press <Space> or Close the Program to Save")
print("Closing Program by Task Manager may not Save the record")

while True:
    Frame = ImageGrab.grab(bbox=(0, 0, 1920, 1080))
    Mat = np.array(Frame)
    T_Frame = cv2.cvtColor(Mat, cv2.COLOR_BGR2RGB)
    cv2.imshow("Screen", T_Frame)
    Record.write(T_Frame)
    if cv2.waitKey(1) == 32:
        break;

Record.release()
cv2.destroyAllWindows()
