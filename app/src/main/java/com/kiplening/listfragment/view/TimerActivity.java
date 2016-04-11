package com.kiplening.listfragment.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kiplening.listfragment.R;
import com.kiplening.listfragment.common.MyApplication;
import com.kiplening.listfragment.util.DisplayUtil;
import com.kiplening.listfragment.util.TomatoUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MOON on 4/6/2016.
 */
public class TimerActivity extends Activity{

    private TextView pinnedTodo, timerDisplay;
    boolean flag = true, isClose = false;
    private RadioButton isDone;
    private CheckBox controller;
    private Button timerBack,voiceControl;
    private Bitmap timerViewBmp;
    private ImageView timerController;
    private LinearLayout checkPinnedTodo;
    private TomatoUtil tomatoUtil;
    private MyApplication.Tomato tomato = MyApplication.getTomato();
    private Paint paint = new Paint();

    private Thread thread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //todo update UI
                    DisplayMetrics dm =getResources().getDisplayMetrics();
                    float w_screen = dm.widthPixels;
                    //int h_screen = dm.heightPixels;
                    float width = DisplayUtil.px2dip(getApplicationContext(), w_screen);
                    float height = 200;
                    paint.setStyle(Paint.Style.FILL); //绘制空心圆
                    paint.setColor(Color.RED);
                    float cx = width/2;//DisplayUtil.dip2px(this, width/2);
                    float cy = height/2;//DisplayUtil.dip2px(this, height/2);
                    final Canvas canvas = new Canvas(timerViewBmp);

                    final RectF rectF = new RectF(cx-72, cy-72, cx+72, cy+72);
                    TomatoInfo info = (TomatoInfo) msg.obj;
                    canvas.drawArc(rectF, 0, ((info.count * 6) / 25), true, paint);

                    paint.setColor(Color.WHITE);
                    //final RectF rectFInside = new RectF(cx-72, cy-72, cx+72, cy+72);
                    canvas.drawCircle(cx, cy, 68, paint);
                    //canvas.drawArc(rectFInside, 0, ((info.count*6)/25), true, paint);

                    timerController.setImageBitmap(timerViewBmp);

                    int remainTime = (int) (25*60000 - Calendar.getInstance().getTimeInMillis()
                            + info.startTime.getTimeInMillis());
                    timerDisplay.setText(remainTime/60000+":"+(remainTime%60000)/1000);
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_layout);

        controller = (CheckBox) findViewById(R.id.controller);
        voiceControl = (Button) findViewById(R.id.voice_button);
        pinnedTodo = (TextView) findViewById(R.id.pinned_todo);
        isDone = (RadioButton) findViewById(R.id.timer_task_is_done);
        timerBack = (Button) findViewById(R.id.timer_back);
        timerController = (ImageView) findViewById(R.id.timer_controller);
        checkPinnedTodo = (LinearLayout) findViewById(R.id.check_Pinned_todo);
        timerDisplay = (TextView) findViewById(R.id.timer_display);

        controller.setOnClickListener(listener);
        timerBack.setOnClickListener(listener);
        voiceControl.setOnClickListener(listener);
        checkPinnedTodo.setOnClickListener(listener);

        paint.setStrokeWidth(4);
        paint.setAntiAlias(true); //消除锯齿
        //paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClose = true;
        handler.removeCallbacksAndMessages(null);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.timer_back:
                    //实现返回功能
                    finish();
                    break;
                case R.id.voice_button:
                    //todo:开关声音，并更新图片

                    break;
                case R.id.controller:
                    //TODO:开始或drop一个番茄
                    CheckBox view = (CheckBox) v;
                    MyApplication.Tomato tomato = MyApplication.getTomato();
                    tomatoUtil = new TomatoUtil();
                    if (view.isChecked()) {
                        if (!tomato.isStarted) {
                            if(tomatoUtil.startTomato()){
                                timerAction();
                            }
                        }
                    }
                    else {
                        if (tomato.isStarted) {
                            tomatoUtil.dropTomato();
                            initView();
                        }
                    }
                    break;
                case R.id.check_Pinned_todo:
                    //todo:完成当前pinned 到最前面的 todo。

                    break;
                default:break;
            }
        }
    };

    private void timerAction() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Calendar startTime = MyApplication.getTomato().startTime;
                final TomatoInfo info = new TomatoInfo(startTime);
                info.count = (Calendar.getInstance().
                        getTimeInMillis()-info.startTime.getTimeInMillis())/1000;
                flag = true;
                System.out.println(info.count);
                handler.obtainMessage(1,info).sendToTarget();
                Timer timer = new Timer(); //设置定时器
                while (Calendar.getInstance().getTimeInMillis()
                        < startTime.getTimeInMillis() + 25*60*1000 && tomato.isStarted){
                    if (isClose){
                        break;
                    }
                    if (flag){
                        flag = false;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() { //发送一个更新UI的Message
                                info.count = (Calendar.getInstance().
                                        getTimeInMillis()-info.startTime.getTimeInMillis())/1000;
                                flag = true;
                                System.out.println(info.count);
                                handler.obtainMessage(1,info).sendToTarget();
                            }
                        },1000); //设置1秒的时长,刷新一次进度。
                    }
                }
            }
        };
        thread.start();
    }

    private void initView() {
        DisplayMetrics dm =getResources().getDisplayMetrics();
        float w_screen = dm.widthPixels;
        //int h_screen = dm.heightPixels;
        float width = DisplayUtil.px2dip(this, w_screen);
        float height = 200;
        Log.i("width", width + " ");
        timerViewBmp = Bitmap.createBitmap((int)width,
                (int)height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(timerViewBmp);
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE); //绘制空心圆
        float cx = width/2;//DisplayUtil.dip2px(this, width/2);
        float cy = height/2;//DisplayUtil.dip2px(this, height/2);
        canvas.drawCircle(cx, cy, 70, paint);
        timerController.setImageBitmap(timerViewBmp);
        timerDisplay.setText("25:00");

        if (tomato.isStarted) {
            controller.setChecked(true);
            timerAction();
        }
        else {
            controller.setChecked(false);
            timerDisplay.setText("25:00");
        }
    }
    class TomatoInfo{
        long count;
        Calendar startTime;
        TomatoInfo(Calendar t){
            this.startTime = t;
            count = 0;
        }
    }
}
