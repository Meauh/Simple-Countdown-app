package com.example.simplecountdown;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static long START_TIME_IN_MILIS = 6000;

    private RelativeLayout mMyLayout;
    private TextView mTextViewCountdown ;
    private TextView mTextViewCountdownMilis ;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonEdit;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMilis = START_TIME_IN_MILIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyLayout = findViewById(R.id.mylayout);
        mTextViewCountdown = findViewById(R.id.text_countdown);
        mTextViewCountdownMilis = findViewById(R.id.text_countdown_milis);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonEdit = findViewById(R.id.button_edit);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning)
                {
                    pauseTimer();
                }
                else
                {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("");
                dialog.setMessage("Pick the period (seconds)");
                final NumberPicker input = new NumberPicker(MainActivity.this);
                input.setMaxValue(999);
                input.setMinValue(5);
                dialog.setView(input);
                dialog.setCancelable(false);
                dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        START_TIME_IN_MILIS = input.getValue()*1000;
                        mTimeLeftInMilis = START_TIME_IN_MILIS;
                        Snackbar.make(mMyLayout,"Picked :" + String.valueOf(input.getValue()),BaseTransientBottomBar.LENGTH_LONG).show();
                        updateTimerText();
                    }
                });
                dialog.setNeutralButton("Dissmis", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        updateTimerText();

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMilis , 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMilis = millisUntilFinished;
                updateTimerText();
                if (mTimeLeftInMilis <=3100 && mTimeLeftInMilis >=3000){
                    Snackbar.make(mMyLayout,"Running",BaseTransientBottomBar.LENGTH_LONG).show();
                    mMyLayout.setBackgroundColor(getResources().getColor(R.color.red_400));
                }
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mMyLayout.setBackgroundColor(getResources().getColor(R.color.green_700));
                Snackbar.make(mMyLayout, "Time's up !" ,BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonEdit.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("resume");
        mButtonReset.setVisibility(View.VISIBLE);

    }
    private void resetTimer() {
        mMyLayout.setBackgroundColor(getResources().getColor(R.color.green_700));
        mTimeLeftInMilis = START_TIME_IN_MILIS;
        updateTimerText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonEdit.setVisibility(View.VISIBLE);

//        mTimerRunning = false ;
//        mButtonStartPause.setText("start");
    }

    private void updateTimerText(){
        int seconds = (int) mTimeLeftInMilis / 1000 % 60 ;
        int minuts = (int) mTimeLeftInMilis / 1000 / 60 ;
        int milis = (int) mTimeLeftInMilis % 1000 / 100;

/*        if (mTimeLeftInMilis == 0) {
            mTextViewCountdown.setText("00:00");
        }
        else{*/
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d" , minuts );
        String milisTimeLeftFormatted = String.format(Locale.getDefault(),"%02d.%01d" , seconds ,milis);
        mTextViewCountdown.setText(timeLeftFormatted);
        mTextViewCountdownMilis.setText(milisTimeLeftFormatted);
    }

}