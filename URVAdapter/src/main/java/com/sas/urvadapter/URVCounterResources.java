package com.sas.urvadapter;

public class URVCounterResources {

    private boolean enabled = false;
    private int box = 0;
    private int counter = 0;
    private int units = 0;

    private boolean visibleUnits = false;

    public void setup(int idBox, int idCounter, int idUnits) {
        box = idBox;
        counter = idCounter;
        units = idUnits;
        enabled = true;
    }


    public boolean useBox() {
        return box != 0;
    }

    public boolean useCounter() {
        return counter != 0;
    }

    public boolean useUnits() {
        return units != 0;
    }



    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public boolean isVisibleUnits() {
        return visibleUnits;
    }

    public void setVisibleUnits(boolean visibleUnits) {
        this.visibleUnits = visibleUnits;
    }
}
