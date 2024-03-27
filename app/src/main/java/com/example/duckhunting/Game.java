
package com.example.duckhunting;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

public class Game {
    private Rect huntingRect;
    private int deltaTime;
    private int point =0;
    private Rect duckRect;

    private int duckWidth;

    private int duckHeight;

    private float duckSpeed;

    private boolean duckShot;
    private Random random;
    private Point cannonCenter;
    private int cannonRadius;
    private int barreLength;
    private int barreRadius;
    private float cannonAngle;
    //Manage Bullet
    private Point bulletCenter;
    private int bulletRadius;
    private boolean bulletFired;
    private float bulletAngle;
    private float bulletSpeed;
    private int highScore=0;
    private float originalDuckSpeed;
    private int timeElapsed; // Thời gian đã trôi qua
    private final int DUCK_SPEED_BOOST_INTERVAL = 10; // Khoảng thời gian để tăng tốc độ của vịt (giây)

    public Game(Rect duckRect, float duckSpeed,int bulletRadius, float bulletSpeed){
        setDuckRect(duckRect);
        setDuckSpeed(duckSpeed);
        setDuckShot(false);
        setBulletRadius(bulletRadius);
        setBulletSpeed(bulletSpeed);
        bulletFired=false;
        random = new Random();
        cannonAngle=(float)Math.PI/4;
        originalDuckSpeed=duckSpeed;
    }
    public void moveDuck(){
        if(!duckShot) {
            duckRect.left -= duckSpeed * deltaTime;
            duckRect.right -= duckSpeed * deltaTime;
        }else{
            duckRect.top += 5*duckSpeed*deltaTime;
            duckRect.bottom += 5*duckSpeed*deltaTime;
        }
    }
    public boolean duckOffScreen(){
        return duckRect.right<0||duckRect.left>huntingRect.right||duckRect.top>huntingRect.bottom||duckRect.bottom<0;
    }
    public Rect getDuckRect(){
        return duckRect;
    }

    public void setDuckRect(Rect duckRect) {
        this.duckRect = duckRect;
        setDuckWidth(duckRect.right-duckRect.left);
        setDuckHeight(duckRect.bottom-duckRect.top);
    }

    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }

    public void setDuckHeight(int duckHeight) {
        this.duckHeight = duckHeight;
    }

    public void setDuckShot(boolean duckShot) {
        this.duckShot = duckShot;
    }

    public void setDuckSpeed(float duckSpeed) {
        this.duckSpeed = duckSpeed;
    }

    public void setDuckWidth(int duckWidth) {
        this.duckWidth = duckWidth;
    }

    public boolean isDuckShot() {
        return duckShot;
    }

    public float getDuckSpeed() {
        return duckSpeed;
    }

    public int getDeltaTime() {
        return deltaTime;
    }

    public int getDuckHeight() {
        return duckHeight;
    }

    public int getDuckWidth() {
        return duckWidth;
    }

    public void setHuntingRect(Rect huntingRect) {
        this.huntingRect = huntingRect;
    }

    public Rect getHuntingRect() {
        return huntingRect;
    }

    public Point getCannonCenter() {
        return cannonCenter;
    }

    public int getCannonRadius() {
        return cannonRadius;
    }

    public int getBarreLength() {
        return barreLength;
    }

    public int getBarreRadius() {
        return barreRadius;
    }

    public float getCannonAngle() {
        return cannonAngle;
    }

    public void setCannonAngle(float cannonAngle) {
        if (cannonAngle>=0&&cannonAngle<=Math.PI/2)
            this.cannonAngle=cannonAngle;
        else if(cannonAngle<0)
            this.cannonAngle=0;
        else
            this.cannonAngle=(float)Math.PI/2;
        if(!isBulletFired())
            loadBullet();
    }
    public void setCannon(Point cannonCenter, int cannonRadius,int barreLength,int barreRadius){
        if(cannonCenter!=null && cannonRadius>0 && barreLength>0){
            this.cannonCenter=cannonCenter;
            this.cannonRadius=cannonRadius;
            this.barreLength=barreLength;
            this.barreRadius=barreRadius;
            this.bulletCenter=new Point(
                    (int)(cannonCenter.x+cannonRadius+Math.cos(cannonAngle)),(int)(cannonCenter.y-cannonRadius*Math.sin(cannonAngle))
            );
        }

    }

    public Point getBulletCenter() {
        return bulletCenter;
    }

    public int getBulletRadius() {
        return bulletRadius;
    }

    public void setBulletRadius(int bulletRadius) {
        if(bulletRadius>0)
            this.bulletRadius = bulletRadius;
    }

    public void setBulletSpeed(float bulletSpeed) {
        if(bulletSpeed>0)
            this.bulletSpeed = bulletSpeed;
    }

    public boolean isBulletFired() {
        return bulletFired;
    }
    public void fireBullet(){
        bulletFired=true;
        bulletAngle=cannonAngle;
    }
    public void moveBullet()
    {
        bulletCenter.x+=bulletSpeed*Math.cos(bulletAngle)*deltaTime;
        bulletCenter.y-=bulletSpeed*Math.sin(bulletAngle)*deltaTime;
    }
    public boolean bulletOffScreen(){
        return bulletCenter.x-bulletRadius>huntingRect.right||bulletCenter.y+bulletRadius<0;
    }
    public void loadBullet(){
        bulletFired=false;
        bulletCenter.x=(int)(cannonCenter.x+cannonRadius*Math.cos(cannonAngle));
        bulletCenter.y=(int)(cannonCenter.y-cannonRadius*Math.sin(cannonAngle));
    }
    public void startDuckFromRightToTopHalf(){
        duckRect.left=huntingRect.right;
        duckRect.right=duckRect.left+duckWidth;
        duckRect.top=random.nextInt(huntingRect.bottom/2);
        duckRect.bottom=duckRect.top+duckHeight;
    }
    public boolean duckHit(){
        return duckRect.intersects(bulletCenter.x-bulletRadius,bulletCenter.y-bulletRadius,bulletCenter.x+bulletRadius,bulletCenter.y+bulletRadius);
    }
    public void yourPoint() {
        point++;
    }
    public int getPoint() {
        return point;
    }
    public void reset() {
        point = 0;
        loadBullet();
        timeElapsed = 0;
        setDuckSpeed(originalDuckSpeed);

        startDuckFromRightToTopHalf();
        duckShot = false;
    }

    public void setHighScore(int n){
        highScore=n;
    }
    public int getHighScore(){
        return highScore;
    }
    public void updateTimeElapsed(int deltaTime) {
        timeElapsed += deltaTime;


        if (timeElapsed >= DUCK_SPEED_BOOST_INTERVAL * 1000) {
            timeElapsed = 0;


            duckSpeed *= 2;
        }
    }
}