package com.wh.tally.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wh.tally.R;
import com.wh.tally.db.entity.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {

    private List<AccountBean> mDatas;
    private Context context;
    private LayoutInflater inflater;
    private int year,month,day;

    public AccountAdapter(Context context,List<AccountBean> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
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
        ViewHolder holder = null;
        if (view == null){
            view = inflater.inflate(R.layout.item_mainlv,parent,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        AccountBean accountBean = mDatas.get(position);
        holder.typeIv.setImageResource(accountBean.getSImageId());
        holder.typeTv.setText(accountBean.getTypename());
        holder.beizhuTv.setText(accountBean.getBeizhu());
        holder.moneyTv.setText("￥ " + accountBean.getMoney());
        if (accountBean.getYear() == year && accountBean.getMonth() == month && accountBean.getDay() == day) {
            String time = accountBean.getTime().split(" ")[1];
            holder.timeTv.setText("今天 "+time);
        }else {
            holder.timeTv.setText(accountBean.getTime());
        }
        return view;
    }

    class ViewHolder{
        ImageView typeIv;
        TextView typeTv,beizhuTv,timeTv,moneyTv;
        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);

        }
    }
}
