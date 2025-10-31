package com.sas.urvadapter;

public class URVItemElement {

    private int idView = 0;
    private int logic = 0;
    private int valueType = 0;
    private int action = URVConst.LabelAction.CUSTOM;


    public URVItemElement(int idView, int logic, int valueType, int act) {
        this.idView = idView;
        this.logic = logic;
        this.valueType = valueType;
        this.action = act;
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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
