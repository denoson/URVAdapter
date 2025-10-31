package com.sas.urvadapter;

import android.view.View;

public class URVClickItem {

    private int idView = 0;
    private int id = 0;
    private int action = URVConst.ClickerAction.CUSTOM;


    public URVClickItem(int idView, int id, int act) {
        this.idView = idView;
        this.id = id;
        this.action = act;
    }


    public int getIdView() {
        return idView;
    }

    public void setIdView(int idView) {
        this.idView = idView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
