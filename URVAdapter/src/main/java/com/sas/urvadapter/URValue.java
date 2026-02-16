package com.sas.urvadapter;

public class URValue {

    private int type = URVConst.ValueType.STRING;
    private String key = "";
    private boolean changed = false;

    private int valueInt = 0;
    private float valueFloat = 0;
    private String valueStr = "";
    private boolean valueBool = false;


    private float min = 0;
    private float max = 0;

    /**
     * Constructor ..................................
     */
    public URValue() {
    }

    public void setup(String key, String value) {
        this.type = URVConst.ValueType.STRING;
        this.key = key;
        this.valueStr = value;
        changed = false;
    }

    public void setup(String key, int value) {
        this.type = URVConst.ValueType.INTEGER;
        this.key = key;
        this.valueInt = value;
        changed = false;
    }

    public void setup(String key, float value) {
        this.type = URVConst.ValueType.FLOAT;
        this.key = key;
        this.valueFloat = value;
        changed = false;
    }

    public void setup(String key, boolean value) {
        this.type = URVConst.ValueType.BOOLEAN;
        this.key = key;
        this.valueBool = value;
        changed = false;
    }


    public void setupMinMax(float sMin, float sMax) {
        this.min = sMin;
        this.max = sMax;
    }

    public boolean isMinMaxExists() {
        return min != max;
    }

    public void setValueTypeString() {
        type = URVConst.ValueType.STRING;
    }

    public void setValueTypeInt() {
        type = URVConst.ValueType.INTEGER;
    }

    public void setValueTypeFloat() {
        type = URVConst.ValueType.FLOAT;
    }

    public void setValueTypeBool() {
        type = URVConst.ValueType.BOOLEAN;
    }



    public void inc(float addon) {
        switch (type) {

            case URVConst.ValueType.INTEGER:
                valueInt = valueInt + Math.round(addon);
                break;

            case URVConst.ValueType.FLOAT:
                valueFloat = valueFloat + addon;
                break;
        }

        checkMinMax();
    }

    public void dec(float addon) {
        switch (type) {

            case URVConst.ValueType.INTEGER:
                valueInt = valueInt - Math.round(addon);
                break;

            case URVConst.ValueType.FLOAT:
                valueFloat = valueFloat - addon;
                break;
        }
        checkMinMax();
    }


    private void checkMinMax() {
        if(!isMinMaxExists()) return;

        switch (type) {

            case URVConst.ValueType.INTEGER:
                if(valueInt < min) valueInt = Math.round(min);
                if(valueInt > max) valueInt = Math.round(max);
                break;

            case URVConst.ValueType.FLOAT:
                if(valueFloat < min) valueFloat = min;
                if(valueFloat > max) valueFloat = max;
                break;
        }
    }


    public int getType() {
        return type;
    }

    public boolean isChanged() {
        return changed;
    }

    public String getKey() {
        return key;
    }

    public void setValue(int valueInt) {
        this.valueInt = valueInt;
        changed = true;
    }

    public void setValue(float valueFloat) {
        this.valueFloat = valueFloat;
        changed = true;
    }

    public void setValue(String valueStr) {
        this.valueStr = valueStr;
        changed = true;
    }

    public void setValue(boolean valueBool) {
        this.valueBool = valueBool;
        changed = true;
    }




    public int getValueInt() {
        return valueInt;
    }


    public float getValueFloat() {
        return valueFloat;
    }


    public String getValueStr() {
        return valueStr;
    }

    public boolean getValueBool() {
        return valueBool;
    }


    public boolean isValueBool() {
        return type == URVConst.ValueType.BOOLEAN;
    }


    public String asString() {
        switch (type) {
            case URVConst.ValueType.INTEGER: return String.valueOf(valueInt);
            case URVConst.ValueType.FLOAT: return String.format("%.01f", valueFloat);
            case URVConst.ValueType.BOOLEAN: return valueBool ? "1" : "0";
            default: return valueStr;
        }
    }

    public boolean isInt() {
        return type == URVConst.ValueType.INTEGER;
    }

    public boolean isFloat() {
        return type == URVConst.ValueType.FLOAT;
    }

    public boolean isString() {
        return type == URVConst.ValueType.STRING;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

}
