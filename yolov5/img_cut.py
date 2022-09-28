import cv2
from sympy import true

video_path = "data/videos/black_PLATE1.mp4"
vidcap = cv2.VideoCapture(video_path)

count = 1
success = true

# tmp폴더 yolov5안에 생성하고 코드 실행하자
while success:
  success,image = vidcap.read()
  if success:
    image90 = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
    image270 = cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE)
    cv2.imwrite("tmp/%d.jpg" % count, image)
    cv2.imwrite("tmp/Right%d.jpg" % count, image90)
    cv2.imwrite("tmp/Left%d.jpg" % count, image270)
    print("%dth image store" % count)
  
  if cv2.waitKey(10) == 27:                    
      break
  count += 1