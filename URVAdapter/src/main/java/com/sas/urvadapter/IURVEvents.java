package com.sas.urvadapter;

public interface IURVEvents {

    void onItemClick(int index);

    void onLongClick(int index);

    boolean onAllowSelect(int index);

    void onSelectionChanged();

    void onItemSwipe(int index, int direction);

    void onItemDrag(int index);

    void onCommand(final String cmdBase, String userMessage);
}
