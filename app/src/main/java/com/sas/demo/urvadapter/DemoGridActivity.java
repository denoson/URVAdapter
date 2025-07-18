package com.sas.demo.urvadapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class DemoGridActivity extends AppCompatActivity {

    private URVAdapter adapter;
    private RecyclerView list;
    private URVTabButtons tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo_grid);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initResources();
        initAdapter();
        fillDemoMessages();

        initTabs();
        initSearch();
    }


    private void initAdapter() {
        list = findViewById(R.id.rvList);
        adapter = new URVAdapter();
        adapter.initRecyclerView(getApplicationContext(), list, true, 3);

        adapter.setMultiselect(true);
        adapter.setIconFont(Config.FONT_ICON);
        adapter.setTextIcons(true);
        adapter.setColorSelected(Color.rgb(00, 85, 255));
        adapter.setupDefaultCheckbox("L", "M");


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
    }


    private void fillDemoMessages() {
        toLog("fillDemoMessages");
        URVItem item;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.img_demo);

        adapter.clear();
        adapter.setupGridRowsCols(getApplicationContext(), 2, 6); //adapter.setupGridRowsCols(getApplicationContext(),  adapter.getRandomNum(2, 6), adapter.getRandomNum(1, 6));
        adapter.Properties.setAutoHideEmpty(true);

        int[] arrImages = { R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06,
                R.drawable.img07, R.drawable.img08, R.drawable.img09 };

        for(int i=0; i < 50; i++) {
            item = adapter.addItem(i, "Photo-" + i, "Photo description color");
            item.Icon.setIconBitmap(BitmapFactory.decodeResource(getResources(), arrImages[adapter.getRandomNum(0, arrImages.length)]));

            if(i < 15) item.setDescription("");
            if(i < 10) item.setTitle("");

            toLog("add grid item: " + item.getTitle());
        }

        adapter.notifyDataSetChanged();
        toLog("total items: " + adapter.items.size());
    }



    private void startRandomDesign(int quantity) {
        new CountDownTimer(quantity * 2000, 2000) {
            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                fillDemoMessages();
            }
            public void onFinish() {
                // mTextField.setText("done!");
                toLog("random Complete");
            }
        }.start();
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
                //adapter.addItem(index, 0, rvi.getTitle(), rvi.getDescription(), null);
                //adapter.notifyDataSetChanged();
                //adapter.search(rvi.getTitle(), true, true, true, false, false, false, false);
                fillDemoMessages();
            }
        };
        tabs.setActiveTabIndex(0);
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