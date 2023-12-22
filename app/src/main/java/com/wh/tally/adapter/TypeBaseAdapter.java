package com.wh.tally.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wh.tally.R;
import com.wh.tally.db.entity.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {

    private Context context;
    private List<TypeBean> mDatas;
    public int selectPos = 0; // 选中位置

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv,parent,false);
        // 查找布局中的控件
        ImageView iv = view.findViewById(R.id.item_recordfrag_iv);
        TextView tv = view.findViewById(R.id.item_recordfrag_tv);
        // 获取指定位置数据源
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypeName());
        // 判断当前是否为选中位置
        if (selectPos == position){
            iv.setImageResource(typeBean.getSImageId());
        }else{
            iv.setImageResource(typeBean.getImageId());
        }
        return view;
    }
}
