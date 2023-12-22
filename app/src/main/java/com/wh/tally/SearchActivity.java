package com.wh.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wh.tally.adapter.AccountAdapter;
import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.AccountBean;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView searchBackIV;
    private EditText searchET;
    private ImageView searchIV;
    private ListView searchLV;
    private TextView emptyTV;

    private List<AccountBean> mDatas;

    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this,mDatas);
        searchLV.setAdapter(adapter);
        searchLV.setEmptyView(emptyTV);
    }

    private void initView() {
        searchET = findViewById(R.id.search_et);
        searchLV = findViewById(R.id.search_lv);
        emptyTV = findViewById(R.id.search_tv_empty);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                String searchContent = searchET.getText().toString();
                if (TextUtils.isEmpty(searchContent)){
                    Toast.makeText(this,"请输入查询条件",Toast.LENGTH_SHORT).show();
                }
                AccountDao accountDao = TallyDatabase.getInstance(getApplicationContext()).getAccountDao();
                List<AccountBean> list = accountDao.getAccountsByBeizhuOrTypename(searchContent);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;

        }
    }

}