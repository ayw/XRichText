package com.sendtion.xrichtextdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sendtion.xrichtextdemo.R;

/**
 * 自定义文本限制  EditTextView
 */

public class LimitEditTextView extends RelativeLayout implements InputFilter {

    private View rootView;
    private EditText limitEt;
    private TextView limitInfo;
    private int limitNum = 60;

    public LimitEditTextView(Context context) {
        super(context);
    }

    public LimitEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EditText getLimitEt() {
        return limitEt;
    }

    public void setLimitEt(EditText limitEt) {
        this.limitEt = limitEt;
    }

    private void initView(final Context context, AttributeSet attrs) {
        rootView = LayoutInflater.from(context).inflate(R.layout.limit_edite_text, this, true);
        limitEt = rootView.findViewById(R.id.limit_et);
        limitInfo = rootView.findViewById(R.id.limit_info);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LimitEditTextView);
        if (attributes != null) {
            String hint = attributes.getString(R.styleable.LimitEditTextView_hint_text);
            if (hint != null) {
                limitEt.setHint(hint);
            }
            limitNum = attributes.getInt(R.styleable.LimitEditTextView_limit_num, limitNum);
            limitInfo.setText("0/" + limitNum);
            attributes.recycle();
        }

        limitEt.setFilters(new InputFilter[]{this});
        limitEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limitInfo.setText(s.toString().length() + "/" + limitNum);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getInputText() {
        return limitEt.getText().toString().trim();
    }

    public void setLimitText(CharSequence text) {
        if (TextUtils.isEmpty(text)
                || text.length() > limitNum) {
            return;
        }
        limitEt.setText(text);
        limitEt.setSelection(text.length());
    }

    public void clearText() {
        limitEt.setText("");
    }

    public void setHintText(CharSequence text) {
        if (TextUtils.isEmpty(text)
                || text.length() > limitNum) {
            return;
        }
        limitEt.setHint(text);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = limitNum - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            Toast.makeText(getContext(), "最多只能输入" + limitNum + "个字", Toast.LENGTH_SHORT).show();
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}
