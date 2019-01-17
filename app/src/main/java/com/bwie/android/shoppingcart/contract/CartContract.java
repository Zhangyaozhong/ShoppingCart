package com.bwie.android.shoppingcart.contract;

import com.bwie.android.shoppingcart.entity.CartEntity;
import com.bwie.android.shoppingcart.model.CartModel;

import java.util.HashMap;
import java.util.List;

public interface CartContract {
    abstract class CartPresenter{
        public abstract void cartData(HashMap<String,String> params);
    }

    interface ICartModel{
        void cartData(HashMap<String,String> params, CartModel.CartModelCallback cartModelCallback);
    }
    interface ICartView{
        void success(List<CartEntity.DataBean> dataBeans);
        void failure(String msg);
    }
}
