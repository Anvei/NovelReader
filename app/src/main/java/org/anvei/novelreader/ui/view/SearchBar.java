package org.anvei.novelreader.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.anvei.novelreader.R;

public class SearchBar extends RelativeLayout {

    private boolean editable;                   // 是否可以输入文本，默认为false

    private View root;
    private TextView inputText;
    private TextView searchBtn;

    public SearchBar(Context context) {
        super(context, null);
    }

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
        // 根据editable选择相应的视图
        if (editable) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.view_search_bar_edit_text, this);
            inputText = root.findViewById(R.id.vsb_edit);
            searchBtn = root.findViewById(R.id.vsb_search_btn);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.view_search_bar_text_view, this);
            inputText = root.findViewById(R.id.vsb_edit_2);
            searchBtn = root.findViewById(R.id.vsb_search_btn_2);
        }
    }

    public void setText(CharSequence text) {
        inputText.setText(text);
    }

    public void setBtnOnClickListener(OnClickListener listener) {
        if (editable) {
            searchBtn.setOnClickListener((v) ->
                    listener.onClick((Editable) inputText.getText()));
        } else {
            throw new IllegalStateException("SearchBar is uneditable!");
        }
    }

    public interface OnClickListener {
        void onClick(@Nullable Editable inputText);
    }
}
