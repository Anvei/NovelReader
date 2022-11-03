package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    public static final String EXTRA_NOVEL_HOME_NAME = "org.anvei.novel.home.name";

    public static final String EXTRA_NOVEL_HOME_AUTHOR = "org.anvei.novel.home.author";

    public static final String EXTRA_NOVEL_HOME_URL = "org.anvei.novel.home.url";

    public static final String EXTRA_NOVEL_HOME_COVER = "org.anvei.novel.home.cover";

    public static final String EXTRA_NOVEL_HOME_BRIEF = "org.anvei.novel.home.brief";

    public static final String EXTRA_NOVEL_HOME_WEBSITE = "org.anvei.novel.home.website";

    public static final String DATABASE_NAME = "anvei";

    public static final String TABLE_WEB_CACHE = "WebCache";

    public static final String TABLE_HISTORY = "History";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }*/
}
