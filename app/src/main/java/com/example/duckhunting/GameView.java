package com.example.duckhunting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;
import android.graphics.Canvas;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class GameView extends View {
    private int height;
    private Paint paint;
    private Bitmap duck;
    private Rect duckRect;
    private Game game;
    private int[] TARGETS = {R.drawable.anim_duck0, R.drawable.anim_duck1, R.drawable.anim_duck2, R.drawable.anim_duck0};
    private Bitmap[] ducks;
    private CountDownTimer countDownTimer;
    private int timeLeftInSeconds = 60;
    private GameTimerTask gameTimerTask;
    public static final int DELTA_TIME = 100;
    private int duckFrame;


    public GameView(Context context, int width, int height) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);

        this.height = height;
        ducks = new Bitmap[TARGETS.length];

        for (int i = 0; i < ducks.length; i++)
            ducks[i] = BitmapFactory.decodeResource(getResources(), TARGETS[i]);
        float scale = ((float) width / (ducks[0].getWidth() * 5));
        duckRect = new Rect(0, 0, width / 5, (int) (ducks[0].getHeight() * scale));
        game = new Game(duckRect, 0.05f, 20, 0.2f);
        game.setDuckSpeed(width * 0.00003f);
        game.setBulletSpeed(width * 0.0003f);
        game.setDeltaTime(DELTA_TIME);
        game.setHuntingRect(new Rect(0, 0, width, height));
        game.setCannon(new Point(0, height), width / 30, width / 15, width / 50);
        paint.setColor(0xFF000000);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(game.getBarreRadius());

        startCountDownTimer();
    }

    public Game getGame() {
        return game;
    }

    private void startCountDownTimer() {
        gameTimerTask = new GameTimerTask(this);
        countDownTimer = new CountDownTimer(timeLeftInSeconds * 1000 + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInSeconds = (int) (millisUntilFinished / 1000);
                game.updateTimeElapsed(1000);
                invalidate();
            }

            @Override
            public void onFinish() {
                timeLeftInSeconds = 0;
                invalidate();

                gameTimerTask.cancel();


                showGameOverDialog();
            }
        }.start();
    }



    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Trò chơi kết thúc");
        String message = String.format("Số vịt bắn được là: %d\nKỷ lục : %d", game.getPoint(), game.getHighScore());
        builder.setMessage(message);
        builder.setPositiveButton("Chơi lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                resetGame();
                startCountDownTimer();
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ((MainActivity) getContext()).finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void resetGame() {

        game.reset();
        timeLeftInSeconds = 60;
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Vẽ pháo
        canvas.drawCircle(game.getCannonCenter().x, game.getCannonCenter().y, game.getCannonRadius(), paint);
        // Vẽ góc của pháo
        canvas.drawLine(game.getCannonCenter().x, game.getCannonCenter().y,
                game.getCannonCenter().x + game.getBarreLength() * (float) Math.cos(game.getCannonAngle()),
                game.getCannonCenter().y - game.getBarreLength() * (float) Math.sin(game.getCannonAngle()), paint);
        // Vẽ vịt bay
        duckFrame = (duckFrame + 1) % ducks.length;
        if (game.isDuckShot()) {
            canvas.drawBitmap(ducks[0], null, game.getDuckRect(), paint);
        } else {
            canvas.drawBitmap(ducks[duckFrame], null, game.getDuckRect(), paint);
        }

        if (!game.bulletOffScreen()) {
            canvas.drawCircle(game.getBulletCenter().x, game.getBulletCenter().y, game.getBulletRadius(), paint);
        }

        canvas.drawText("Thời gian còn lại: " + timeLeftInSeconds + " giây", 50, 100, paint);

        canvas.drawText("Số vịt bắn được: " + game.getPoint(), canvas.getWidth() - 600, 100, paint);
        canvas.drawText("kỷ lục : " + game.getHighScore(), canvas.getWidth() - 600, 200, paint);
    }
}