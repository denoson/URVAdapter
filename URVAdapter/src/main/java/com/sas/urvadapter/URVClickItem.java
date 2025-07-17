package com.sas.urvadapter;

import android.view.View;

public class URVClickItem {

    private int idView = 0;
    private int id = 0;


    public URVClickItem(int idView, int id) {
        this.idView = idView;
        this.id = id;
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
}
