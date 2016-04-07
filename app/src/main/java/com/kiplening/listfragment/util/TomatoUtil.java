package com.kiplening.listfragment.util;

import com.kiplening.listfragment.common.MyApplication;

import java.util.Calendar;

/**
 * Created by MOON on 4/6/2016.
 */
public class TomatoUtil {
    private MyApplication.Tomato tomato = MyApplication.getTomato();

    /**
     * 开始一个新的 Tomato
     * @return boolean,
     * 开启成功就返回True
     * 开启不成功返回false
     */
    public boolean startTomato() {
        if (tomato.isStarted) {
            return false;
        }
        else {
            tomato.isStarted = true;
            tomato.startTime = Calendar.getInstance();
            return true;
        }
    }

    /**
     * drop 掉一个tomato
     * @return boolean
     * 成功返回true，否者返回false
     */
    public boolean dropTomato() {
        if (tomato.isStarted) {
            tomato.startTime = null;
            tomato.isStarted = false;
            return true;
        }
        else {
            return false;
        }
    }
}
