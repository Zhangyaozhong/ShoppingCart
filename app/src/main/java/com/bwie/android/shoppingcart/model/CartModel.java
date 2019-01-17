package com.bwie.android.shoppingcart.model;

import com.bwie.android.shoppingcart.api.CartApi;
import com.bwie.android.shoppingcart.callback.OkhttpCallback;
import com.bwie.android.shoppingcart.contract.CartContract;
import com.bwie.android.shoppingcart.utils.OkhttpUtil;

import java.util.HashMap;

public class CartModel implements CartContract.ICartModel {
    @Override
    public void cartData(HashMap<String, String> params, final CartModelCallback cartModelCallback) {
        OkhttpUtil.getInstance().doPost(CartApi.CART_URL, params, new OkhttpCallback() {
            @Override
            public void success(String result) {
                if (cartModelCallback != null) {

                    cartModelCallback.success(result);
                }
            }

            @Override
            public void failure(String msg) {
                if (cartModelCallback != null) {

                    cartModelCallback.failure(msg);
                }
            }
        });
    }

    public interface CartModelCallback {
        void success(String result);

        void failure(String msg);
    }

    private CartModelCallback cartModelCallback;

    public void setCartModelCallback(CartModelCallback cartModelCallback) {
        this.cartModelCallback = cartModelCallback;
    }
}
