package com.kiplening.listfragment.common;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by MOON on 4/5/2016.
 */
public class MyApplication extends Application {
    public static class Tomato{
        public static boolean isStarted = false;
        public static Calendar startTime;
    }
    public static class InstanceHolder{
        public static MyApplication instance = new MyApplication();
        public static Tomato tomato = new Tomato();
    }


    /*
    定义全局变量
     */
    public static List<Map> mytodo = new ArrayList<>();


    /**
     * android应用程序真正入口
     * 此方法在所有activity，service，receiver组件之前调用
     */
    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("Create", "applicationg created");

    }

    /**
     * 此方法方便那些没有context对象的类中使用
     * @return MyApplication实例
     */
    public static MyApplication getApplicationInstance(){
        return InstanceHolder.instance;
    }

    /**
     * 在程序中获取当前的 tomato
     * @return Tomato 信息
     */
    public static Tomato getTomato() {
        return InstanceHolder.tomato;
    }
}
