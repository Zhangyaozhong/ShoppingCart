package com.bwie.android.shoppingcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bwie.android.shoppingcart.adapter.CartAdapter;
import com.bwie.android.shoppingcart.contract.CartContract;
import com.bwie.android.shoppingcart.contract.CartUICallback;
import com.bwie.android.shoppingcart.entity.CartEntity;
import com.bwie.android.shoppingcart.presenter.CartPresenter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CartContract.ICartView, CartUICallback {
    private CheckBox mcb;
    private CartPresenter cartPresenter;
    private List<CartEntity.DataBean> dataList;
    private CartAdapter cartAdapter;
    private XRecyclerView rv;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        rv = findViewById(R.id.xrv);
        mcb = findViewById(R.id.total_cb);
        //设置布局管理器
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setPullRefreshEnabled(true);
        rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
                rv.refreshComplete();
            }

            @Override
            public void onLoadMore() {

            }
        });
        //全选、反选
        mcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (CartEntity.DataBean dataBean : dataList) {
                    for (CartEntity.DataBean.Product product : dataBean.list) {
                        dataBean.isFirstChecked = product.isSecondChecked = isChecked;
                    }
                }
                totalPrice();
                cartAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        dataList = new ArrayList<>();

        cartPresenter = new CartPresenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", "71");
        cartPresenter.cartData(map);
    }

    /**
     * 成功的回调
     *
     * @param dataBeans
     */
    @Override
    public void success(List<CartEntity.DataBean> dataBeans) {
        if (dataBeans != null) {
            dataList = dataBeans;
            for (CartEntity.DataBean cart : dataBeans) {
                for (CartEntity.DataBean.Product product : cart.list) {
                    product.num = 1;
                }
            }
            //设置适配器
            cartAdapter = new CartAdapter(MainActivity.this, dataList);
            cartAdapter.setCartUICallback(this);
            rv.setAdapter(cartAdapter);
        }
    }

    @Override
    public void failure(String msg) {

    }

    /**
     * 计算总价
     */
    public void totalPrice() {

        double totalPrice = 0;
        for (CartEntity.DataBean dataBean : dataList) {
            for (CartEntity.DataBean.Product product : dataBean.list) {
                if (product.isSecondChecked) {
                    totalPrice += product.num * product.price;
                }


            }
        }
        mcb.setText(totalPrice + "");

    }

    /**
     * 通知底部导航栏总价联动
     */
    @Override
    public void notifyCart() {
        totalPrice();
    }
}
