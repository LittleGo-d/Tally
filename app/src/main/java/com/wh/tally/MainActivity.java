package com.wh.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wh.tally.adapter.AccountAdapter;
import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.AccountBean;
import com.wh.tally.db.entity.TypeBean;
import com.wh.tally.utils.BudgetDialog;
import com.wh.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //主页展示收支情况
    private List<AccountBean> mDatas;
    private AccountAdapter accountAdapter;
    private int year,month,day;

    // 头布局相关控件
    private TextView topOutTV,topInTV,topBudgetTV,topConTv;
    private View headerView;
    private ImageView topShowIV;

    private ImageView searchIV;
    private Button editBtn;
    private ImageButton moreBtn;

    // 存储预算的共享参数
    private SharedPreferences preferences;

    // 今日收支情况
    private ListView todayLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget",MODE_PRIVATE);
        mDatas = new ArrayList<>();
        // 加载主界面头布局
        loadHeadView();
        // 设置适配器
        accountAdapter = new AccountAdapter(this,mDatas);
        todayLV.setAdapter(accountAdapter);
    }

    private void initView() {
        todayLV = findViewById(R.id.main_lv);
        searchIV = findViewById(R.id.main_iv_search);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIV.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);

        setLVLongClickListener();
    }

    private void setLVLongClickListener() {
        todayLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) { // 点击了头布局
                    return false;
                }
                int pos = position - 1;
                AccountBean item = mDatas.get(pos);
                showItemDeleteDialog(item);
                return false;
            }
        });
    }

    private void showItemDeleteDialog(AccountBean item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountDao accountDao = TallyDatabase.getInstance(getApplicationContext()).getAccountDao();
                        accountDao.removeById(item);
                        mDatas.remove(item);
                        accountAdapter.notifyDataSetChanged();
                        setTopTVData();
                    }
                });
        builder.create().show();
    }

    private void loadHeadView() {
        // 将头布局转换成 View 对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top,null);
        todayLV.addHeaderView(headerView);

        topOutTV = findViewById(R.id.item_mainlv_top_tv_out);
        topInTV = findViewById(R.id.item_mainlv_top_tv_in);
        topBudgetTV = findViewById(R.id.item_mainlv_top_tv_budget);
        topShowIV = findViewById(R.id.item_mainlv_top_iv_hide);
        topConTv = findViewById(R.id.item_mainlv_top_tv_day);

        topBudgetTV.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIV.setOnClickListener(this);
    }

    // 当 activity 获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        setTopTVData();
    }

    private void setTopTVData() {
        AccountDao accountDao = TallyDatabase.getInstance(this).getAccountDao();
        Long outcomeOfDay = accountDao.getInOrOutOneDay(year, month, day, 0);
        Long incomeOfDay = accountDao.getInOrOutOneDay(year, month, day, 1);
        topConTv.setText("今日收入 ￥ " + (incomeOfDay == null ? 0 : incomeOfDay) + " 支出 ￥ " + (outcomeOfDay == null ? 0 : outcomeOfDay));
        Long outcomeOfMonth = accountDao.getInOrOutOneMonth(year, month, 0);
        Long incomeOfMonth = accountDao.getInOrOutOneMonth(year, month, 1);
        topInTV.setText("￥ " + String.valueOf(incomeOfMonth == null ? 0 : incomeOfMonth));
        topOutTV.setText("￥ " + String.valueOf(outcomeOfMonth == null ? 0 : outcomeOfMonth                 ));

        //    设置显示运算剩余
        float budget = preferences.getFloat("budget", 0);//预算
        if (budget - outcomeOfMonth <= 0) {
            topBudgetTV.setText("￥ 0");
        }else{
            float remain = budget - outcomeOfMonth;
            topBudgetTV.setText("￥"+ remain);
        }
    }

    private void loadData() {
        AccountDao accountDao = TallyDatabase.getInstance(this).getAccountDao();
        List<AccountBean> list = accountDao.getAccountListOfDay(year, month, day);
        mDatas.clear(); // 防止数据重复
        mDatas.addAll(list);
        accountAdapter.notifyDataSetChanged(); // 提醒数据发生改变
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_iv_search:
                Intent it = new Intent(this,SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent intent = new Intent(this,RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                toggleShow();
                break;
        }
        if (view == headerView) {// 头布局被点击了

        }
    }

    private void showBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setDialogSize();
        budgetDialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("budget",money);
                editor.commit();
                AccountDao accountDao = TallyDatabase.getInstance(getApplicationContext()).getAccountDao();
                float outcome = accountDao.getInOrOutOneMonth(year, month, 0);
                float remain = money - outcome;
                topBudgetTV.setText("￥ " + remain);
            }
        });
    }

    private boolean isShow = true;

    // 明文密文切换
    private void toggleShow() {
        if (isShow){
            PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();
            topInTV.setTransformationMethod(passwordTransformationMethod);
            topOutTV.setTransformationMethod(passwordTransformationMethod);
            topBudgetTV.setTransformationMethod(passwordTransformationMethod);
            topShowIV.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }else{
            HideReturnsTransformationMethod hideReturnsTransformationMethod = HideReturnsTransformationMethod.getInstance();
            topInTV.setTransformationMethod(hideReturnsTransformationMethod);
            topOutTV.setTransformationMethod(hideReturnsTransformationMethod);
            topBudgetTV.setTransformationMethod(hideReturnsTransformationMethod);
            topShowIV.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }

    }
}