package com.nm.base.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nm.base.log.Logger;

/**
 * Created by huangming on 2016/9/25.
 */

public class BaseFragment extends Fragment {
    
    private static final String TAG = "BaseFragment";
    
    protected FragmentHelper mFragHelper;
    protected ActionBarHelper mActionBarHelper;
    protected FragListener mFragListener;
    
    protected SparseArray<MenuItem> mMenuItemSparseArray = new SparseArray<>();
    
    protected boolean mFragSelected;
    private boolean mFragPendingSelected;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mFragHelper = new FragmentHelper();
        mFragHelper.onAttach(this);
        mActionBarHelper = new ActionBarHelper();
        mActionBarHelper.onAttach(this);
        setFragSelected(mFragPendingSelected);
        return getLayoutResId() > 0 ? inflater.inflate(getLayoutResId(), container, false)
                : super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.i(TAG, "onViewCreated");
        initArguments();
        initView();
    }
    
    protected void initArguments() {
        
    }
    
    protected void initView() {
        
    }
    
    protected void showLoadingDialog(String title, String message) {
        if (getActivity() instanceof BaseActivity && isAttached()) {
            ((BaseActivity) getActivity()).showLoadingDialog(title, message);
        }
    }
    
    protected void showLoadingDialog(int titleResId, int msgResId) {
        if (getActivity() instanceof BaseActivity && isAttached()) {
            ((BaseActivity) getActivity()).showLoadingDialog(titleResId, msgResId);
        }
    }
    
    protected void hideLoadingDialog() {
        if (getActivity() instanceof BaseActivity && isAttached()) {
            ((BaseActivity) getActivity()).hideLoadingDialog();
        }
    }
    
    protected View findViewById(int id) {
        if (getView() != null) {
            return getView().findViewById(id);
        }
        return null;
    }
    
    protected int getLayoutResId() {
        return 0;
    }
    
    public BaseFragment setFragListener(FragListener l) {
        this.mFragListener = l;
        return this;
    }
    
    protected FragListener getFragListener() {
        return mFragListener;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Logger.i(TAG, "onStart");
        if (mFragListener != null) {
            mFragListener.onStart();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        Logger.i(TAG, "onStop");
        if (mFragListener != null) {
            mFragListener.onStop();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Logger.i(TAG, "onPause");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i(TAG, "onDestroyView");
        mFragHelper.onDetach();
        mActionBarHelper.onDetach();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem menuItem = menu.getItem(i);
                if (menuItem != null && menuItem.getItemId() > 0) {
                    mMenuItemSparseArray.put(menuItem.getItemId(), menuItem);
                }
            }
        }
        displayMenuItems();
        Logger.i(getClass().getSimpleName(), "onCreateOptionsMenu");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return onNavClick();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void displayMenuItems() {
        int[] itemIds = getMenuItemIds();
        if (itemIds != null) {
            int size = mMenuItemSparseArray.size();
            for (int i = 0; i < size; i++) {
                MenuItem menuItem = mMenuItemSparseArray
                        .get(mMenuItemSparseArray.keyAt(i));
                menuItem.setVisible(containMenuItemId(itemIds, menuItem.getItemId()));
            }
        }
        else {
            int size = mMenuItemSparseArray.size();
            for (int i = 0; i < size; i++) {
                MenuItem menuItem = mMenuItemSparseArray
                        .get(mMenuItemSparseArray.keyAt(i));
                menuItem.setVisible(true);
            }
        }
    }
    
    private boolean containMenuItemId(int[] itemIds, int id) {
        for (int itemId : itemIds) {
            if (itemId == id) {
                return true;
            }
        }
        return false;
    }
    
    protected int[] getMenuItemIds() {
        return null;
    }
    
    // 是否拦截
    protected boolean onNavClick() {
        return false;
    }
    
    public final BaseFragment setFragSelected(boolean selected) {
        if (!isAttached()) {
            mFragPendingSelected = selected;
            return this;
        }
        boolean preSelected = mFragSelected;
        mFragSelected = selected;
        if (selected) {
            if (preSelected) {
                onFragReselected();
            }
            else {
                onFragSelected();
            }
        }
        else if (preSelected != selected) {
            onFragUnselected();
        }
        return this;
    }
    
    @CallSuper
    public void onFragSelected() {
        mActionBarHelper.setTitle(getTitle());
        displayMenuItems();
    }
    
    @CallSuper
    public void onFragUnselected() {}
    
    @CallSuper
    public void onFragReselected() {}
    
    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
    
    private boolean isAttached() {
        return mFragHelper != null && mFragHelper.isAttached();
    }
    
    protected String getTitle() {
        return "";
    }
    
    public interface FragListener {
        
        // 当前Fragment开始显示
        void onStart();
        
        // 当前Fragment开始不显示
        void onStop();
    }
}
