package com.sas.urvadapter;

public interface IURVPropertyEvents {

    void onPropertyChanges(int itemIndex, int action, int idClicker, URValue value);

}