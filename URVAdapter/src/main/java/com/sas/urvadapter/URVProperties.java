package com.sas.urvadapter;

public class URVProperties {

    private boolean autoHideEmpty = true;
    private boolean titleVisible = true;
    private boolean descrVisible = true;

    private String textOK = "OK";
    private String textCancel = "Cancel";
    private String textSelect = "Select";

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



    public String getTextOK() {
        return textOK;
    }

    public void setTextOK(String textOK) {
        this.textOK = textOK;
    }

    public String getTextCancel() {
        return textCancel;
    }

    public void setTextCancel(String textCancel) {
        this.textCancel = textCancel;
    }

    public String getTextSelect() {
        return textSelect;
    }

    public void setTextSelect(String textSelect) {
        this.textSelect = textSelect;
    }
}
