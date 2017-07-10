package com.zero.wolf.greenroad.litepalbean;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/5.
 */

public class SupportGoods extends DataSupport{
    private String cargoid;
    private String scientificname;
    private String alias;
    private int goodsId;
    private Bitmap bitmap;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCargoid() {
        return cargoid;
    }

    public void setCargoid(String cargoid) {
        this.cargoid = cargoid;
    }


    public String getScientificname() {
        return scientificname;
    }

    public void setScientificname(String scientificname) {
        this.scientificname = scientificname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
