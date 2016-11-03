package com.nm.both.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nm.base.app.BaseActivity;
import com.nm.both.R;
import com.nm.both.view.table.TableAdapter;
import com.nm.both.view.table.TableCellAdapter;
import com.nm.both.view.table.TableCellLayout;
import com.nm.both.view.table.TableView;

/**
 * Created by huangming on 2016/10/26.
 */

public class SplashActivity extends BaseActivity {
    
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBarHelper.setHomeButtonEnabled(false);
        
        TableCellAdapter dataAdapter = new TableCellAdapter() {
            @Override
            public int getCountX() {
                return 15;
            }
            
            @Override
            public int getCountY() {
                return 15;
            }
            
            @Override
            public View getView(int cellX, int cellY, ViewGroup parent) {
                TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.table_item_data, parent, false);
                StringBuilder sb = new StringBuilder(
                        "cell(" + cellX + ", " + cellY + ")");
                
//                for (int i = 0; i < cellX * cellY / 25; i++) {
//                    sb.append("(" + i + ")");
//                }
//
                for (int j = 0; j < cellX / 2; j++) {
                    sb.append("\n(" + j + ")");
                }
                textView.setText(sb.toString());
                return textView;
            }
        };

        TableCellLayout tableCellLayout = (TableCellLayout) findViewById(R.id.table_cell_layout);
        //tableCellLayout.setAdapter(dataAdapter);

        TableView tableView = (TableView) findViewById(R.id.table_view);
        //tableView.setAdapter(new TableAdapter(dataAdapter, null, null));
        tableView.setAdapter(new TableAdapter(dataAdapter));
    }
}
