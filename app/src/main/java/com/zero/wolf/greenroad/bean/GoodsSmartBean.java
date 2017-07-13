package com.zero.wolf.greenroad.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/5.
 */

public class GoodsSmartBean extends DataSupport{

    private String scientificname;
    private String alias;
    private String imgurl;


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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
