package com.bwie.android.shoppingcart.entity;

import java.util.List;

public class CartEntity {
    public String code;
    public String msg;
    public List<DataBean> data;

    public class DataBean {
        public String sellerName;
        public String sellerid;
        public boolean isFirstChecked;
        public List<Product> list;

        public class Product {
            public boolean isSecondChecked;
            public String title;
            public String images;
            public int pos;
            public double price;
            public String pid;
            public int num ;
        }
    }
}
