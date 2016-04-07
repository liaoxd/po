package com.kiplening.listfragment.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kiplening.listfragment.R;
import com.kiplening.listfragment.common.MyApplication;
import com.kiplening.listfragment.view.AddTodoActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MOON on 4/4/2016.
 */
public class TaskListFragment extends Fragment {
    private View view;
    private EditText addTodo;
    private ListView todoList;
    private List<Map> myTodo;
    private MyAdapter adapter;
    private MyApplication.Tomato tomato = MyApplication.getTomato();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_todolist,null);
        addTodo = (EditText) view.findViewById(R.id.add_todo);
        todoList = (ListView) view.findViewById(R.id.todo_list);

        myTodo = MyApplication.mytodo;
        addTodo.setFocusable(false);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.add_todo) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), AddTodoActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (myTodo.size() == 0){
            Map map = new HashMap();
            map.put("task", "debug program!");
            map.put("check", false);
            map.put("stick", false);
            myTodo.add(map);
            Map map1 = new HashMap();
            map1.put("task", "sleep!");
            map1.put("check", false);
            map1.put("stick", false);
            myTodo.add(map1);
        }
        adapter = new MyAdapter(getActivity(),myTodo);
        todoList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(myTodo.size());
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        public class ListItemView{   //自定义控件集合
            RadioButton check;
            TextView todo;
            Button stick;
        }
        private List<Map> list;
        private Context context;
        private LayoutInflater listContainer;

        public MyAdapter(Context context, List<Map> list){
            this.context = context;
            listContainer = LayoutInflater.from(context);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int selectID = position;
            //自定义视图
            ListItemView listItemView = null;
            if (convertView == null){
                listItemView = new ListItemView();
                convertView = listContainer.inflate(R.layout.todo_list_item,null);

                listItemView.stick = (Button) convertView.findViewById(R.id.stick);
                listItemView.todo = (TextView) convertView.findViewById(R.id.todo_info);
                listItemView.check = (RadioButton) convertView.findViewById(R.id.todo_check);

                convertView.setTag(listItemView);
            }else {
                listItemView = (ListItemView) convertView.getTag();

            }
            listItemView.check.setChecked((boolean)list.get(selectID).get("check"));
            listItemView.todo.setText((String)list.get(selectID).get("task"));
            listItemView.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //list.remove(selectID);
                        myTodo.remove(selectID);
                        Log.i("todoSize", myTodo.size() + " ");
                        notifyDataSetChanged();
                    }
                }
            });
            listItemView.stick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map temp = list.get(selectID);
                    for (int i = selectID; i > 0; i--) {
                        list.set(i, list.get(i - 1));
                    }
                    list.set(0, temp);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}
