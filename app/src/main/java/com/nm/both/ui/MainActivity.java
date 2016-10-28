package com.nm.both.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.nm.base.app.BaseActivity;
import com.nm.both.R;
import com.nm.both.ui.im.ChatFragment;
import com.nm.both.ui.me.MyPageFragment;
import com.nm.both.ui.memory.MemoryFragment;
import com.nm.both.view.TabView;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    private static final String TAG_TAG_MEMORY = "memory";
    private static final String TAG_TAG_CHAT = "chat";
    private static final String TAG_TAG_MY_PAGE = "my page";


    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    private MemoryFragment mMemoryFragment;
    private ChatFragment mChatFragment;
    private MyPageFragment mMyPageFragment;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabLayout.setOnTabSelectedListener(mTabSelectedListener);

        TabLayout.Tab tab = mTabLayout.newTab()
                .setText(R.string.tab_text_memory)
                .setTag(TAG_TAG_MEMORY);
        mTabLayout.addTab(tab);
        tab.setCustomView(R.layout.tab_item_main);
        //没有此行代码，第一次进入时，首页没有选中
        ((TabView) tab.getCustomView()).setTab(tab).setSelected(true);

        tab = mTabLayout.newTab()
                .setText(R.string.tab_text_chat)
                //.setIcon(R.drawable.ic_tab_product)
                .setTag(TAG_TAG_CHAT);
        mTabLayout.addTab(tab);
        tab.setCustomView(R.layout.tab_item_main_remain);
        ((TabView) tab.getCustomView()).setTab(tab);


        tab = mTabLayout.newTab()
                .setText(R.string.tab_text_my_page)
                //.setIcon(R.drawable.ic_tab_im)
                .setTag(TAG_TAG_MY_PAGE);
        mTabLayout.addTab(tab);
        tab.setCustomView(R.layout.tab_item_main_remain);
        ((TabView) tab.getCustomView()).setTab(tab);
    }


    private TabLayout.OnTabSelectedListener mTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
