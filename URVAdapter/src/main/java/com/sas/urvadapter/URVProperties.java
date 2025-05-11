package com.sas.urvadapter;

public class URVProperties {

    private boolean autoHideEmpty = true;
    private boolean titleVisible = true;
    private boolean descrVisible = true;


    public boolean isTitleVisible() {
        return titleVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    public boolean isDescrVisible() {
        return descrVisible;
    }

    public void setDescrVisible(boolean descrVisible) {
        this.descrVisible = descrVisible;
    }

    public boolean isAutoHideEmpty() {
        return autoHideEmpty;
    }

    public void setAutoHideEmpty(boolean autoHideEmpty) {
        this.autoHideEmpty = autoHideEmpty;
    }
}
