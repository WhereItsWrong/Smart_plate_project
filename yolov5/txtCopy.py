
# txt파일 경로
path = "annotation_black_PLATE/"

# 기본값 설정
index = 1
cls = "0"
x = y = 0
w = h = "0"

# 416은 Left와 Right를 제외한 txt파일 개수
for index in range(1, 326):
    with open(path + "black_PLATE_" + str(index) + ".txt", 'r') as f:
        for line in f:
            cls, x, y, w, h = line.split()

    with open(path + "black_PLATE_" + "Right" + str(index) + ".txt", 'w') as newFile:
        newFile.write(cls + " " + str(round(1 - float(y), 6)) + " " + x + " " + h + " " + w + "\n")


for index in range(1, 326):
    with open(path + "black_PLATE_" + str(index) + ".txt", 'r') as f:
        for line in f:
            cls, x, y, w, h = line.split()

    with open(path + "black_PLATE_" + "Left" + str(index) + ".txt", 'w') as newFile:
        newFile.write(cls + " " + y + " " + str(round(1 - float(x), 6)) + " " + h + " " + w + "\n")
