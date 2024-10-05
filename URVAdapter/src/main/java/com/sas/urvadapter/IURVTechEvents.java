package com.sas.urvadapter;

public interface IURVTechEvents {

    boolean onAllowSelect(int index);

    void onSelectionChanged();

    void onItemSwipe(int index, int direction);

    void onItemDrag(int index);

}
