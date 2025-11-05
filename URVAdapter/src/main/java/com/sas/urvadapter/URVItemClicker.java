package com.sas.urvadapter;

public class URVItemClicker {

    private int idView = 0;
    private int id = 0;
    private int action = URVConst.ClickerAction.CUSTOM;


    private boolean valueExists = false;
    private String valueStr = "";
    private Boolean valueBool = false;
    private int valueInt = 0;
    private float valueFloat = 0;



    public URVItemClicker(int idView, int id, int act) {
        this.idView = idView;
        this.id = id;
        this.action = act;
    }

    public boolean isValueExists() {
        return valueExists;
    }

    public void setValue(String value) {
        valueStr = value;
        valueExists = true;
    }

    public void setValue(int value) {
        valueInt = value;
        valueExists = true;
    }

    public void setValue(float value) {
        valueFloat = value;
        valueExists = true;
    }

    public void setValue(boolean value) {
        valueBool = value;
        valueExists = true;
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


    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public Boolean getValueBool() {
        return valueBool;
    }

    public void setValueBool(Boolean valueBool) {
        this.valueBool = valueBool;
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
}
