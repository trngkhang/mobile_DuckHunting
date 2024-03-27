package com.example.duckhunting;

import java.util.TimerTask;

public class GameTimerTask extends TimerTask {
    private Game game;
    private GameView gameview;
    public GameTimerTask(GameView gameView){
        this.gameview=gameView;
        game=gameView.getGame();
        game.startDuckFromRightToTopHalf();
    }
    public void run(){
        game.moveDuck();

        if(game.bulletOffScreen())
            game.loadBullet();
        else if(game.isBulletFired())
            game.moveBullet();;
        if(game.duckOffScreen()) {
            game.setDuckShot(false);
            game.startDuckFromRightToTopHalf();
        }else if(game.duckHit()){
            game.setDuckShot(true);
            game.yourPoint();
            if(game.getPoint()>game.getHighScore()){
                game.setHighScore(game.getPoint());
            }
            ((MainActivity)gameview.getContext()).playHitSound();
            game.loadBullet();
        }
        gameview.postInvalidate();
    }
}