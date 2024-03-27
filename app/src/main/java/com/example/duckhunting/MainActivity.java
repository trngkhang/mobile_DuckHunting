package com.example.duckhunting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.SoundPool;
import android.os.Bundle;

import java.util.Timer;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends Activity {
    private GameView gameView;
    private GestureDetector gestureDetector;
    private Game game;
    private SoundPool soundPool;
    private int fireSoundId;
    private int hitSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        int statusBarHeight=0;
        int statusBarId=res.getIdentifier("status_bar_height","dimen","android");
        if(statusBarId>0)
            statusBarHeight=res.getDimensionPixelOffset(statusBarId);
        Point size= new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        gameView= new GameView(this,size.x,size.y-statusBarHeight);
        setContentView(gameView);
        Timer gameTimer = new Timer();
        gameTimer.schedule(new GameTimerTask(gameView),0,GameView.DELTA_TIME);
        TouchHandler touchHandler = new TouchHandler();
        game = gameView.getGame();
        gestureDetector = new GestureDetector(this, touchHandler);
        gestureDetector.setOnDoubleTapListener(touchHandler);
        SoundPool.Builder poolBuilder= new SoundPool.Builder();
        poolBuilder.setMaxStreams(2);
        soundPool=poolBuilder.build();
        fireSoundId=soundPool.load(this,R.raw.cannon_fire,1);
        hitSoundId=soundPool.load(this,R.raw.duck_hit,1);
    }
    public void playHitSound(){
        soundPool.play(hitSoundId,1.0f,1.0f,1,0,1.0f);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class TouchHandler extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            if(!game.isBulletFired()) {
                game.fireBullet();
                soundPool.play(fireSoundId,1.0f,1.0f,1,0,1.0f);
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            updateCanon(e);
            return  true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            updateCanon(e2);
            return true;
        }
        public void updateCanon(MotionEvent event){
            float x = event.getX() - game.getCannonCenter().x;
            float y = game.getCannonCenter().y-event.getY();
            float angle =(float)Math.atan2(y,x);
            game.setCannonAngle(angle);

        }

    }
}