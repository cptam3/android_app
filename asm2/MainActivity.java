package scm.cptam3.scmcptam3hotpot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static Boolean call_losedialog;
    public static Boolean call_windialog;
    private Button start_button;
    private GameActivity GA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        call_losedialog = false;
        call_windialog = false;
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.btn_start);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent1);
            }
        });

    }


    public void LoseDialog(){
        LoseDialog dialog = new LoseDialog();
        dialog.show(getSupportFragmentManager(),"lose dialog");
    }

    public void WinDialog(){
        WinDialog dialog = new WinDialog();
        dialog.show(getSupportFragmentManager(),"win dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(call_losedialog){
            LoseDialog();
            call_losedialog = false;
        }
        if(call_windialog){
            WinDialog();
            call_windialog = false;
        }
    }


}