package com.android.htc.greenroad.httpresultbean;

import java.util.List;

/**
 * Created by zerowolf on 2018/2/28.
 */

public class HttpResultGoods {

    /**
     * code : 200
     * data : {"count":8,"goodsTypeList":["蔬菜","水果","水产品","畜禽","杂粮","肉蛋奶","小吃","甜品","奶酪"],"subjects":[{"name":"大白菜","pinyin":"dabaicai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":1},{"name":"菜薹","pinyin":"caitai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":4},{"name":"菜薹","pinyin":"caitai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"奶酪","sortId":4},{"name":"菜薹","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"caitai","type":"肉蛋奶","sortId":4},{"name":"菜薹","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"caitai","type":"甜品","sortId":4},{"name":"油菜","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"youcai","type":"小吃","sortId":2},{"name":"菜花","pinyin":"caihua","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"小吃","sortId":5},{"name":"芥蓝","pinyin":"jielan","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":6},{"name":"萝卜","pinyin":"luobo","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"甜品","sortId":7},{"name":"西蓝花","pinyin":"xilanhua","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":10},{"name":"小青菜","pinyin":"小青菜","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":3},{"name":"香蕉","pinyin":"xiangjiao","imageUrl":"www.baidu.com","type":"水果","sortId":8},{"name":"苹果","pinyin":"pingguo","imageUrl":"www.baidu.com","type":"水果","sortId":5},{"name":"哈密瓜","pinyin":"hamigua","imageUrl":"www.baidu.com","type":"水果","sortId":1},{"name":"黑鱼","pinyin":"heiyu","imageUrl":"www.baidu.com","type":"水产品","sortId":3},{"name":"鲶鱼","pinyin":"nianyu","imageUrl":"www.baidu.com","type":"水产品","sortId":2},{"name":"虾","pinyin":"xia","imageUrl":"www.baidu.com","type":"水产品","sortId":1},{"name":"猪","pinyin":"zhu","imageUrl":"www.baidu.com","type":"畜禽","sortId":1},{"name":"大豆","pinyin":"dadou","imageUrl":"www.baidu.com","type":"杂粮","sortId":1}]}
     */

    private int code;
    private DataBean data;

    @Override
    public String toString() {
        return "GoodsBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 8
         * goodsTypeList : ["蔬菜","水果","水产品","畜禽","杂粮","肉蛋奶","小吃","甜品","奶酪"]
         * subjects : [{"name":"大白菜","pinyin":"dabaicai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":1},{"name":"菜薹","pinyin":"caitai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":4},{"name":"菜薹","pinyin":"caitai","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"奶酪","sortId":4},{"name":"菜薹","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"caitai","type":"肉蛋奶","sortId":4},{"name":"菜薹","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"caitai","type":"甜品","sortId":4},{"name":"油菜","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","pinyin":"youcai","type":"小吃","sortId":2},{"name":"菜花","pinyin":"caihua","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"小吃","sortId":5},{"name":"芥蓝","pinyin":"jielan","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":6},{"name":"萝卜","pinyin":"luobo","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"甜品","sortId":7},{"name":"西蓝花","pinyin":"xilanhua","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":10},{"name":"小青菜","pinyin":"小青菜","imageUrl":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y","type":"蔬菜","sortId":3},{"name":"香蕉","pinyin":"xiangjiao","imageUrl":"www.baidu.com","type":"水果","sortId":8},{"name":"苹果","pinyin":"pingguo","imageUrl":"www.baidu.com","type":"水果","sortId":5},{"name":"哈密瓜","pinyin":"hamigua","imageUrl":"www.baidu.com","type":"水果","sortId":1},{"name":"黑鱼","pinyin":"heiyu","imageUrl":"www.baidu.com","type":"水产品","sortId":3},{"name":"鲶鱼","pinyin":"nianyu","imageUrl":"www.baidu.com","type":"水产品","sortId":2},{"name":"虾","pinyin":"xia","imageUrl":"www.baidu.com","type":"水产品","sortId":1},{"name":"猪","pinyin":"zhu","imageUrl":"www.baidu.com","type":"畜禽","sortId":1},{"name":"大豆","pinyin":"dadou","imageUrl":"www.baidu.com","type":"杂粮","sortId":1}]
         */

        private String markTime;
        private List<String> goodsTypeList;
        private List<String> carTypeList;
        private List<SubjectsBean> subjects;


        @Override
        public String toString() {
            return "DataBean{" +
                    "markTime=" + markTime +
                    ", goodsTypeList=" + goodsTypeList +
                    ", carTypeList=" + carTypeList +
                    ", subjects=" + subjects +
                    '}';
        }

        public String getMarkTime() {
            return markTime;
        }

        public void setMarkTime(String markTime) {
            this.markTime = markTime;
        }

        public List<String> getCarTypeList() {
            return carTypeList;
        }

        public void setCarTypeList(List<String> carTypeList) {
            this.carTypeList = carTypeList;
        }

        public List<String> getGoodsTypeList() {
            return goodsTypeList;
        }

        public void setGoodsTypeList(List<String> goodsTypeList) {
            this.goodsTypeList = goodsTypeList;
        }

        public List<SubjectsBean> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<SubjectsBean> subjects) {
            this.subjects = subjects;
        }

        public static class SubjectsBean {
            /**
             * name : 大白菜
             * pinyin : dabaicai
             * imageUrl : https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQr5C9kBRsAkKKzJOyqh8nHgZfCj8JJSWYrGzJpkcq6brmmGM4Y
             * type : 蔬菜
             * sortId : 1
             */

            private String name;
            private String pinyin;
            private String imageUrl;
            private String type;
            private int sortId;

            @Override
            public String toString() {
                return "SubjectsBean{" +
                        "name='" + name + '\'' +
                        ", pinyin='" + pinyin + '\'' +
                        ", imageUrl='" + imageUrl + '\'' +
                        ", type='" + type + '\'' +
                        ", sortId=" + sortId +
                        '}';
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPinyin() {
                return pinyin;
            }

            public void setPinyin(String pinyin) {
                this.pinyin = pinyin;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getSortId() {
                return sortId;
            }

            public void setSortId(int sortId) {
                this.sortId = sortId;
            }
        }
    }
}

