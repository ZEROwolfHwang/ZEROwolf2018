package com.zero.wolf.greenroad.helper;

import com.zero.wolf.greenroad.litepalbean.SupportSubmit;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SortSubmitTime implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {

        int flag2=0;

        SupportSubmit preview1 = (SupportSubmit) o1;
        SupportSubmit preview2 = (SupportSubmit) o2;

        int flag = preview1.getCurrent_time().compareTo(preview2.getCurrent_time());

        //倒序
        if(flag>0){
            flag2=-1;
        }else if(flag<0){
            flag2=1;
        }


        return flag2;

    }


}
