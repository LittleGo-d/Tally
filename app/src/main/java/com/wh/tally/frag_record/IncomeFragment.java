package com.wh.tally.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wh.tally.R;
import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.TypeBean;

import java.util.List;

/**
 * 记录中的收入模块
 */
public class IncomeFragment extends BaseRecordFragment{

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
         TallyDatabase db = TallyDatabase.getInstance(getContext());
        TypeDao typeDao = db.getTypeDao();
        List<TypeBean>  list = typeDao.getTypeList(1);
        typeList.addAll(list);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        AccountDao accountDao = TallyDatabase.getInstance(getContext()).getAccountDao();
        accountDao.insert(accountBean);
    }
}