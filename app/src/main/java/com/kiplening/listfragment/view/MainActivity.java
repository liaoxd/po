package com.kiplening.listfragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kiplening.listfragment.R;
import com.kiplening.listfragment.common.MyApplication;
import com.kiplening.listfragment.util.TomatoUtil;
import com.kiplening.listfragment.view.fragment.MainFragment;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {
    private final int UPDATETEXT = 2;

    private FragmentManager fragmentManager;
    private TextView textView;
    private Button btnSettings;
    private CheckBox ckbStart;

    //用于更新Actionbar上面的时间数据
    private TomatoUtil tomatoUtil;
    private long remainTime;
    private Thread thread;
    private MyApplication.Tomato tomato = MyApplication.getTomato();
    boolean flag = true, isClose = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATETEXT:
                    remainTime = tomato.startTime.getTimeInMillis()+25*60000
                            - Calendar.getInstance().getTimeInMillis();
                    textView.setText(remainTime/60000+":"+(remainTime%60000)/1000);
                    System.out.println(remainTime);
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment,fragment).commit();

        textView = (TextView) findViewById(R.id.timer);
        ckbStart = (CheckBox) findViewById(R.id.start);
        btnSettings = (Button) findViewById(R.id.settings);

        textView.setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.timer:
                    Intent intent = new Intent(MainActivity.this,TimerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.start:
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
                            ckbStart.setChecked(false);
                            textView.setText("25:00");
                        }
                    }
                    break;
                case R.id.settings:
                    Intent setIntent = new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(setIntent);
            }
        }
    };

    private void timerAction() {
        isClose = false;
        if (tomato.isStarted) {
            remainTime = tomato.startTime.getTimeInMillis()+25*60000
                    - Calendar.getInstance().getTimeInMillis();
            textView.setText(remainTime/60000+":"+(remainTime%60000)/1000);

            thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    Calendar startTime = MyApplication.getTomato().startTime;
                    Timer timer = new Timer(); //设置定时器
                    while (Calendar.getInstance().getTimeInMillis() <
                            startTime.getTimeInMillis() + 25*60*1000){
                        if (isClose){
                            break;
                        }
                        if (flag){
                            flag = false;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() { //发送一个更新UI的Message
                                    flag = true;
                                    System.out.println("!!");
                                    handler.obtainMessage(UPDATETEXT).sendToTarget();
                                }
                            },1000); //设置1秒的时长,刷新一次进度。
                        }
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClose = true;
    }
}
