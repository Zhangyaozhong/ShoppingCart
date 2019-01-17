package com.bwie.android.shoppingcart.presenter;

import com.bwie.android.shoppingcart.contract.CartContract;
import com.bwie.android.shoppingcart.entity.CartEntity;
import com.bwie.android.shoppingcart.model.CartModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class CartPresenter extends CartContract.CartPresenter {
    private CartModel cartModel;
    private CartContract.ICartView iCartView;

    public CartPresenter(CartContract.ICartView iCartView) {
        this.iCartView = iCartView;
        cartModel = new CartModel();
    }

    @Override
    public void cartData(HashMap<String, String> params) {
        if (cartModel != null) {
            cartModel.cartData(params, new CartModel.CartModelCallback() {
                @Override
                public void success(String result) {
                    if (iCartView != null) {
                        Gson gson = new Gson();
                        CartEntity cartEntity = gson.fromJson(result, CartEntity.class);
                        List<CartEntity.DataBean> data = cartEntity.data;
                        iCartView.success(data);
                    }
                }

                @Override
                public void failure(String msg) {
                    if (iCartView != null) {
                        iCartView.failure(msg);
                    }
                }
            });
        }
    }
}
