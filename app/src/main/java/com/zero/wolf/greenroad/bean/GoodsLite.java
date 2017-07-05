package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/7/5.
 */

public class GoodsLite<T>{

    /**
     * code : 400
     * msg : 返回成功
     * data : [{"cargoid":"1","cargoimg":"","scientificname":"番茄","alias":"西红柿","kind":"蔬菜瓜果"},{"cargoid":"4","cargoimg":"Thegoodspicture/2017-07-05/149924515729333.jpg","scientificname":"西瓜","alias":"青门绿玉房","kind":"蔬菜瓜果"},{"cargoid":"5","cargoimg":"Thegoodspicture/2017-07-05/149924566233513.jpg","scientificname":"甜瓜","alias":"甜瓜皮","kind":"蔬菜瓜果"}]
     */

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
         * cargoid : 1
         * cargoimg :
         * scientificname : 番茄
         * alias : 西红柿
         * kind : 蔬菜瓜果
         */

        private String cargoid;
        private String scientificname;
        private String alias;
        private String kind;

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

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }
    }
}
