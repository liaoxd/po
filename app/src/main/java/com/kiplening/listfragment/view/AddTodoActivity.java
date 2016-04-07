package com.kiplening.listfragment.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kiplening.listfragment.R;
import com.kiplening.listfragment.common.MyApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MOON on 4/5/2016.
 */
public class AddTodoActivity extends Activity{
    private EditText editText;
    private List<Map> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtodo_activity);

        editText = (EditText) findViewById(R.id.add);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300); //设置300毫秒的时长

        list = MyApplication.mytodo;

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //除了判断当前按键的 keyCode 以外，判定当前的动作。
                    //不然这个方法在 ACTION_DOWN 和ACTION_UP的时候都会被调用
                    //这样会导致多增加一个空的任务。需要多加注意。
                    if (event.getAction() == KeyEvent.ACTION_DOWN){
                        Map map = new HashMap();
                        map.put("task", editText.getText().toString());
                        map.put("check", false);
                        map.put("stick", false);
                        list.add(map);
                        Log.i("todoSize", list.size()+" ");
                        System.out.println(list.size());
                        editText.clearFocus();
                        editText.setText("");
                        finish();
                    }
                    return true;
                }
                return false;
            }
        });

    }
}
