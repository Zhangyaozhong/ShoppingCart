package com.bwie.android.shoppingcart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.android.shoppingcart.R;
import com.bwie.android.shoppingcart.callback.FirstCallback;
import com.bwie.android.shoppingcart.entity.CartEntity;
import com.bwie.android.shoppingcart.witget.AddMinusView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<CartEntity.DataBean.Product> list;
    private List<CartEntity.DataBean> flist;
    private FirstCallback firstCallback;

    public void setFirstCallback(FirstCallback firstCallback) {
        this.firstCallback = firstCallback;
    }

    public ProductAdapter(Context context, List<CartEntity.DataBean.Product> list, List<CartEntity.DataBean> flist) {
        this.context = context;
        this.list = list;
        this.flist = flist;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, viewGroup, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int i) {

        final CartEntity.DataBean.Product product = list.get(i);
        holder.cb_second.setChecked(product.isSecondChecked);
        Log.i("TAG", "onBindViewHolder: " + product.isSecondChecked);

        holder.tv_name.setText(list.get(i).title);
        holder.tv_price.setText("￥" + list.get(i).price + "");
        String[] imgs = list.get(i).images.split("\\|");
        if (imgs != null && imgs.length > 0) {
            Glide.with(context).load(imgs[0]).into(holder.iv_icon);
        }
        holder.mView.setEtText(product.num);
        holder.mView.setMyViewCallback(new AddMinusView.MyViewCallback() {
            @Override
            public void addClick(int num) {
                product.num=num;
            if (firstCallback!=null){
                firstCallback.notifyRefresh();
            }
            }

            @Override
            public void minusClick(int num) {
                product.num=num;
               if (firstCallback!=null){
                   firstCallback.notifyRefresh();
               }
            }
        });
        holder.cb_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("holder.cb_second.onClick:" + holder.cb_second.isChecked());
                //判断如果二级列表有一个未选中则一级列表就不选中
                if (!holder.cb_second.isChecked()) {
                    product.isSecondChecked = false;
//                    通知一级的回调
                    if (firstCallback != null) {
                        firstCallback.notifyCartItem(false, product.pos);
                    }
                } else {
                    product.isSecondChecked = true;
                    boolean temp = true;
//                    判断二级选中状态 如果有一个未选中 退出循环
                    for (CartEntity.DataBean.Product product1 : list) {
                        if (!product1.isSecondChecked) {
                            if (firstCallback != null) {
                                firstCallback.notifyCartItem(false, product1.pos);
                            }
                            temp = false;
                            return;
                        }
                    }
                    if (temp) {
                        if (firstCallback != null) {
                            firstCallback.notifyCartItem(true, list.get(i).pos);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private AddMinusView mView;
        private CheckBox cb_second;
        private ImageView iv_icon;
        private TextView tv_name, tv_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.mView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            cb_second = itemView.findViewById(R.id.cb_second);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
