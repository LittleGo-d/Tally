package com.wh.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wh.tally.adapter.CalendarAdapter;
import com.wh.tally.R;
import com.wh.tally.db.database.TallyDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {

    private ImageView errorIV;
    private GridView calendarGV;
    private LinearLayout hsvLayout;
    private List<TextView> hsvViewList;
    private List<Integer> yearList;
    private int selectPos =  -1; // 正在被点击的年份的位置
    private int selectMonth  = -1;
    private CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        errorIV = findViewById(R.id.dialog_calendar_iv);
        calendarGV = findViewById(R.id.dialog_calendar_gv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        errorIV.setOnClickListener(this);
        // 向横向的 ScrollView 中添加 view
        addViewLayout();
        initGridView();
        // 给 gv 中每一个 item 设置点击事件
        setGVItemListener();
    }

    private void setGVItemListener() {
        calendarGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();
                int month = position + 1;
                int year = adapter.year;
                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else {
            adapter.selPos = selectMonth-1;
        }
        calendarGV.setAdapter(adapter);
    }

    private void addViewLayout() {
        hsvViewList = new ArrayList<>();
        yearList = TallyDatabase.getInstance(getContext()).getAccountDao().getYearList();
        // 数据库中如果没有年份记录，就添加今年的年份
        if (yearList.size() == 0){
            yearList.add(Calendar.getInstance().get(Calendar.YEAR));
        }
        // 遍历年份并添加到 ScrollView 中
        for (int i = 0; i < yearList.size(); i++) {
            Integer year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            hsvLayout.addView(view);
            TextView hsvTV = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTV.setText(year + "");
            hsvViewList.add(hsvTV);
        }
        if (selectPos == -1){
            // 如果没有年份被选中，设置被选中的是最近的年份
            selectPos = hsvViewList.size() - 1;
        }
        changeTVbg(selectPos);
        setHSVListener(); // 设置每一个 view 的监听事件
    }

    private void setHSVListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView textView = hsvViewList.get(i);
            final int pos = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTVbg(pos);
                    selectPos = pos;
                    Integer year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    private void changeTVbg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }
        TextView selectTV = hsvViewList.get(selectPos);
        selectTV.setBackgroundResource(R.drawable.main_recordbtn_bg);
        selectTV.setTextColor(Color.WHITE);
    }

    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    public void setDialogSize(){
//        获取当前窗口对象
        Window window = getWindow();
//        获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
//        获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
