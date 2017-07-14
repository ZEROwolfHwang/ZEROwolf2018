package com.zero.wolf.greenroad.smartsearch;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SortModel  implements Serializable {

    public Bitmap mBitmap;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * 显示数据拼音的首字母
     */
    public String sortLetters;
    /**
     * 简拼
     */
    public String simpleSpell;
    /**
     * 全拼
     */
    public String wholeSpell;


    public String scientificname;
    public String alias;
  //  public String imgurl;

    public SortModel() {

    }

    public String getSimpleSpell() {
        return simpleSpell;
    }

    public void setSimpleSpell(String simpleSpell) {
        this.simpleSpell = simpleSpell;
    }

    public String getWholeSpell() {
        return wholeSpell;
    }

    public void setWholeSpell(String wholeSpell) {
        this.wholeSpell = wholeSpell;
    }


/*
    public SortModel(String scientificname, String alias, String imgurl) {
        super(scientificname, alias, imgurl);
    }

    public SortModel(String scientificname, String alias, String imgurl,
                     String sortLetters, String simpleSpell, String wholeSpell,
                     String chName, String scientificname1, String alias1, String imgurl1) {
        super(scientificname, alias, imgurl);
        this.sortLetters = sortLetters;
        this.simpleSpell = simpleSpell;
        this.wholeSpell = wholeSpell;
        this.scientificname = scientificname1;
        this.alias = alias1;
        this.imgurl = imgurl1;
    }*/

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
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

  /*  public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }*/

    @Override
    public String toString() {
        return "SortModel{" +
                "mBitmap=" + mBitmap +
                ", sortLetters='" + sortLetters + '\'' +
                ", simpleSpell='" + simpleSpell + '\'' +
                ", wholeSpell='" + wholeSpell + '\'' +
                ", scientificname='" + scientificname + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
