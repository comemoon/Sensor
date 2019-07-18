package com.example.sensor.chart;


import android.annotation.TargetApi;
import android.os.Build;

import java.time.LocalTime;

@TargetApi(Build.VERSION_CODES.O)
public class Data0 {
    private static final LocalTime localTime = LocalTime.now().withNano(0);
    private static final int hour = localTime.getHour();
    private static final int min = localTime.getMinute();
    private static final int sec = localTime.getSecond();
    private static int i = 0;
    private static String time;
    private String Time;
    private String temperature;
    private String humidness;

    public Data0() {
        int h, m, s;
        String hh,mm,ss;
        s = (sec + i) % 60;
        m = (min + (sec + i) / 60) % 60;
        h = hour + (min + (sec + i) / 60) / 60;
        hh=h+"";
        mm=m+"";
        ss=s+"";
        if(hh.length()==1) hh="0"+h;
        if(mm.length()==1) mm="0"+m;
        if(ss.length()==1) ss="0"+s;
        time = hh + ":" + mm + ":" + ss;
        i++;
        Time = time;
    }

    public String getHumidness() {
        return humidness;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return Time;
    }

    public void setHumidness(String humidness) {
        this.humidness = humidness;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
