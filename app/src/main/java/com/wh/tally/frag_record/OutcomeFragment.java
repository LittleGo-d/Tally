package com.wh.tally.frag_record;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wh.tally.MainApplication;
import com.wh.tally.R;
import com.wh.tally.adapter.TypeBaseAdapter;
import com.wh.tally.db.dao.AccountDao;
import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.AccountBean;
import com.wh.tally.db.entity.TypeBean;
import com.wh.tally.utils.KeyboardUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录中的支出模块
 */
public class OutcomeFragment extends BaseRecordFragment {

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        AccountDao accountDao = TallyDatabase.getInstance(getContext()).getAccountDao();
        accountDao.insert(accountBean);
    }

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        TallyDatabase db = TallyDatabase.getInstance(getContext());
        TypeDao typeDao = db.getTypeDao();
        List<TypeBean> list = typeDao.getTypeList(0);
        typeList.addAll(list);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }
}