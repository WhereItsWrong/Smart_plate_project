import os
from pathlib import Path

source = "data/images/"     # 이미지 파일 경로

os.chdir("./" + source)    # 현재 경로 yolov5/data/images/로 이동

# 이미지 파일들 생성날짜 기준으로 정렬 (마지막이 최신)
files = sorted(os.listdir("."), key=os.path.getmtime)

os.chdir("../../")          # 경로 전전으로 돌아가기 yolov5/

# detect실행 (클래스, x, y, w, h, 신뢰도를 포함한 txt파일 저장)
# (사진 저장되는 경로에 "labels/사진이름.txt" 형태로 저장됨)
# files[-1] # 제일 최신 파일만 detecting
os.system(f"python detect.py --source {source + files[-1]} --weights Chicken_Salmon_blackP.pt --save-txt --save-conf")

file = Path(source + files[-1])     # detect한 이미지 파일 경로를 Path형태로 저장

os.chdir("runs/detect/")            # yolov5/runs/detect/로 파일 경로 이동

# 결과 파일들 생성날짜 기준으로 정렬 (마지막이 최신)
exps = sorted(os.listdir("."), key=os.path.getmtime)

os.chdir(exps[-1] + "/labels/")     # 최신 결과폴더의 labels/로 경로 이동
txt = file.stem + ".txt"            # 이미지 파일과 이름이 같은 .txt파일명을 txt에 저장

foodlist = []
platelist = []

if os.path.isfile(txt):             # txt가 있는지 검사
   with open(txt, 'r') as f:       # txt파일 읽기 모드로 읽음
       index = 1
       for line in f:              # 내용 한 줄마다 출력
           print(line, end="")
           cls, Xmid, Ymid, width, height, conf = line.split()
           x1 = float(Xmid) - float(width)/2
           x2 = float(Xmid) + float(width)/2
           y1 = float(Ymid) - float(height)/2
           y2 = float(Ymid) + float(height)/2
           
           print(f"{x1 : .6f} {y1 : .6f}")
           print(f"{x2 : .6f} {y2 : .6f}")
           index += 1

# class Boundary:
#     '''parameter : (x1, y1, x2, y2)'''
#     def __init__(self, x1, y1, x2, y2):
#         self.x1 = x1
#         self.x2 = x2
#         self.y1 = y1
#         self.y2 = y2
    
#     def isIN(self, InnerBoundary) -> bool:
#         '''A.isIN(B)  A안에 B가 있는지 확인하는지 True/False 반환'''
#         if self.x1 <= InnerBoundary.x1 and InnerBoundary.x2 <= self.x2 and self.y1 <= InnerBoundary.y1 and InnerBoundary.y2 <= self.y2:
#             return True
#         else: return False

