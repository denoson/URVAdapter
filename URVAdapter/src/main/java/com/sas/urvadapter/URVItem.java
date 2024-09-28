package com.sas.urvadapter;

public class URVItem {

    public URVCounterValue Counter;

    private int index = -1;

    private long id;
    private long iddb;
    private long idOwner = 0;

    private String uid = "";
    private int action = 0;
    private int type = 0;
    private int group = 0;
    private int viewType = 0;
    private int logic = 0;
    private int marker = 0;
    private int clickAction = 0;

    private boolean selected = false;
    private boolean focused = false;
    private boolean checked = false;

    private String textIcon = "";
    private String title;
    private String description;

    private int valueInt = 0;
    private String valueString;

    private int customBackgroundColor = 0;

    private boolean canSwipe = false;
    private boolean canDrag = false;
    private int itemMode = 0;



    private URVAbstractCustomData customData = null;


    public URVItem(int id, int viewType, String title, String description, URVAbstractCustomData customData) {
        this.id = id;
        this.viewType = viewType;
        this.title = title;
        this.description = description;

        Counter = new URVCounterValue();

        if(customData != null) {
            this.customData = customData;
        }
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIddb() {
        return iddb;
    }

    public void setIddb(long iddb) {
        this.iddb = iddb;
    }

    public long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(long idOwner) {
        this.idOwner = idOwner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getTextIcon() {
        return textIcon;
    }

    public void setTextIcon(String textIcon) {
        this.textIcon = textIcon;
    }

    public boolean isCustomDataExists() {
        return customData != null;
    }

    public URVAbstractCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(URVAbstractCustomData customData) {
        this.customData = customData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getClickAction() {
        return clickAction;
    }

    public void setClickAction(int clickAction) {
        this.clickAction = clickAction;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isCanSwipe() {
        return canSwipe;
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    public boolean isCanDrag() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCustomBackgroundColor() {
        return customBackgroundColor;
    }

    public void setCustomBackgroundColor(int customBackgroundColor) {
        this.customBackgroundColor = customBackgroundColor;
    }

    public int getItemMode() {
        return itemMode;
    }

    public void setItemMode(int itemMode) {
        this.itemMode = itemMode;
    }
}
