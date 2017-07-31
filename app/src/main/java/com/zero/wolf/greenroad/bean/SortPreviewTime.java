package com.zero.wolf.greenroad.bean;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SortPreviewTime implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {

        int flag2=0;

        SerializablePreview preview1 = (SerializablePreview) o1;
        SerializablePreview preview2 = (SerializablePreview) o2;

        int flag = preview1.getShutTime().compareTo(preview2.getShutTime());

        //倒序
        if(flag>0){
            flag2=-1;
        }else if(flag<0){
            flag2=1;
        }


        return flag2;

    }


}
