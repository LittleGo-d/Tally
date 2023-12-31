package com.wh.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.wh.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    private ImageView cancelIV;
    private Button ensureBtn;
    private EditText moneyET;

    public interface OnEnsureListener{
        public void onEnsure(float money);
    }

    private OnEnsureListener onEnsureListener;

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIV = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyET = findViewById(R.id.dialog_budget_et);
        cancelIV.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_budget_iv_error:
                cancel();
                break;
            case R.id.dialog_budget_btn_ensure:
                String data = moneyET.getText().toString();
                if (TextUtils.isEmpty(data)){
                    Toast.makeText(getContext(), "输入预算不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if (money <= 0){
                    Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener != null){
                    onEnsureListener.onEnsure(money);
                }
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
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软键盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
