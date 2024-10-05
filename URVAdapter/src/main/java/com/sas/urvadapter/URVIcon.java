package com.sas.urvadapter;

import android.graphics.Bitmap;

public class URVIcon {

    public static final int ICON_TYPE_NONE = 0;
    public static final int ICON_TYPE_BITMAP = 1;
    public static final int ICON_TYPE_TEXT = 2;


    private int iconType = ICON_TYPE_NONE;
    private boolean visible = false;
    private String iconText = null;
    private Bitmap iconBitmap = null;


    /**
     * Constructor
     */
    public URVIcon() {
        setIconNone();
    }

    public boolean isIconTypeNone() {
        return iconType == ICON_TYPE_NONE;
    }

    public boolean isIconTypeText() {
        return iconType == ICON_TYPE_TEXT;
    }

    public boolean isIconTypeBitmap() {
        return iconType == ICON_TYPE_BITMAP;
    }



    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }



    public String getIconText() {
        return iconText;
    }

    public void setIconText(String iconText) {
        this.iconType = ICON_TYPE_TEXT;
        this.iconText = iconText;
    }




    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconType = ICON_TYPE_BITMAP;
        this.iconBitmap = iconBitmap;
    }


    public void setIconNone() {
        iconType = ICON_TYPE_NONE;
        iconText = "";
        // mute bitmap here
        iconBitmap = null;
    }


    public int getIconType() {
        return iconType;
    }
}
