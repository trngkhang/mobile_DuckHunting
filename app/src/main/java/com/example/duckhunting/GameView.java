package com.example.duckhunting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;

public class GameView extends View {
    public int height ;
    private Paint paint;
    private Bitmap duck;
    private Rect duckRect;



    public GameView(Context context, int width, int height){
        super(context);
        this.height=height;
        paint=new Paint();
        paint.setColor(0xFF000000);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10.0f);
        duck= BitmapFactory.decodeResource(getResources(), R.drawable.duck);
        float scale =((float) width/(duck.getWidth()*5));
        duckRect = new Rect(width-width/5,0,width,(int)(duck.getHeight()*scale));
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(0,height,height/10,paint);
        canvas.drawLine(0,height, height/5,height-height/5,paint);
        //draw duck
        canvas.drawBitmap(duck,null, duckRect,paint);
    }
}
