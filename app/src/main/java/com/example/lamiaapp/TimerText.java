package com.example.lamiaapp;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.sql.Time;

import static android.os.SystemClock.sleep;

public class TimerText implements Runnable{
    private TextView lblTimer;

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    private boolean stop = false;
    Activity activity;

    public TimerText(){ }
    public TimerText(Activity activity, TextView lblTimer) {
        this.activity = activity;
        this.lblTimer = (TextView)this.activity.findViewById(R.id.lblTimer);
        //this.lblTimer = lblTimer;
    }

    @Override
    public void run() {
        String auxSec = "00", auxMin = "00", auxHou = "00";
        try{
            do{
                seconds ++;
                if(seconds == 60) {
                    seconds = 0;
                    minutes ++;
                }
                if(seconds < 10)
                    auxSec = "0"+ seconds;
                if(seconds >= 10)
                    auxSec = seconds + "";

                if(minutes == 60) {
                    minutes = 0;
                    hours++;
                    if (hours < 10)
                        auxHou = "0" + hours;
                    if (hours >= 10)
                        auxHou = hours + "";
                }
                if(minutes < 10)
                    auxMin = "0"+ minutes;
                if(minutes >= 10)
                    auxMin = minutes + "";
                sleep(1000);
                lblTimer.setText(auxHou +":" + auxMin  + ":" + auxSec);
            }while(!stop);
        }catch (Exception e) { }
    }

    public void StopTimer() {
        stop = true;
    }
}
