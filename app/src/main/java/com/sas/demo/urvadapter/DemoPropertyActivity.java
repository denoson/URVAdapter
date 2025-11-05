package com.sas.demo.urvadapter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.urvadapter.IURVItemEvents;
import com.sas.urvadapter.IURVPropertyEvents;
import com.sas.urvadapter.URVAdapter;
import com.sas.urvadapter.URVConst;
import com.sas.urvadapter.URVItem;
import com.sas.urvadapter.URVItemClicker;
import com.sas.urvadapter.URValue;

public class DemoPropertyActivity extends AppCompatActivity {

    private URVAdapter adapter;
    private RecyclerView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_property);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initResources();
        initAdapter();
    }



    private void initAdapter() {
        Log.d(getClass().getSimpleName(), "initAdapter");
        list = findViewById(R.id.rvList);
        adapter = new URVAdapter();
        adapter.initRecyclerView(getApplicationContext(), list, true, 0);
        adapter.initDefaultListParams(true, Config.FONT_ICON);

        adapter.eventsItem = new IURVItemEvents() {
            @Override
            public void onItemClick(int index) {

            }

            @Override
            public void onLongClick(int index) {

            }

            @Override
            public void onClickEx(int index, int id) {

            }
        };


        adapter.eventProperty = new IURVPropertyEvents() {
            @Override
            public void onPropertyChanges(int itemIndex, int action, int idClicker, URValue value) {

                Log.d("DemoPropertyActivity",
                        String.format("onPropertyChanges itemIndex: %d, action: %d, key: %s, value: %s",
                                itemIndex,
                                action,
                                value.getKey(),
                                value.asString()
                        ));
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        fillItemsDifType();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    private void fillItemsDifType() {
        toLog("fillItemsDifType");
        URVItem item;
        adapter.setDebug(true);
        adapter.clear();
        adapter.initListProperties(true);
        // adapter.setDefaultItemBackgroundColor(0xFF414863); // default color for all list items



        // [0] Common list item
        item = adapter.addItem(0, 0, "Item type 0x0", "Simple common type", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");

        // [0] common item with custom background
        item = adapter.addItem(0, 0, "Item type 0x1", "Simple common type with background", null);
        item.setCustomBackgroundColor(adapter.HexToColor("#162b45", 0));
        item.Icon.setIconText("X");


        // [1] Icon with counter 123
        item = adapter.addItem(0, 1, "Item type 1", "Item with counter", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#162b45", 0));
        item.Icon.setIconText("X");
        item.Counter.setCounter("120");
        item.Counter.setUnits("KPH");




        // [2.1] Item Color marker
        item = adapter.addItem(0, 2, "Item type 1", "Simple color marker", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#162b45", 0));
        item.setMarkerColor(adapter.HexToColor("#f70000", 0));
        item.Icon.setIconText("X");

        // [2.2] Item Color marker
        item = adapter.addItem(0, 2, "Item type 1", "Simple color marker", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#162b45", 0));
        item.setMarkerColor(adapter.HexToColor("#00f74a", 0));
        item.Icon.setIconText("X");

        // [2.3] Item Color marker
        item = adapter.addItem(0, 2, "Item type 1", "Simple color marker", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#162b45", 0));
        item.setMarkerColor(adapter.HexToColor("#0036f7", 0));
        item.Icon.setIconText("X");



/*
        // [3] Zones
        item = adapter.addItem(0, 3, "Item type 3", "Item for zones editor", null);
        item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");

        item.setColumn1V("90");
        item.setColumn1L("Start");

        item.setColumn2V("120");
        item.setColumn2L("End");

        item.setTextIconA("1");
        item.setTextIconB("2");
        item.setTextIconC("3");
        item.setTextIconD("4");


*/

        // [4] Item Property marker
        item = adapter.addItem(0, 4, "Item type 4x0", "Property value editor, no min, max", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-A", 100);
        item.initEditorPlusMinus();


        // [4] Item Property marker, min, max
        item = adapter.addItem(0, 4, "Item type 4x0", "Property value editor, min: 10, max: 20", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-B", 15);
        item.Value.setupMinMax(10, 20);
        item.initEditorPlusMinus();


        // [4] Item Property marker, min, max
        item = adapter.addItem(0, 4, "Item type 4x0", "Property value editor, min: 0, max: 3", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-C", 0.3f);
        item.Value.setupMinMax(0, 5);
        item.setValueChangeStep(0.3f);
        item.initEditorPlusMinus();



        // [5] Item Property marker
        item = adapter.addItem(0, 5, "Item type 5", "Item with 2 buttons", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-D", "..");
        item.setTextIconA("1");
        item.setTextIconB("2");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_1), "VISIBLE");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_2), "INVISIBLE");


        // [6] Item Property marker
        item = adapter.addItem(0, 6, "Item type 6", "Item with 3 buttons", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-E", "...");
        item.setTextIconA("1");
        item.setTextIconB("2");
        item.setTextIconC("3");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_1), "LEFT");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_2), "CENTER");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_3), "RIGHT");



        // [7] Item Property marker
        item = adapter.addItem(0, 7, "Item type 7", "Item with 4 buttons", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-F", "....");
        item.setTextIconA("1");
        item.setTextIconB("2");
        item.setTextIconC("3");
        item.setTextIconD("4");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_1), "Q1");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_2), "Q2");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_3), "Q3");
        item.setClickerValue(String.valueOf(URVConst.ClickerID.BUTTON_4), "Q4");
    }



    private void fillItemsDifType_OLD() {
        toLog("fillItemsDifType");
        URVItem item;
        adapter.setDebug(true);
        adapter.clear();
        adapter.initListProperties(true);

        for(int iType=0; iType < 8; iType++) {

            for(int i=0; i < 3; i++) {
                item = adapter.addItem(iType*10 + i, iType, String.format("Item %d-%d", iType, i), "current type " + i, null);
                item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
                item.Icon.setIconText("X");

                item.setValueInt(adapter.getRandomNum(10, 600));
                item.setValueFloat(item.getValueInt());

                item.setButton1Label("-");
                item.setButton2Label("...");
                item.setButton3Label("+");

                item.Counter.setCounter("" + iType*10 + i);
                item.Counter.setUnits("counter");

                if(iType == 2) {
                    item.setCustomBackgroundColor(URVAdapter.COLORS_BCK[i]); //adapter.HexToColor(URVAdapter.COLORS_BCK[i], 0)
                }

                item.setTextIconA("1");
                item.setTextIconB("2");
                item.setTextIconC("3");
                item.setTextIconD("4");

                if(iType == 5) {
                    item.setColumn1L("START");
                    item.setColumn1V("" + (i * 1000));

                    item.setColumn2L("END");
                    item.setColumn2V("" + (i * 1000 + 789));
                }

                adapter.setupItemModeProperty(item);
                item.Value.setup("key-" + i, adapter.getRandomNum(10, 100));
            }

        }

        adapter.updateItemsLabelActions();
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

    public String IntToHex(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        return ((value < 16) ? "0" : "") + hex;
    }

    private void toLog(String info) {
        Log.d(getClass().getSimpleName(), info);
    }
}