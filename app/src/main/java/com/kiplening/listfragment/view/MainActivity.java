package com.kiplening.listfragment.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kiplening.listfragment.R;
import com.kiplening.listfragment.view.fragment.MainFragment;

public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager;
    private TextView textView;
    private Button btnStart,btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment,fragment).commit();

        textView = (TextView) findViewById(R.id.timer);
        btnStart = (Button) findViewById(R.id.start);
        btnSettings = (Button) findViewById(R.id.settings);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TimerActivity.class);
                startActivity(intent);
            }
        });
    }
}
