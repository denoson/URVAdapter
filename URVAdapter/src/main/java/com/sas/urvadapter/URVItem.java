package com.sas.urvadapter;

public class URVItem {

    public URVCounterValue Counter;
    public URVIcon Icon;

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
    private int filter = 0;
    private int mode = 0;
    private int state = 0;
    private int clickAction = 0;

    private boolean selected = false;
    private boolean focused = false;
    private boolean checked = false;
    private boolean visible = true;
    private boolean enabled = true;

    private String title = "";
    private String description = "";
    private String keywords = "";
    private String hint = "";
    private String info = "";
    private String note = "";

    private String valueString;
    private int valueInt = 0;
    private float valueFloat = 0f;
    private boolean valueBool = false;

    private int customBackgroundColor = 0;

    private boolean canSwipe = false;
    private boolean canDrag = false;
    private int itemMode = 0;

    private String column1V = "";
    private String column1L = "";

    private String column2V = "";
    private String column2L = "";

    private String column3V = "";
    private String column3L = "";

    private String column4V = "";
    private String column4L = "";

    private String textIconA = "";
    private String textIconB = "";
    private String textIconC = "";


    private URVAbstractCustomData customData = null;




    public URVItem(int id, int viewType, String title, String description, URVAbstractCustomData customData) {
        this.id = id;
        this.viewType = viewType;
        this.title = title;
        this.description = description;

        Counter = new URVCounterValue();
        Icon = new URVIcon();

        if(customData != null) {
            this.customData = customData;
        }
    }




    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getClickAction() {
        return clickAction;
    }

    public void setClickAction(int clickAction) {
        this.clickAction = clickAction;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public float getValueFloat() {
        return valueFloat;
    }

    public void setValueFloat(float valueFloat) {
        this.valueFloat = valueFloat;
    }

    public boolean isValueBool() {
        return valueBool;
    }

    public void setValueBool(boolean valueBool) {
        this.valueBool = valueBool;
    }

    public int getCustomBackgroundColor() {
        return customBackgroundColor;
    }

    public void setCustomBackgroundColor(int customBackgroundColor) {
        this.customBackgroundColor = customBackgroundColor;
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

    public int getItemMode() {
        return itemMode;
    }

    public void setItemMode(int itemMode) {
        this.itemMode = itemMode;
    }

    public URVAbstractCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(URVAbstractCustomData customData) {
        this.customData = customData;
    }


    public String getColumn1V() {
        return column1V;
    }

    public void setColumn1V(String column1V) {
        this.column1V = column1V;
    }

    public String getColumn1L() {
        return column1L;
    }

    public void setColumn1L(String column1L) {
        this.column1L = column1L;
    }

    public String getColumn2V() {
        return column2V;
    }

    public void setColumn2V(String column2V) {
        this.column2V = column2V;
    }

    public String getColumn2L() {
        return column2L;
    }

    public void setColumn2L(String column2L) {
        this.column2L = column2L;
    }

    public String getColumn3V() {
        return column3V;
    }

    public void setColumn3V(String column3V) {
        this.column3V = column3V;
    }

    public String getColumn3L() {
        return column3L;
    }

    public void setColumn3L(String column3L) {
        this.column3L = column3L;
    }

    public String getColumn4V() {
        return column4V;
    }

    public void setColumn4V(String column4V) {
        this.column4V = column4V;
    }

    public String getColumn4L() {
        return column4L;
    }

    public void setColumn4L(String column4L) {
        this.column4L = column4L;
    }

    public String getTextIconA() {
        return textIconA;
    }

    public void setTextIconA(String textIconA) {
        this.textIconA = textIconA;
    }

    public String getTextIconB() {
        return textIconB;
    }

    public void setTextIconB(String textIconB) {
        this.textIconB = textIconB;
    }

    public String getTextIconC() {
        return textIconC;
    }

    public void setTextIconC(String textIconC) {
        this.textIconC = textIconC;
    }
}
