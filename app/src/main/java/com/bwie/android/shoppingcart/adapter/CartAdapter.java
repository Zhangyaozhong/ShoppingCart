package com.bwie.android.shoppingcart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bwie.android.shoppingcart.R;
import com.bwie.android.shoppingcart.callback.FirstCallback;
import com.bwie.android.shoppingcart.contract.CartUICallback;
import com.bwie.android.shoppingcart.entity.CartEntity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements FirstCallback {
    private Context context;
    private List<CartEntity.DataBean> list;
    private ProductAdapter productAdapter;
    //通知首页刷新价格的接口
    private CartUICallback cartUICallback;

    public void setCartUICallback(CartUICallback cartUICallback) {
        this.cartUICallback = cartUICallback;
    }

    public CartAdapter(Context context, List<CartEntity.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, viewGroup, false);
        CartViewHolder holder = new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, int i) {
        final CartEntity.DataBean dataBean = list.get(i);
        holder.cb.setChecked(dataBean.isFirstChecked);
//对每件商品的pos赋值，记录一级列表的位置pos
        for (CartEntity.DataBean.Product product : dataBean.list) {

            product.pos = i;
        }

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean.isFirstChecked = holder.cb.isChecked();
                Log.i("TAG", "onClick: " + holder.cb.isChecked());
                for (CartEntity.DataBean.Product product : dataBean.list) {
                    product.isSecondChecked = holder.cb.isChecked();
                    Log.i("TAG", "isSecondChecked: " + holder.cb.isChecked());
                }
                notifyDataSetChanged();
                //通知首页价格联动

                if (cartUICallback != null) {
                    cartUICallback.notifyCart();
                }

            }
        });

//        设置标题
        holder.titleTv.setText(list.get(i).sellerName);
//        设置商品列表的适配器
        holder.mRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        productAdapter = new ProductAdapter(context, dataBean.list, list);
        productAdapter.setFirstCallback(this);//设置二级适配器回调接口
        holder.mRV.setAdapter(productAdapter);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void notifyRefresh() {
        if (cartUICallback != null) {
            cartUICallback.notifyCart();
        }
    }

    @Override
    public void notifyCartItem(boolean flag, int postion) {
        list.get(postion).isFirstChecked = flag;
        notifyDataSetChanged();
        if (cartUICallback != null) {
            cartUICallback.notifyCart();
        }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb;
        private TextView titleTv;
        private RecyclerView mRV;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb);
            mRV = itemView.findViewById(R.id.rv);
            titleTv = itemView.findViewById(R.id.tv_title);

        }
    }
}
