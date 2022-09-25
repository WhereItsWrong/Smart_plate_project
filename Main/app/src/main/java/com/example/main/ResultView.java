// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package com.example.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class ResultView extends View {

    private final static int TEXT_X = 40;
    private final static int TEXT_Y = 35;
    private final static int TEXT_WIDTH = 260;
    private final static int TEXT_HEIGHT = 50;

    //그릇 사각형 객체 선언
    static Rect white_plate_rect = null;
    static Rect black_plate_rect = null;
    static Rect red_plate_rect = null;
    static Rect blue_plate_rect = null;

    //그릇 안에 포함된 음식 객체(이름값) 선언
    static String white_plate_include = null;
    static String black_plate_include = null;
    static String red_plate_include = null;
    static String blue_plate_include = null;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<Result> mResults;

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mResults == null) return;
        for (Result result : mResults) {
            mPaintRectangle.setStrokeWidth(5);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            canvas.drawRect(result.rect, mPaintRectangle);

            Path mPath = new Path();
            RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH,  result.rect.top + TEXT_HEIGHT);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPaintText.setColor(Color.MAGENTA);
            canvas.drawPath(mPath, mPaintText);

            mPaintText.setColor(Color.WHITE);
            mPaintText.setStrokeWidth(0);
            mPaintText.setStyle(Paint.Style.FILL);
            mPaintText.setTextSize(32);
            canvas.drawText(String.format("%s %.2f", PrePostProcessor.mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);

            //plate 텍스트가 포함된 result를 찾아 그릇 rect값에 대입
            if( PrePostProcessor.mClasses[result.classIndex].contains("plate")){
                if(PrePostProcessor.mClasses[result.classIndex].equals("white_plate")){
                    white_plate_rect = result.rect;
                }
                else if(PrePostProcessor.mClasses[result.classIndex].equals("black_plate")){
                    black_plate_rect = result.rect;
                }
                else if(PrePostProcessor.mClasses[result.classIndex].equals("red_plate")){
                    red_plate_rect = result.rect;
                }
                else if(PrePostProcessor.mClasses[result.classIndex].equals("blue_plate")){
                    blue_plate_rect = result.rect;
                }
            }
        }
        //그릇과 음식의 포함관계를 파악하기 위해 중간값을 정하고 그릇안의 내용물이 뭔지 if문을 통해 저장
        //TODO plate_result를 그릇만 포함된 Result 배열로 지정하면 더 좋을것 같으나 우선순위로 인해 후에 처리 예정
        for (Result result : mResults) {
            if (!PrePostProcessor.mClasses[result.classIndex].contains("plate")) {
                int result_middleY = (result.rect.top + result.rect.bottom) / 2;
                int result_middleX = (result.rect.right + result.rect.left) / 2;
                for (Result plate_result: mResults) {
                    if (
                            plate_result.rect.top < result_middleY &&
                                    plate_result.rect.bottom > result_middleY &&
                                    plate_result.rect.right > result_middleX &&
                                    plate_result.rect.left < result_middleX) {
                        if (PrePostProcessor.mClasses[plate_result.classIndex].equals("white_plate")) {
                            white_plate_include = PrePostProcessor.mClasses[result.classIndex];
                        } else if (PrePostProcessor.mClasses[plate_result.classIndex].equals("black_plate")) {
                            black_plate_include = PrePostProcessor.mClasses[result.classIndex];
                        } else if (PrePostProcessor.mClasses[plate_result.classIndex].equals("red_plate")) {
                            red_plate_include = PrePostProcessor.mClasses[result.classIndex];
                        } else if (PrePostProcessor.mClasses[plate_result.classIndex].equals("blue_plate")) {
                            blue_plate_include = PrePostProcessor.mClasses[plate_result.classIndex];
                        }
                    }
                }
            }
        }

    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
