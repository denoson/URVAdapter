package com.sas.urvadapter;

public class URVItemElement {

    private int idView = 0;
    private int logic = 0;
    private int valueType = 0;


    public URVItemElement(int idView, int logic, int valueType) {
        this.idView = idView;
        this.logic = logic;
        this.valueType = valueType;
    }



    public int getIdView() {
        return idView;
    }

    public void setIdView(int idView) {
        this.idView = idView;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }

    public int getValueType() {
        return valueType;
    }

    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
}
