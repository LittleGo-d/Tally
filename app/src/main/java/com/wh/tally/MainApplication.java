package com.wh.tally;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.wh.tally.db.dao.TypeDao;
import com.wh.tally.db.database.TallyDatabase;
import com.wh.tally.db.entity.TypeBean;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TallyDatabase db = TallyDatabase.getInstance(this);
        TypeDao typeDao = db.getTypeDao();

        int typeCount = typeDao.getTypeCount();

            if (typeCount == 0) {
                typeDao.insert(new TypeBean(null,"其他", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0));
                typeDao.insert(new TypeBean(null,"餐饮", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0));
                typeDao.insert(new TypeBean(null,"交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0));
                typeDao.insert(new TypeBean(null,"购物", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,0));
                typeDao.insert(new TypeBean(null,"服饰", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,0));
                typeDao.insert(new TypeBean(null,"日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,0));
                typeDao.insert(new TypeBean(null,"娱乐", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,0));
                typeDao.insert(new TypeBean(null,"零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,0));
                typeDao.insert(new TypeBean(null,"烟酒茶", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0));
                typeDao.insert(new TypeBean(null,"学习", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,0));
                typeDao.insert(new TypeBean(null,"医疗", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,0));
                typeDao.insert(new TypeBean(null,"住宅", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,0));
                typeDao.insert(new TypeBean(null,"水电煤", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,0));
                typeDao.insert(new TypeBean(null,"通讯", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,0));
                typeDao.insert(new TypeBean(null,"人情往来", R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,0));

                typeDao.insert(new TypeBean(null,"其他", R.mipmap.in_qt,R.mipmap.in_qt_fs,1));
                typeDao.insert(new TypeBean(null,"薪资", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,1));
                typeDao.insert(new TypeBean(null,"奖金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1));
                typeDao.insert(new TypeBean(null,"借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1));
                typeDao.insert(new TypeBean(null,"收债", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1));
                typeDao.insert(new TypeBean(null,"利息收入", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,1));
                typeDao.insert(new TypeBean(null,"投资回报", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1));
                typeDao.insert(new TypeBean(null,"二手交易", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1));
                typeDao.insert(new TypeBean(null,"意外所得", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1));
            }
            db.close();
        }

}
