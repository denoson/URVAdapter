package com.sas.demo.urvadapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.urvadapter.AdapterTest;
import com.sas.urvadapter.IURVEvents;
import com.sas.urvadapter.IURVTabEvents;
import com.sas.urvadapter.URVAdapter;
import com.sas.urvadapter.URVItem;
import com.sas.urvadapter.URVTabButtons;

/**
 * Date: 2024.09.25
 * Author: Den Vigovski
 * Universal RecycleView adapter
 * Licence: This is an experimental project for educational purposes. Not for commercial use.
 */

public class MainActivity extends AppCompatActivity {

    private URVAdapter adapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initResources();
        initAdapter();

        fillDemoMessages();

        TextView lbl = findViewById(R.id.lbl);
        lbl.setText("Adapter " + adapter.getClass().getSimpleName() + " Version " + URVAdapter.VERSION);

        initTabs();
    }


    private void fillDemoMessages() {
        URVItem item;

        int i = 0;
        for(int color : URVAdapter.COLORS_BCK) {
            item = adapter.addItem(i, 0, "Color[" + i + "] #" + IntToHex(color), "Lorem ipsum dolor sit amet, consectetur adipiscing elit " + i, null);
            item.setTextIcon("X");
            item.setCustomBackgroundColor(color);

            item.Counter.setVisible((i == 3) || (i == 5) || (i == 10));
            item.Counter.setCounter(String.valueOf(i));
            item.Counter.setUnits("Items");

            i++;
        }

        adapter.notifyDataSetChanged();
    }


    private void initAdapter() {
        list = findViewById(R.id.rvList);
        adapter = new URVAdapter();
        adapter.initRecyclerView(getApplicationContext(), list);
        adapter.setupResourceHolders(R.id.title, R.id.descr, 0, R.id.bckPanel, R.id.txtIcon, R.id.msgbox);
        adapter.setupResourceItems(R.layout.item_msg_action, 0, 0, 0, 0);
        adapter.setMultiselect(true);
        adapter.setIconFont(Config.FONT_ICON);
        adapter.setTextIcons(true);
        adapter.setColorSelected(Color.rgb(00, 85, 255));
        adapter.setupCheckbox(URVAdapter.COLORS_BCK[9], "L",  URVAdapter.COLORS_BCK[17], "M");


        // Counter
        adapter.ResourceCounter.setup(R.id.pnlCounter, R.id.lblCounter, R.id.lblCounterUnits);
        adapter.ResourceCounter.setVisibleUnits(false);
        adapter.ResourceCounter.setEnabled(true);


        // Terminal - messages functions .............................................
        adapter.initTerminal(findViewById(R.id.edMessage), findViewById(R.id.btnSend), findViewById(R.id.lblTerminalBase));

        adapter.events = new IURVEvents() {
            @Override
            public void onItemClick(int index) {

            }

            @Override
            public void onLongClick(int index) {

            }

            @Override
            public boolean onAllowSelect(int index) {
                return false;
            }

            @Override
            public void onSelectionChanged() {

            }

            @Override
            public void onItemSwipe(int index, int direction) {

            }

            @Override
            public void onItemDrag(int index) {

            }

            @Override
            public void onCommand(String cmdBase, String userMessage) {

            }
        };

    }


    private void initResources() {
        if (Config.FONT_ICON == null) {
            try {
                Config.FONT_ICON = Typeface.createFromAsset(getAssets(), "fonts/console.ttf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    URVTabButtons tabs;

    private void initTabs() {
        tabs = new URVTabButtons(getApplicationContext());
        tabs.setupComponent(findViewById(R.id.pnlTabs), R.drawable.tab_normal, R.drawable.tab_active);

        tabs.addTab("Color");
        tabs.addTab("Model");
        tabs.addTab("Date");
        tabs.addTab("Time");
        tabs.addTab("Widgets");
        tabs.addTab("Plugins");
        tabs.addTab("Gauges");
        tabs.addTab("Servers");

        tabs.events = new IURVTabEvents() {
            @Override
            public void onTabSelected(int index) {
                URVItem rvi = tabs.items.get(index);
                adapter.addItem(index, 0, rvi.getTitle(), rvi.getDescription(), null);
                adapter.notifyDataSetChanged();
            }
        };

        tabs.setActiveTabIndex(0);
    }



    public String IntToHex(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        return ((value < 16) ? "0" : "") + hex;
    }
}