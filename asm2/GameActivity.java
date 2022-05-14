package scm.cptam3.scmcptam3hotpot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private GameView Gv;
    private MainActivity Ma;
    private CountDownTimer myCountDownTimer;
    private Timer timer;

    private SensorManager mgr;
    private Sensor sensor;
    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            float ax = (float) (event.values[0]/9.81);
            float ay = (float) (event.values[1]/9.81);

            if(!Gv.vegetable1_isStop){
                if((ay<-0.15&&ax<0.4&&ax>-0.4)||(ay>0&&ax<0.5&&ax>-0.5)) {  //limitation: bigger focus is required to move forward; smaller focus is required to move backward
                    Gv.vegetable1_update(0, 2 * ay);
                }
            }

            if(!Gv.vegetable2_isStop){
               if((ax<-0.15&&ay>0.15)||(ax>0&&ay<0)) {
                    Gv.vegetable2_update(-ax + ay, -ax + ay);
               }
            }
            if(!Gv.meatball1_isStop) {
                if((ax<-0.15&&ay<0.4&&ay>-0.4)||(ax>0&&ay<0.5&&ay>-0.5)){
                    Gv.meatball1_update(-2 * ax, 0);
                }
            }

            if(!Gv.meatball2_isStop){
                if((ax>0.15&&ay<-0.15)||(ax<0&&ay>0)){
                    Gv.meatball2_update(-ax+ay,-ax+ay);
                }
            }

            if(!Gv.mushroom1_isStop){
                if((ax<-0.15&&ay<-0.15)||(ax>0&&ay>0)){
                    Gv.mushroom1_update(-ax-ay,ax+ay);
                }
            }

            if(!Gv.mushroom2_isStop){
                if((ax>-0.4&&ax<0.4&&ay>0.15)||(ax>-0.5&&ax<0.5&&ay<0)){
                    Gv.mushroom2_update(0,2*ay);
                }
            }

            if(!Gv.clam1_isStop){
                if((ax>0.15&&ay<0.4&&ay>-0.4)||(ax<0&&ay<0.5&&ay>-0.5)){
                    Gv.clam1_update(-2*ax,0);
                }
            }

            if(!Gv.clam2_isStop){
                if((ax>0.15&&ay>0.15)||(ax<0&&ay<0)){
                    Gv.clam2_update(-ax-ay,ax+ay);
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };




    private Display display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        TextView amount_meatball = findViewById(R.id.val_meatball);
        TextView amount_mushroom = findViewById(R.id.val_mushroom);
        TextView amount_clam = findViewById(R.id.val_clam);
        TextView amount_vegetable =findViewById(R.id.val_vegetable);
        TextView TimeLeft = findViewById(R.id.timeCounter);
        Gv = findViewById(R.id.gameView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);    //line111-118 are from Tilting Test - Topic07
        sensor = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            Log.d("Tilting", "sensor of interest not detected.");
        }

        display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        amount_vegetable.setText(Gv.vegetable_index+"");
                        amount_meatball.setText(Gv.meatball_index+"");
                        amount_mushroom.setText(Gv.mushroom_index+"");
                        amount_clam.setText(Gv.clam_index+"");

                        if(Gv.setGameover) {
                            Gv.setGameover=false;
                            myCountDownTimer.cancel();
                            timer.cancel();
                            setGameover();
                        }

                        if(Gv.vegetable_index==0&&Gv.meatball_index==0&&Gv.mushroom_index==0&&Gv.clam_index==0){
                            myCountDownTimer.cancel();
                            timer.cancel();
                            setWin();
                        }
                    }
                });
            }
        }, 0, 50);

        myCountDownTimer = new CountDownTimer(Gv.total_time,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeft.setText(millisUntilFinished/1000+"");
            }

            public void onFinish(){
                setGameover();
                timer.cancel();
            }
        }.start();
    }

    protected void setGameover(){
        Ma.call_losedialog = true;

        finish();
    }

    protected void setWin(){
        Ma.call_windialog = true;

        finish();
    }

    protected void onResume() {
        super.onResume();
        if (sensor != null)
            mgr.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensor != null) mgr.unregisterListener(listener, sensor);
        timer.cancel();
        myCountDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        myCountDownTimer.cancel();
    }
}