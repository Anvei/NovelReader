package org.anvei.novelreader.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.anvei.novelreader.R;

public class SearchBar extends RelativeLayout {

    private boolean editable;                   // EditText是否可以编辑文本

    private View root;
    private EditText editText;
    private TextView searchBtn;

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchBar);
        editable = typedArray.getBoolean(R.styleable.SearchBar_editable, false);
        typedArray.recycle();
        root = LayoutInflater.from(getContext()).inflate(R.layout.view_search_bar, this);
        editText = root.findViewById(R.id.vsb_edit);
        searchBtn = root.findViewById(R.id.vsb_search_btn);
        editText.setEnabled(editable);
    }

    public void setText(CharSequence text) {
        editText.setText(text);
    }

    public void setCallBack(CallBack callBack) {
        if (editable) {
            searchBtn.setOnClickListener((v) ->
                    callBack.onClickWithEditable(editText.getText().toString()));
        } else {
            root.setOnClickListener((v) ->
                    callBack.onClickWithUnEditable());
        }
    }

    public abstract static class CallBack {
        public void onClickWithEditable(String inputText){}
        public void onClickWithUnEditable(){}
    }
}
