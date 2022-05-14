package scm.cptam3.scmcptam3hotpot;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class GameView extends View {
    private Paint veg1_paint;
    private Paint veg2_paint;
    private Paint meat1_paint;
    private Paint meat2_paint;
    private Paint mush1_paint;
    private Paint mush2_paint;
    private Paint clam1_paint;
    private Paint clam2_paint;

    private Bitmap bmp_vegetable1;
    private Bitmap bmp_vegetable2;
    private Bitmap bmp_mushroom1;
    private Bitmap bmp_mushroom2;
    private Bitmap bmp_meatball1;
    private Bitmap bmp_meatball2;
    private Bitmap bmp_clam1;
    private Bitmap bmp_clam2;
    private Bitmap bmp_plate;

    private float vx_vegetable1=0;
    private float vy_vegetable1=0;
    private float vx_vegetable2=0;
    private float vy_vegetable2=0;
    private float vx_mushroom1=0;
    private float vy_mushroom1=0;
    private float vx_mushroom2=0;
    private float vy_mushroom2=0;
    private float vx_clam1=0;
    private float vy_clam1=0;
    private float vx_clam2=0;
    private float vy_clam2=0;
    private float vx_meatball1=0;
    private float vy_meatball1=0;
    private float vx_meatball2=0;
    private float vy_meatball2=0;
    private float scale = 0.5f;

    public int vegetable_index=set_Rindex();
    public int meatball_index=set_Rindex();
    public int clam_index=set_Rindex();
    public int mushroom_index=set_Rindex();
    public int total_time = (vegetable_index+meatball_index+clam_index+mushroom_index)*2*1000+1000;
    private PointF position_vegetable1 = new PointF(0,0);
    private PointF position_vegetable2 = new PointF(0,0);
    private PointF position_mushroom1 = new PointF(0,0);
    private PointF position_mushroom2 = new PointF(0,0);
    private PointF position_clam1 = new PointF(0,0);
    private PointF position_clam2 = new PointF(0,0);
    private PointF position_meatball1 = new PointF(0,0);
    private PointF position_meatball2 = new PointF(0,0);

    public boolean vegetable1_isStop=false;
    public boolean vegetable2_isStop=false;
    public boolean meatball1_isStop=false;
    public boolean meatball2_isStop=false;
    public boolean mushroom1_isStop=false;
    public boolean mushroom2_isStop=false;
    public boolean clam1_isStop=false;
    public boolean clam2_isStop=false;

    public boolean setGameover = false;

    private int d = 100;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        bmp_vegetable1= BitmapFactory.decodeResource(this.getResources(),R.drawable.vegetable);
        bmp_vegetable2= BitmapFactory.decodeResource(this.getResources(),R.drawable.vegetable);
        bmp_mushroom1= BitmapFactory.decodeResource(this.getResources(),R.drawable.mushroom);
        bmp_mushroom2= BitmapFactory.decodeResource(this.getResources(),R.drawable.mushroom);
        bmp_meatball1= BitmapFactory.decodeResource(this.getResources(),R.drawable.meatball);
        bmp_meatball2= BitmapFactory.decodeResource(this.getResources(),R.drawable.meatball);
        bmp_clam1= BitmapFactory.decodeResource(this.getResources(),R.drawable.clam);
        bmp_clam2= BitmapFactory.decodeResource(this.getResources(),R.drawable.clam);
        bmp_plate= BitmapFactory.decodeResource(this.getResources(),R.drawable.plate);
        position_vegetable1.x=-bmp_vegetable1.getWidth()/2;
        position_vegetable1.y=-bmp_vegetable1.getWidth()/2-d;
        position_vegetable2.x=-bmp_vegetable2.getWidth()/2+d;
        position_vegetable2.y=-bmp_vegetable2.getWidth()/2+d;
        position_mushroom1.x=-bmp_mushroom1.getWidth()/2+d;
        position_mushroom1.y=-bmp_mushroom1.getWidth()/2-d;
        position_mushroom2.x=-bmp_mushroom2.getWidth()/2;
        position_mushroom2.y=-bmp_mushroom2.getWidth()/2+d;
        position_meatball1.x=-bmp_meatball1.getWidth()/2+d;
        position_meatball1.y=-bmp_meatball1.getWidth()/2;
        position_meatball2.x=-bmp_meatball2.getWidth()/2-d;
        position_meatball2.y=-bmp_meatball2.getWidth()/2-d;
        position_clam1.x=-bmp_clam1.getWidth()/2-d;
        position_clam1.y=-bmp_clam1.getWidth()/2;
        position_clam2.x=-bmp_clam2.getWidth()/2-d;
        position_clam2.y=-bmp_clam2.getWidth()/2+d;

        veg1_paint = new Paint();
        veg1_paint.setAlpha(255);
        veg2_paint = new Paint();
        veg2_paint.setAlpha(255);
        mush1_paint = new Paint();
        mush1_paint.setAlpha(255);
        mush2_paint = new Paint();
        mush2_paint.setAlpha(255);
        meat1_paint = new Paint();
        meat1_paint.setAlpha(255);
        meat2_paint = new Paint();
        meat2_paint.setAlpha(255);
        clam1_paint = new Paint();
        clam1_paint.setAlpha(255);
        clam2_paint = new Paint();
        clam2_paint.setAlpha(255);

    }

    protected int set_Rindex(){

        Random r = new Random();
        int  index = r.nextInt(3);
        return index;
    }

    protected void onDraw(Canvas canvas){

        int cx = this.getWidth() / 2;
        int cy = this.getHeight() / 2;
        canvas.translate(cx,cy);
        canvas.drawBitmap(bmp_plate,-bmp_plate.getWidth()/2,-bmp_plate.getHeight()/2,null);
        canvas.drawBitmap(bmp_vegetable1,position_vegetable1.x,position_vegetable1.y,veg1_paint );
        canvas.drawBitmap(bmp_vegetable2,position_vegetable2.x,position_vegetable2.y,veg2_paint );
        canvas.drawBitmap(bmp_mushroom1,position_mushroom1.x,position_mushroom1.y,mush1_paint );
        canvas.drawBitmap(bmp_mushroom2,position_mushroom2.x,position_mushroom2.y,mush2_paint);
        canvas.drawBitmap(bmp_meatball1,position_meatball1.x,position_meatball1.y,meat1_paint);
        canvas.drawBitmap(bmp_meatball2,position_meatball2.x,position_meatball2.y,meat2_paint);
        canvas.drawBitmap(bmp_clam1,position_clam1.x,position_clam1.y,clam1_paint );
        canvas.drawBitmap(bmp_clam2,position_clam2.x,position_clam2.y,clam2_paint );

        //invalidate();
    }
    public void vegetable1_update(float ax,float ay){                   //the structure of xxx_update() are from tilting view - topic07
        vx_vegetable1 += ax * scale;
        vy_vegetable1 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_vegetable1.x += vx_vegetable1;
        position_vegetable1.y += vy_vegetable1;

        if (position_vegetable1.y > -bmp_vegetable1.getWidth()/2-d) {
            position_vegetable1.y = -bmp_vegetable1.getWidth()/2-d;
            vx_vegetable1 = ax = 0;
            vy_vegetable1 = ay = 0;
        } else if (position_vegetable1.y < -500) {
            vegetable1_isStop = true;

            position_vegetable1.y = -500;
            vx_vegetable1 = ax = 0;
            vy_vegetable1 = ay = 0;
            vegetable_index--;

            if(vegetable_index<0){
                setGameover = true;
            }

            veg1_paint.setAlpha(0);
        }
        invalidate();

    }

    public void vegetable2_update(float ax,float ay){
        vx_vegetable2 += ax * scale;
        vy_vegetable2 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_vegetable2.x += vx_vegetable2;
        position_vegetable2.y += vy_vegetable2;

        if (position_vegetable2.y < -bmp_vegetable2.getWidth()/2+d) {
            position_vegetable2.y = -bmp_vegetable2.getWidth()/2+d;
            position_vegetable2.x = -bmp_vegetable2.getWidth()/2+d;
            vx_vegetable2 = ax = 0;
            vy_vegetable2 = ay = 0;
        } else if (position_vegetable2.y > 220) {
            vegetable2_isStop = true;

            position_vegetable2.y = 220;
            position_vegetable2.x = 220;
            vx_vegetable2 = ax = 0;
            vy_vegetable2 = ay = 0;
            vegetable_index--;

            if(vegetable_index<0){
                setGameover = true;
            }
            veg2_paint.setAlpha(0);
        }
        invalidate();

    }

    public void meatball1_update(float ax,float ay){
        vx_meatball1 += ax * scale;
        vy_meatball1 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_meatball1.x += vx_meatball1;
        position_meatball1.y += vy_meatball1;

        if (position_meatball1.x < -bmp_meatball1.getWidth()/2+d) {
            position_meatball1.x = -bmp_meatball1.getWidth()/2+d;
            vx_meatball1 = ax = 0;
            vy_meatball1 = ay = 0;
        } else if (position_meatball1.x > 360) {
            meatball1_isStop = true;

            position_meatball1.x = 360;
            vx_meatball1 = ax = 0;
            vy_meatball1 = ay = 0;
            meatball_index--;

            if(meatball_index<0){
                setGameover = true;
            }
            meat1_paint.setAlpha(0);
        }
        invalidate();

    }

    public void meatball2_update(float ax,float ay){
        vx_meatball2 += ax * scale;
        vy_meatball2 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_meatball2.x += vx_meatball2;
        position_meatball2.y += vy_meatball2;

        if (position_meatball2.x > -bmp_meatball2.getWidth()/2-d) {
            position_meatball2.x = -bmp_meatball2.getWidth()/2-d;
            position_meatball2.y = -bmp_meatball2.getWidth()/2-d;
            vx_meatball2 = ax = 0;
            vy_meatball2 = ay = 0;
        } else if (position_meatball2.x < -370) {
            meatball2_isStop = true;

            position_meatball2.x = -370;
            position_meatball2.y = -370;
            vx_meatball2 = ax = 0;
            vy_meatball2 = ay = 0;
            meatball_index--;

            if(meatball_index<0){
                setGameover = true;
            }
            meat2_paint.setAlpha(0);
        }
        invalidate();

    }

    public void mushroom1_update(float ax,float ay){
        vx_mushroom1 += ax * scale;
        vy_mushroom1 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_mushroom1.x += vx_mushroom1;
        position_mushroom1.y += vy_mushroom1;

        if (position_mushroom1.x < -bmp_mushroom1.getWidth()/2+d) {
            position_mushroom1.x = -bmp_mushroom1.getWidth()/2+d;
            position_mushroom1.y = -bmp_mushroom1.getWidth()/2-d;
            vx_mushroom1 = ax = 0;
            vy_mushroom1 = ay = 0;
        } else if (position_mushroom1.x > 240) {
            mushroom1_isStop = true;

            position_mushroom1.x = 240;
            position_mushroom1.y = -370;
            vx_mushroom1 = ax = 0;
            vy_mushroom1 = ay = 0;
            mushroom_index--;

            if(mushroom_index<0){
                setGameover = true;
            }
            mush1_paint.setAlpha(0);
        }
        invalidate();

    }

    public void mushroom2_update(float ax,float ay){
        vx_mushroom2 += ax * scale;
        vy_mushroom2 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_mushroom2.x += vx_mushroom2;
        position_mushroom2.y += vy_mushroom2;

        if (position_mushroom2.y < -bmp_mushroom2.getWidth()/2+d) {
            position_mushroom2.y = -bmp_mushroom2.getWidth()/2+d;
            vx_mushroom2 = ax = 0;
            vy_mushroom2 = ay = 0;
        } else if (position_mushroom2.y > 360) {
            mushroom2_isStop = true;

            position_mushroom2.y = 360;
            vx_mushroom2 = ax = 0;
            vy_mushroom2 = ay = 0;
            mushroom_index--;

            if(mushroom_index<0){
                setGameover = true;
            }
            mush2_paint.setAlpha(0);
        }
        invalidate();

    }

    public void clam1_update(float ax,float ay){
        vx_clam1 += ax * scale;
        vy_clam1 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_clam1.x += vx_clam1;
        position_clam1.y += vy_clam1;

        if (position_clam1.x > -bmp_clam1.getWidth()/2-d) {
            position_clam1.x = -bmp_clam1.getWidth()/2-d;
            vx_clam1 = ax = 0;
            vy_clam1 = ay = 0;
        } else if (position_clam1.x < -500) {
            clam1_isStop = true;
            position_clam1.x = -500;
            vx_clam1 = ax = 0;
            vy_clam1 = ay = 0;
            clam_index--;

            if(clam_index<0){
                setGameover = true;
            }
            clam1_paint.setAlpha(0);
        }
        invalidate();

    }

    public void clam2_update(float ax,float ay){
        vx_clam2 += ax * scale;
        vy_clam2 += ay * scale;

        // integrate the velocity over time to determine the distance traveled
        position_clam2.x += vx_clam2;
        position_clam2.y += vy_clam2;

        if (position_clam2.x > -bmp_clam2.getWidth()/2-d) {
            position_clam2.x = -bmp_clam2.getWidth()/2-d;
            position_clam2.y = -bmp_clam2.getWidth()/2+d;
            vx_clam2 = ax = 0;
            vy_clam2 = ay = 0;
        } else if (position_clam2.x < -370) {
            clam2_isStop = true;
            position_clam2.y = 240;
            position_clam2.x = -370;
            vx_clam2 = ax = 0;
            vy_clam2 = ay = 0;
            clam_index--;

            if(clam_index<0){
                setGameover = true;
            }
            clam2_paint.setAlpha(0);
        }
        invalidate();

    }
}
