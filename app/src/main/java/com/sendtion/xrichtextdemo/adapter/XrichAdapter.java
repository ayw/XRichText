package com.sendtion.xrichtextdemo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sendtion.xrichtextdemo.R;
import com.sendtion.xrichtextdemo.bean.XrichBean;

import java.util.List;

public class XrichAdapter extends BaseQuickAdapter<XrichBean, BaseViewHolder> {

    public XrichAdapter(int layoutResId, @Nullable List<XrichBean> data) {
        super(layoutResId, data);
    }

    public XrichAdapter(@Nullable List<XrichBean> data) {
        super(data);
    }

    public XrichAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final XrichBean item) {
        if (TextUtils.isEmpty(item.getText())) {
            helper.getView(R.id.text_tv).setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(item.getImgUrl())
                    .into((ImageView) helper.getView(R.id.img_item_iv));
            helper.getView(R.id.img_item_iv).setBackgroundColor(item.isCheck() ? ContextCompat.getColor(mContext, R.color.color_ff9999) : ContextCompat.getColor(mContext, R.color.white));

        } else {
            helper.getView(R.id.img_item_iv).setVisibility(View.GONE);
            helper.setText(R.id.text_tv, item.getText());
            helper.getView(R.id.text_tv).setBackgroundColor(item.isCheck() ? ContextCompat.getColor(mContext, R.color.color_ff9999) : ContextCompat.getColor(mContext, R.color.white));
        }
        helper.getView(R.id.item_opt_ll).setVisibility(item.isCheck() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.item_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isCheck()) {
                    item.setCheck(false);
                    helper.getView(R.id.item_opt_ll).setVisibility(View.GONE);
                } else {
                    item.setCheck(true);
                    helper.getView(R.id.item_opt_ll).setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        });

        helper.getView(R.id.up_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.getLayoutPosition() == 0) {
                    return;
                }
                //上移
                int pos = helper.getLayoutPosition();
                XrichBean temp = mData.get(pos - 1);
                mData.add(pos - 1, item);
                mData.add(pos, temp);
                mData.remove(pos);
                mData.remove(pos + 1);
                notifyDataSetChanged();
            }
        });

        helper.getView(R.id.down_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.getLayoutPosition() == mData.size() - 1) {
                    return;
                }
                //下移动
                int pos = helper.getLayoutPosition();
                XrichBean temp = mData.get(pos + 1);
                mData.add(pos + 1, item);
                mData.add(pos, temp);
                mData.remove(pos + 1);
                mData.remove(pos + 2);
                notifyDataSetChanged();
            }
        });
        helper.getView(R.id.del_opt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(item);
                notifyDataSetChanged();
            }
        });

//        helper.getView(R.id.edite_btn).setOnClickListener();
    }
}
