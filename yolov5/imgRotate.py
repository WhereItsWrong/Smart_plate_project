import cv2

# tmp폴더 yolov5안에 생성하고 코드 실행하자
for count in range(326, 425):
    image = cv2.imread("black_PLATE_IMG_ADD/black_PLATE_%d.jpg" % count, cv2.IMREAD_COLOR)
    image90 = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
    image270 = cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE)
    cv2.imwrite("black_PLATE_IMG_ADD/black_PLATE_Right%d.jpg" % count, image90)
    cv2.imwrite("black_PLATE_IMG_ADD/black_PLATE_Left%d.jpg" % count, image270)
    print("%dth image store" % count)
    
    count += 1