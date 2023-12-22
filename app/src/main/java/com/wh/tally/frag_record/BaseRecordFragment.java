package com.wh.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wh.tally.R;
import com.wh.tally.adapter.TypeBaseAdapter;
import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.AccountBean;
import com.wh.tally.db.entity.TypeBean;
import com.wh.tally.utils.BeiZhuDialog;
import com.wh.tally.utils.KeyboardUtils;
import com.wh.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    private KeyboardView keyboardView;
    private EditText moneyEt;
    public ImageView typeIv;
    private GridView typeGv;
    public TextView typeTv;
    private TextView beizhuTv;
    private TextView timeTv;
    public ArrayList<TypeBean> typeList;
    public TypeBaseAdapter adapter;
    public AccountBean accountBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();   //创建对象
        accountBean.setTypename("其他");
        accountBean.setSImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);

        // 初始化 view
        initView(view);
        setInitTime();
        // 给GridView填充数据
        loadDataToGV();
        setGVListener();
        return view;
    }

    /* 获取当前时间，显示在timeTv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }


    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        typeGv = view.findViewById(R.id.frag_record_gv);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        // 让自定义软键盘显示出来
        KeyboardUtils keyboardUtils = new KeyboardUtils(keyboardView, moneyEt);
        keyboardUtils.showKeyboard();
        // 设置接口，监听确定按钮被点击
        keyboardUtils.setOnEnsureListener(new KeyboardUtils.OnEnsureListener(){

            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保存在数据库当中
                saveAccountToDB();
                // 返回上一级页面
                getActivity().finish();
            }
        });
    }

    public abstract void saveAccountToDB();

    public void loadDataToGV() {
        typeList = new ArrayList<TypeBean>();
        adapter = new TypeBaseAdapter(getContext(),typeList);
        typeGv.setAdapter(adapter);
    }

    // 设置 GridView 里每一项item点击事件
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated(); // 提示绘制发生变化了
                TypeBean typeBean = typeList.get(position);
                String typeName = typeBean.getTypeName();
                typeTv.setText(typeName);
                accountBean.setTypename(typeName);
                int sImageId = typeBean.getSImageId();
                typeIv.setImageResource(sImageId);
                accountBean.setSImageId(sImageId);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }
    /* 弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    /* 弹出备注对话框*/
    public  void showBZDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }

}