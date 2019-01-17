package com.bwie.android.shoppingcart.witget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bwie.android.shoppingcart.R;

public class AddMinusView extends LinearLayout implements View.OnClickListener {

    private Button add;
    private Button minus;
    private EditText ed_num;
    private int num = 1;

    public AddMinusView(Context context) {
        this(context, null);
    }

    public AddMinusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddMinusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_minus_layout, this, true);
        add = view.findViewById(R.id.add_btn);
        minus = view.findViewById(R.id.minus_btn);
        ed_num = view.findViewById(R.id.et_num);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);
        ed_num.setText(num + "");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                num = Integer.parseInt(ed_num.getText().toString());
                num++;
                ed_num.setText(num+"");
                if (myViewCallback != null) {
                    myViewCallback.addClick(num);
                }
                break;
            case R.id.minus_btn:
                num = Integer.parseInt(ed_num.getText().toString());
                num--;
                if (num <= 0) {
                    Toast.makeText(getContext(), "不能再减了", Toast.LENGTH_LONG).show();
                    num=1;
                    return;
                }
                ed_num.setText(num+"");

                if (myViewCallback != null) {
                    myViewCallback.minusClick(num);
                }
                break;
        }
    }

    /**
     * 设置et
     * @param num
     */
    public void setEtText(int num) {

        ed_num.setText(num+"");
    }

    public interface MyViewCallback {
        void addClick(int num);

        void minusClick(int num);
    }

    private MyViewCallback myViewCallback;

    public void setMyViewCallback(MyViewCallback myViewCallback) {
        this.myViewCallback = myViewCallback;
    }
}
