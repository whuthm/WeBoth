package com.nm.base.app;

import android.content.Context;
import android.content.Intent;

/**
 * Created by huangming on 2016/10/1.
 */

public class Presenter {

    private Context context;
    private View view;

    public Presenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void onResume() {
    }

    public void onAttach() {
    }

    public void onDestroy() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public interface View {
    }

}
