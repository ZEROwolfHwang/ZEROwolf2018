package com.zero.wolf.greenroad.bean;

import java.io.Serializable;

/**
 * Created by shadow on 2016/3/4.
 */
public class SerializableMain2Sure implements Serializable {




    private String carNumber_I;

    private String goods_I;

    private String conclusion_I;

    private String description_I;

    public String getCarNumber_I() {
        return carNumber_I;
    }

    public void setCarNumber_I(String carNumber_I) {
        this.carNumber_I = carNumber_I;
    }

    public String getGoods_I() {
        return goods_I;
    }

    public void setGoods_I(String goods_I) {
        this.goods_I = goods_I;
    }

    public String getConclusion_I() {
        return conclusion_I;
    }

    public void setConclusion_I(String conclusion_I) {
        this.conclusion_I = conclusion_I;
    }

    public String getDescription_I() {
        return description_I;
    }

    public void setDescription_I(String description_I) {
        this.description_I = description_I;
    }

    @Override
    public String toString() {
        return "SerializableMain2Sure{" +
                "carNumber_I='" + carNumber_I + '\'' +
                ", goods_I='" + goods_I + '\'' +
                ", conclusion_I='" + conclusion_I + '\'' +
                ", description_I='" + description_I + '\'' +
                '}';
    }
}
