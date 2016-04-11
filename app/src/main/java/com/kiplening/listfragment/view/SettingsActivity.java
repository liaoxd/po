package com.kiplening.listfragment.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kiplening.listfragment.R;

/**
 * Created by MOON on 4/11/2016.
 */
public class SettingsActivity extends Activity{
    private TextView alarmSet, workingSoundSet, pomoGoalSet,
            pomoOtherSet, blockAppsSet, displaySet, aboutSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        alarmSet = (TextView) findViewById(R.id.alarm_sound);
        workingSoundSet = (TextView) findViewById(R.id.working_sound);
        pomoGoalSet = (TextView) findViewById(R.id.pomo_goal);
        pomoOtherSet = (TextView) findViewById(R.id.pomo_other);
        blockAppsSet = (TextView) findViewById(R.id.block_app);
        displaySet = (TextView) findViewById(R.id.settings_display);
        aboutSet = (TextView) findViewById(R.id.about);


        alarmSet.setOnClickListener(listener);
        workingSoundSet.setOnClickListener(listener);
        pomoOtherSet.setOnClickListener(listener);
        pomoGoalSet.setOnClickListener(listener);
        blockAppsSet.setOnClickListener(listener);
        displaySet.setOnClickListener(listener);
        aboutSet.setOnClickListener(listener);


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alarm_sound:

                    break;
                case R.id.working_sound:

                    break;
                case R.id.pomo_goal:

                    break;
                case R.id.pomo_other:

                    break;
                case R.id.block_app:

                    break;
                case R.id.settings_display:

                    break;
                case R.id.about:

                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
