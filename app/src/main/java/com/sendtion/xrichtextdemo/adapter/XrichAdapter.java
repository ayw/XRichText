package com.sendtion.xrichtextdemo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
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
    protected void convert(final BaseViewHolder helper, XrichBean item) {
        if (TextUtils.isEmpty(item.getText())) {
            helper.getView(R.id.text_tv).setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(item.getImgUrl())
                    .into((ImageView) helper.getView(R.id.img_item_iv));
        } else {
            helper.getView(R.id.img_item_iv).setVisibility(View.GONE);
            helper.setText(R.id.text_tv, item.getText());
        }

        helper.getView(R.id.item_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.getView(R.id.item_opt_ll).setVisibility(helper.getView(R.id.item_opt_ll).getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
    }
}
