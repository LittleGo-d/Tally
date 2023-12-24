package com.wh.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wh.tally.adapter.AccountAdapter;
import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.AccountBean;
import com.wh.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView historyLV;
    private TextView timeTV;
    private AccountAdapter adapter;
    private List<AccountBean> mDatas;
    private int year,month;

   private int dialogSelPos = -1;
   private int dialogSelMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLV = findViewById(R.id.history_lv);
        timeTV = findViewById(R.id.history_tv_time);
        mDatas = new ArrayList<>();
        initTime();
        adapter = new AccountAdapter(this,mDatas);
        historyLV.setAdapter(adapter);
        timeTV.setText(year + "年" + month + "月");
        loadData(year,month);
        setLVClickListener();
    }

    // 给每个 item 设置长按事件
    private void setLVClickListener() {
        historyLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean accountBean = mDatas.get(position);
                deleteItem(accountBean);
                return false;
            }
        });
    }

    private void deleteItem(AccountBean accountBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除和条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountDao accountDao = TallyDatabase.getInstance(getApplicationContext()).getAccountDao();
                        accountDao.removeById(accountBean);
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();
    }

    private void loadData(int year,int month){
        AccountDao accountDao = TallyDatabase.getInstance(this).getAccountDao();
        List<AccountBean> accounts = accountDao.getInAndOutOneMonth(year, month);
        mDatas.clear();
        mDatas.addAll(accounts);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) - 1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                CalendarDialog dialog = new CalendarDialog(this,dialogSelPos,dialogSelMonth);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        timeTV.setText(year+"年"+month+"月");
                        loadData(year,month);
                        dialogSelPos = selPos;
                        dialogSelMonth = month;
                    }
                });
                break;
        }
    }
}