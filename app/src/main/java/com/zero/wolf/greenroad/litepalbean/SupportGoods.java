package com.zero.wolf.greenroad.litepalbean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/5.
 */

public class SupportGoods extends DataSupport{
    private String cargoid;
    private String scientificname;
    private String alias;
    private String kind;
    private String imgurl;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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