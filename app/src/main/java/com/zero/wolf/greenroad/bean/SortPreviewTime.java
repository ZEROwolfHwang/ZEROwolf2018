package com.zero.wolf.greenroad.bean;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SortPreviewTime implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        SerializablePreview preview1 = (SerializablePreview) o1;
        SerializablePreview preview2 = (SerializablePreview) o2;

        int flag = preview1.getShutTime().compareTo(preview2.getShutTime());

        return flag;

    }

}
