import cv2
from sympy import true

video_path = "data/videos/chicken_bluePLATE2.mp4"
vidcap = cv2.VideoCapture(video_path)

count = 540
success = true

# tmp폴더 yolov5안에 생성하고 코드 실행하자
while success:
  success,image = vidcap.read()
  if success:
    image90 = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
    image270 = cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE)
    cv2.imwrite("tmp/chicken_bluePLATE_%d.jpg" % count, image)
    cv2.imwrite("tmp/chicken_bluePLATE_Right%d.jpg" % count, image90)
    cv2.imwrite("tmp/chicken_bluePLATE_Left%d.jpg" % count, image270)
    print("%dth image store" % count)
  
  if cv2.waitKey(10) == 27:                    
      break
  count += 1