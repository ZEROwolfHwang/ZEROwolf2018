package com.zero.wolf.greenroad.httpresultbean;

/**
 * Created by Administrator on 2017/7/5.
 */

public class HttpResultGoods<T>{


    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cargoid : 12
         * imgurl : http://192.168.2.122/lvsetondao/Public/Home/149968460463439.jpg
         * scientificname : 水蜜桃
         * mAlias : 水水水水
         * kind : 水果
         */

        private String cargoid;
        private String imgurl;
        private String scientificname;
        private String alias;
        private String kind;

        public String getCargoid() {
            return cargoid;
        }

        public void setCargoid(String cargoid) {
            this.cargoid = cargoid;
        }

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

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }
    }
}
