package com.wh.tally.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.wh.tally.AboutActivity;
import com.wh.tally.HistoryActivity;
import com.wh.tally.MonthChartActivity;
import com.wh.tally.R;
import com.wh.tally.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {

    private Button aboutBtn,settingBtn,recordBtn,descBtn;
    private ImageView moreCancelIV;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

        aboutBtn = findViewById(R.id.dialog_more_btn_guanyu);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        recordBtn = findViewById(R.id.dialog_more_btn_record);
        descBtn = findViewById(R.id.dialog_more_btn_info);
        moreCancelIV = findViewById(R.id.dialog_more_btn_cancel);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        descBtn.setOnClickListener(this);
        moreCancelIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.dialog_more_btn_guanyu:
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_cancel:
                break;
        }
        cancel();
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    public void setDialogSize(){
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
