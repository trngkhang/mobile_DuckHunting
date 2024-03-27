package com.example.duckhunting;

import android.graphics.Rect;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;

public class Game {
    private Rect huntingRect;
    private int deltaTime;

    //Manage Duck
    private Rect duckRect;
    private int duckWidth;
    private int duckHeight;
    private float duckSpeed;
    private boolean duckShot;

    //Manage Canon
    private Point cannonCenter;
    private int cannonRadius;
    private int barrelLength;
    private int barrelRadius;
    private float cannonAngle;

    //bullet (vien dan)
    private Point bulletCenter;
    private int bulletRadius;
    private boolean bulletFired;
    private float bulletAngle;
    private float bulletSpeed;

    private Random random;

    public Game(Rect newDuckRect, int newbulletRadius, float newDuckSpeed, float newBulletSpeed) {
        setDuckRect(newDuckRect);
        setDuckSpeed(newDuckSpeed);
        setBulletRadius(newbulletRadius);
        setBulletSpeed(newBulletSpeed);
        bulletCenter = new Point();
        random = new Random();
        bulletFired= false;
        duckShot = false;
        cannonAngle= ( float)Math.PI / 4; // starting cannon angle
    }

    public Rect getHuntingRect() {
        return huntingRect;
    }
    public void setHuntingRect(Rect huntingRect) {
        this.huntingRect = huntingRect;
    }
    public int getDeltaTime() {
        return deltaTime;
    }
    public void setDeltaTime(int newDeltaTime) {
        if(newDeltaTime>0) deltaTime = newDeltaTime;
    }


    //========================DUCK=========================================
    public Rect getDuckRect(){
        return duckRect;
    }
    public void setDuckRect(Rect duckRect) {
        this.duckRect = duckRect;
        setDuckWidth(duckRect.right-duckRect.left);
        setDuckHeight(duckRect.bottom-duckRect.top);
    }

    public int getDuckWidth() {
        return duckWidth;
    }
    public void setDuckWidth(int duckWidth) {
        this.duckWidth = duckWidth;
    }

    public int getDuckHeight() {
        return duckHeight;
    }
    public void setDuckHeight(int duckHeight) {
        this.duckHeight = duckHeight;
    }

    public float getDuckSpeed() {
        return duckSpeed;
    }
    public void setDuckSpeed(float duckSpeed) {
        this.duckSpeed = duckSpeed;
    }

    public boolean getDuckShot(){
        return duckShot;
    }
    public void setDuckShot(boolean duckShot) {
        this.duckShot = duckShot;
    }

    public boolean isDuckShot() {
        return duckShot;
    }

    public void moveDuck(){
        duckRect.left -= duckSpeed * deltaTime;
        duckRect.right -= duckSpeed * deltaTime;
        //Log.w("moveDuck","left="+getDuckRect().left);
    }
    public boolean duckOffScreen(){
        return duckRect.right<0 || duckRect.left > huntingRect.right;
    }
    public void startDuckFromRightTopHalf(){
        duckRect.left=huntingRect.right;
        duckRect.right=duckRect.left+duckWidth;
        duckRect.top= random.nextInt(huntingRect.bottom/2);
        duckRect.bottom=duckRect.top+duckHeight;
    }

    //=========================CANNON======================================
    public Point getCannonCenter() {
        return cannonCenter;
    }
    public int getCannonRadius(){
        return cannonRadius;
    }

    public int getBarrelLength() {
        return barrelLength;
    }

    public int getBarrelRadius() {
        return barrelRadius;
    }

    public float getCannonAngle() {
        return cannonAngle;
    }

    public void setCannonAngle(float cannonAngle) {
        if (cannonAngle>=0 && cannonAngle <= Math.PI/2)
            this.cannonAngle = cannonAngle;
        else if (cannonAngle < 0)
            this.cannonAngle = 0;
        else
            this.cannonAngle = (float)Math.PI/2;
    }
    public void setCannon(Point cannonCenter, int cannonRadius, int barrelLength, int barrelRadius){
        if (cannonCenter!=null && cannonRadius>0 &&barrelLength>0){
            this.cannonCenter = cannonCenter;
            this.cannonRadius = cannonRadius;
            this.barrelLength = barrelLength;
            this.barrelRadius = barrelRadius;
        }
    }

    //==================================BULLET===========================================
    public Point getBulletCenter(){
        return bulletCenter;
    }
    public int getBulletRadius(){
        return bulletRadius;
    }
    public void setBulletRadius(int bulletRadius){this.bulletRadius=bulletRadius;}
    public void setBulletSpeed(float bulletSpeed){this.bulletSpeed=bulletSpeed;}
    public float getCanonAngle(){return cannonAngle;}
    public boolean isBulletFired(){return bulletFired;}
    public void fireBullet(){
        bulletFired=true;
        bulletAngle=cannonAngle;
    }
    public void moveBullet(){
        bulletCenter.x+=bulletSpeed*Math.cos(bulletAngle)*deltaTime;
        bulletCenter.y-=bulletSpeed*Math.sin(bulletAngle)*deltaTime;
    }
    public boolean bulletOffScreen(){
        return bulletCenter.x-bulletRadius>huntingRect.right|| bulletCenter.y-bulletRadius<0;
    }
    public  void loadBullet(){
        bulletFired=false;
        bulletCenter.x=(int)(cannonCenter.x+cannonRadius*Math.cos(cannonAngle));
        bulletCenter.y=(int)(cannonCenter.y-cannonRadius*Math.sin(cannonAngle));
    }
    public boolean duckHit(){
        return duckRect.intersects(bulletCenter.x-bulletRadius,bulletCenter.y-bulletRadius,
                bulletCenter.x+bulletRadius,bulletCenter.y+bulletRadius);
    }

}
