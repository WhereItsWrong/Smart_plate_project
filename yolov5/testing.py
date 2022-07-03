import os
from pathlib import Path

source = "data/images/"     # 이미지 파일 경로

os.chdir("yolov5/" + source)    # 현재 경로 yolov5/data/images/로 이동

# 이미지 파일들 생성날짜 기준으로 정렬 (마지막이 최신)
files = sorted(os.listdir("."), key=os.path.getmtime)

os.chdir("../../")          # 경로 전전으로 돌아가기 yolov5/

# detect실행 (클래스, x, y, w, h, 신뢰도를 포함한 txt파일 저장)
# (사진 저장되는 경로에 "labels/사진이름.txt" 형태로 저장됨)
# files[-1] # 제일 최신 파일만 detecting
os.system(f"python detect.py --source {source + files[-1]} --weights best_gun_result.pt --save-txt --save-conf")

file = Path(source + files[-1])     # detect한 이미지 파일 경로를 Path형태로 저장

os.chdir("runs/detect/")            # yolov5/runs/detect/로 파일 경로 이동

# 결과 파일들 생성날짜 기준으로 정렬 (마지막이 최신)
exps = sorted(os.listdir("."), key=os.path.getmtime)

os.chdir(exps[-1] + "/labels/")     # 최신 결과폴더의 labels/로 경로 이동
txt = file.stem + ".txt"            # 이미지 파일과 이름이 같은 .txt파일명을 txt에 저장

if os.path.isfile(txt):             # txt가 있는지 검사
    with open(txt, 'r') as f:       # txt파일 읽기 모드로 읽음
        for line in f:              # 내용 한 줄마다 출력
            print(line, end="")