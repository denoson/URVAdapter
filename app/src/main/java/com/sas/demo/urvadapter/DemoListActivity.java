package com.sas.demo.urvadapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.urvadapter.IURVItemEvents;
import com.sas.urvadapter.IURVTabEvents;
import com.sas.urvadapter.IURVTechEvents;
import com.sas.urvadapter.IURVTerminalEvents;
import com.sas.urvadapter.URVAdapter;
import com.sas.urvadapter.URVItem;
import com.sas.urvadapter.URVTabButtons;

public class DemoListActivity extends AppCompatActivity {

    private URVAdapter adapter;
    private RecyclerView list;
    private URVTabButtons tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initResources();
        //initAdapter();
        //fillItemsStyle001();
        //fillCustomItems();

        initTabs();
        initSearch();
    }

    private void destroyAdapter() {
        list = null;
        if(adapter != null) adapter.clear();
        adapter = null;
    }

    private void initAdapter() {
        list = findViewById(R.id.rvList);
        adapter = new URVAdapter();
        adapter.initRecyclerView(getApplicationContext(), list, true, 0);

        //adapter.initDefaultResources();
        // if use custom resources:
        //adapter.setupResourceHolders(R.id.title, R.id.descr, R.id.bckPanel, R.id.msgbox);
        //adapter.setupResourceImage(R.id.imgBck, R.id.imgBitmap, R.id.imgLbl);
        //adapter.setupResourceItems(R.layout.item_msg_action, 0, 0, 0, 0);

        adapter.setMultiselect(true);
        adapter.setIconFont(Config.FONT_ICON);
        adapter.setTextIcons(true);
        adapter.setColorSelected(Color.rgb(00, 85, 255));
        adapter.setupDefaultCheckbox("L", "M");


        // Counter
        //adapter.ResourceCounter.setup(R.id.pnlCounter, R.id.lblCounter, R.id.lblCounterUnits);
        //adapter.ResourceCounter.setVisibleUnits(false);
        //adapter.ResourceCounter.setEnabled(true);

        adapter.eventsItem = new IURVItemEvents() {
            @Override
            public void onItemClick(int index) {

            }

            @Override
            public void onLongClick(int index) {

            }
        };

        adapter.eventsTech = new IURVTechEvents() {
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
        };


        // Terminal - messages functions .............................................
        adapter.initTerminal(findViewById(R.id.edMessage), findViewById(R.id.btnSend), findViewById(R.id.lblTerminalBase));

        adapter.eventsTerminal = new IURVTerminalEvents() {
            @Override
            public void onCommand(String cmdBase, String userMessage) {

            }
        };
    }



    private void fillItemsStyle001() {
        toLog("fillItemsStyle001");
        URVItem item;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_demo);

        adapter.clear();
        adapter.initList001();
        adapter.Properties.setAutoHideEmpty(false);

        int i = 0;
        for (int color : URVAdapter.COLORS_BCK) {
            item = adapter.addItem(i, "Color[" + i + "] #" + IntToHex(color), "Lorem ipsum dolor sit amet, consectetur adipiscing elit..." + i);
            item.setCustomBackgroundColor(color);

            if ((i == 2) || (i == 5) || (i == 8) || (i == 13)) {
                item.Icon.setIconBitmap(bmp);
            } else {
                item.Icon.setIconText("X");
            }

            adapter.notifyItemInserted(i);
            i++;
        }

        toLog("total items: " + adapter.items.size());
    }


    private void fillItemsStyle002() {
        toLog("fillItemsStyle002");
        URVItem item;

        adapter.clear();
        adapter.initList002();

        int i = 0;
        for (int color : URVAdapter.COLORS_BCK) {
            item = adapter.addItem(i, "Color[" + i + "] #" + IntToHex(color), "Lorem ipsum dolor sit amet, consectetur adipiscing elit..." + i);
            item.setCustomBackgroundColor(color);
            item.Icon.setIconText("X");
            item.Counter.setCounter("" + i*3);
            item.Counter.setUnits("counter");
            //adapter.notifyItemInserted(i);
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    private void fillItemsStyle003() {
        toLog("fillItemsStyle003");
        URVItem item;
        adapter.clear();
        adapter.initList003();

        int i = 0;
        for (int color : URVAdapter.COLORS_BCK) {
            item = adapter.addItem(i, "Color[" + i + "] #" + IntToHex(color), "Lorem ipsum dolor sit amet, consectetur adipiscing elit..." + i);
            item.setCustomBackgroundColor(color);
            item.Icon.setIconText("X");
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    private void fillItemsStyle004() {
        toLog("fillItemsStyle004");
        URVItem item;
        adapter.clear();
        adapter.initList004();

        int i = 0;
        for (int color : URVAdapter.COLORS_BCK) {
            item = adapter.addItem(i, "", "");
            item.setCustomBackgroundColor(color);

            item.setColumn1L("START");
            item.setColumn1V("" + (i * 1000));

            item.setColumn2L("END");
            item.setColumn2V("" + (i * 1000 + 789));

            item.setTextIconA("1");
            item.setTextIconB("2");
            item.setTextIconC("3");


            i++;
        }

        adapter.notifyDataSetChanged();
    }



    private void fillCustomItems() {
        URVItem item;
        adapter.clear();
        adapter.Properties.setAutoHideEmpty(false);
        adapter.setupResourceItems(R.layout.custom_list_item_zone, 0, 0, 0, 0);
        //adapter.ResourceItems.setId00(R.layout.custom_list_item_zone);
        adapter.ResourceCounter.setup(R.id.pnlCounter, R.id.lblCounter, 0);
        adapter.setupResourceHolders(R.id.bckPanel, R.id.msgbox);

        adapter.addClickItem(R.id.pnlStart, 1);
        adapter.addClickItem(R.id.pnlEnd, 2);
        adapter.addClickItem(R.id.pnlCounter, 3);

        for(int i =0; i < 10; i++) {
            //URVAbstractCustomData cData = new URVAbstractCustomData();

            item = adapter.addItem(0, 0, String.format("00%d", i), String.format("00%d", i), null);
            item.Counter.setVisible(false);
        }

        adapter.notifyDataSetChanged();
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



    private void initTabs() {
        tabs = new URVTabButtons(getApplicationContext());
        tabs.setupComponent(findViewById(R.id.pnlTabs), R.drawable.tab_normal, R.drawable.tab_active);

        tabs.addTab("Style 001");
        tabs.addTab("Style 002");
        tabs.addTab("Style 003");
        tabs.addTab("Date");
        tabs.addTab("Time");
        tabs.addTab("Widgets");
        tabs.addTab("Plugins");
        tabs.addTab("Gauges");
        tabs.addTab("Servers");

        tabs.events = new IURVTabEvents() {
            @Override
            public void onTabSelected(int index) {
                toLog("onTabSelected: " + index);
                URVItem rvi = tabs.items.get(index);
                //adapter.addItem(index, 0, rvi.getTitle(), rvi.getDescription(), null);
                //adapter.notifyDataSetChanged();
                //adapter.search(rvi.getTitle(), true, true, true, false, false, false, false);
                updateListStyle(index);
            }
        };
        tabs.setActiveTabIndex(0);
    }

    private void updateListStyle(int index) {
      toLog("updateListStyle: " + index);
      destroyAdapter();
      initAdapter();

      switch (index) {
          case 0: fillItemsStyle001();
          break;

          case 1: fillItemsStyle002();
          break;

          case 2: fillItemsStyle003();
              break;

          case 3: fillItemsStyle004();
              break;
      }
    }


    private EditText edSearch = null;

    private void initSearch() {
        edSearch = findViewById(R.id.edSearch);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.search(edSearch.getText().toString(), true, true, true, false, false, false, false);
            }
        });

    }

    public String IntToHex(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        return ((value < 16) ? "0" : "") + hex;
    }

    private void toLog(String info) {
        Log.d(getClass().getSimpleName(), info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startRandomDesign(100);
    }
}