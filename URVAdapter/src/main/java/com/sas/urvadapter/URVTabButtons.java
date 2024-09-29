package com.sas.urvadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class URVTabButtons {

    public IURVTabEvents events = null;
    public ArrayList<URVItem> items;

    private Context ctx;
    private int currentId = 0;
    private int activeTabIndex = -1;


    private ViewGroup tabsPanel = null;
    private int idResNormal = 0;
    private int idResActive = 0;

    private int textColorNormal = Color.LTGRAY;
    private int textColorActive = Color.WHITE;


    public URVTabButtons(Context ctx) {
        this.ctx = ctx;
        items = new ArrayList<URVItem>();
    }

    public void setupComponent(ViewGroup pnl, int resNormal, int resActive) {
        tabsPanel = pnl;
        idResNormal = resNormal;
        idResActive = resActive;

        if(tabsPanel != null) {
            tabsPanel.removeAllViews();
        }
    }

    public URVItem addTab(String title) {
        URVItem tab = new URVItem(currentId, 0, title, "", new TabCustomData());
        addItemRaw(tab);
        return tab;
    }


    private void addItemRaw(URVItem item) {
        item.setIndex(items.size());
        items.add(item);
        currentId++;

        if(tabsPanel != null) {
            Button btn = new Button(ctx);
            if(idResNormal != 0) {
                btn.setBackgroundResource(idResNormal);
            }
            btn.setText(item.getTitle());
            btn.setTag(item.getIndex());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    setActiveTabIndex(index);
                }
            });

            TabCustomData tcd = (TabCustomData) item.getCustomData();
            if(tcd != null) {
                tcd.button = btn;
            }

            tabsPanel.addView(btn);
        }
    }


    public int getActiveTabIndex() {
        return activeTabIndex;
    }


    public void setActiveTabIndex(int activeTabIndex) {
        if(isValidIndex(activeTabIndex)) {
            this.activeTabIndex = activeTabIndex;
        } else {
            this.activeTabIndex = -1;
        }
        setActiveTab(this.activeTabIndex);
        if(events != null) {
            events.onTabSelected(this.activeTabIndex);
        }
    }

    private void setActiveTab(int index) {
        int i = 0;
        for(URVItem tab : items) {
            tab.setChecked(i == index);
            tab.setSelected(i == index);
            updateTabStyle(tab);
            i++;
        }
    }

    private void updateTabStyle(URVItem tab) {
        TabCustomData tcd = (TabCustomData) tab.getCustomData();
        if(tcd != null) {
            if(tcd.button != null) {
                tcd.button.setBackgroundResource(getResource(tab.isSelected()));

                tcd.button.setTextColor(tab.isSelected() ? textColorActive : textColorNormal);
            }
        }
    }


    private int getResource(boolean active) {
        return active ? idResActive : idResNormal;
    }

    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < items.size());
    }



    private class TabCustomData extends URVAbstractCustomData {

        public Button button = null;


    }
}
