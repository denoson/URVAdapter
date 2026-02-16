package com.sas.demo.urvadapter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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
import com.sas.urvadapter.URVSingleChoiceDialog;
import com.sas.urvadapter.URValue;

import android.graphics.Color;
import java.util.Random;

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
        adapter.initRecyclerView(this, list, true, 0);
        adapter.initDefaultListParams(true, Config.FONT_ICON);

        adapter.eventsItem = new IURVItemEvents() {
            @Override
            public void onItemClick(int index) {
                URVItem item = adapter.getItem(index);

                Log.d("DemoPropertyActivity",
                        String.format("(a) onItemClick index: %d, action: %d, key: %s, value: %s",
                                index,
                                item.getAction(),
                                item.Value.getKey(),
                                item.Value.asString()
                        ));

                switch (item.getViewType()) {

                    case 2:
                        Log.d("DemoPropertyActivity", "(a) update random color...");
                        item.setMarkerColor(adapter.getLibColor(adapter.getRandomNum(0, 15)));
                        adapter.notifyItemChanged(index);
                        break;

                }

                showDialoger();

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

    private void showDialoger() {

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


/*
        // [4] Item Property marker
        item = adapter.addItem(0, 4, "Item type 4x0", "Property value editor, no min, max", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-A", 100);
        item.initEditorPlusMinus();


        // [4] Item Property marker, min, max
        item = adapter.addItem(0, 4, "Item type 4x1", "Property value editor, min: 10, max: 20", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-B", 15);
        item.Value.setupMinMax(10, 20);
        item.initEditorPlusMinus();


        // [4] Item Property marker, min, max
        item = adapter.addItem(0, 4, "Item type 4x2", "Property value editor, min: 0, max: 3", null);
        //item.setCustomBackgroundColor(adapter.HexToColor("#303030", 0));
        item.Icon.setIconText("X");
        adapter.setupItemModeProperty(item);
        item.Value.setup("key-C", 0.3f);
        item.Value.setupMinMax(0, 5);
        item.setValueChangeStep(0.3f);
        item.initEditorPlusMinus();
*/
        adapter.addPropertyNumber(0, "Input INT number", "free value, no min max", "num-a", 123, 0, 0, 0, "X");
        adapter.addPropertyNumber(0, "Input INT number", "free value, with min max (0-100)", "num-b", 45, 0, 100, 0, "X");
        adapter.addPropertyNumberF(0, "Input FLOAT number", "free value, min max step", "num-c", 1.5f, 0.5f, 5.8f, 0.3f, "X");


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


        // Boolean property
        adapter.addPropertyCheckbox(0, "Checkbox-A", "Boolean property: true or false", "key-bool-a", true);
        adapter.addPropertyCheckbox(0, "Checkbox-B", "Boolean property: true or false", "key-bool-b", false);



        // String Property
        adapter.addPropertyText(0, "Car title", "c-title", "Hummer", "G");
        adapter.addPropertyText(0, "Music", "c-mus", "", "J");

        String[] options = {"Red", "Green", "Blue"};
        adapter.addPropertyList(0, "Select Color", "clr", options, 0, "R");

        String[] icons = {"R", "G", "B"};
        adapter.addPropertyList(0, "Select Color", "clr", options, icons, 0);


        // Color Property
        adapter.addPropertyColorExt(0, "Color-A", "External color picker example", "color-a", 0xFFF44336, "X");
        adapter.addPropertyColorExt(0, "Color-B", "External color picker example", "color-b", 0xFF607D8B, "X");
        URVItem itemC =  adapter.addPropertyColorExt(0, "Color-C (+Alpha)", "External color picker example", "color-c", 0xFFCDDC39, "X");
        itemC.setUseAlpha(true);
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


    /**
     * Генерирует случайный непрозрачный цвет (альфа = 255).
     * @return цвет в формате 0xFFRRGGBB
     */
    private static final Random random = new Random();
    public static int randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}