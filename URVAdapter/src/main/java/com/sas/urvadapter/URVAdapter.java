package com.sas.urvadapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Date: 2024.09.25
 * Author: Den Vigovski
 * Universal RecycleView adapter
 * Licence: This is an experimental project for educational purposes. Not for commercial use.
 */


public class URVAdapter extends RecyclerView.Adapter<URVAdapter.URViewHolder> {

    public static final int VERSION = 1;
    public final String LOGTAG = "URVAdapter";

    public final URVProperties Properties; // visible for title, descr, auto-hide
    public ArrayList<URVItem> items;
    public ArrayList<URVItem> filterSrcItems;

    public IURVItemEvents eventsItem = null;
    public IURVTechEvents eventsTech = null;
    public IURVTerminalEvents eventsTerminal = null;
    public IURVPropertyEvents eventProperty = null;
    //public IURVColorPicker eventsOnColorPicker = null;

    public URVResources ResourceItems;  // resources id00 .. id09
    public URVCounterResources ResourceCounter; // enabled, id: box, counter, unit

    public final ArrayList<URVItemClicker> itemsClicker;
    public final ArrayList<URVItemElement> itemsElements;


    public static final int[] COLORS_BCK = {
            0xFFB71C1C, 0xFF880E4F, 0xFF4A148C, 0xFF311B92, 0xFF1A237E,
            0xFF0D47A1, 0xFF01579B, 0xFF006064, 0xFF004D40, 0xFF1B5E20,
            0xFF33691E, 0xFF827717, 0xFFF57F17, 0xFFFF6F00, 0xFFE65100,
            0xFFBF360C, 0xFF3E2723, 0xFF212121, 0xFF263238
    };


    private int resItemPanelBck = 0;
    private int resItemColorMarker = 0;

    private int resItemImgBck = 0;
    private int resItemImgBitmap = 0;
    private int resItemImgLabel = 0;

    private RecyclerView rView = null;

    private final int itemPanelBackground = 0;

    private boolean textIcons = false;

    private Typeface iconFont = null;

    private boolean multiselect = false;

    private final String defaultTextIcon = "✓";
    private int colorSelected = Color.argb(40, 0, 0, 255);
    private int colorNormal = Color.TRANSPARENT;
    private int defaultItemBackgroundColor = 0xFF202020;



    // Messenger fields ...................................
    public EditText edit = null;
    public TextView lblBase = null;
    public View btnSend = null;

    private String terminalBase = "";
    private final String baseSymbol = "/";

    private int colorBckChecked = 0;
    private int colorBckUnchecked = 0;

    private String iconCheckboxChecked = "L";
    private String iconCheckboxUnchecked = "M";
    private boolean filtered = false;

    private boolean gridMode = false;
    private int gridRows = 0;
    private int gridColumns = 0;

    private int cornerRadius = 6;
    private boolean debug = false;


    /**
     * Constructor
     */
    public URVAdapter() {
        items = new ArrayList<URVItem>();
        filterSrcItems = new ArrayList<URVItem>();
        itemsClicker = new ArrayList<URVItemClicker>();
        itemsElements = new ArrayList<URVItemElement>();

        ResourceItems = new URVResources();
        ResourceCounter = new URVCounterResources();

        Properties = new URVProperties();
    }




    // new functions
    public void initList001() {
        ResourceItems.setId00(R.layout.urv_list_item_001);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

    }

    public void initList002() {
        ResourceItems.setId00(R.layout.urv_list_item_002);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lblCounter, URVConst.Logic.COUNTER_VALUE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lblCounterUnits, URVConst.Logic.COUNTER_UNITS, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
    }


    public void initList003() {
        ResourceItems.setId00(R.layout.urv_list_item_003);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
    }


    public void initList004() {
        ResourceItems.setId00(R.layout.urv_list_item_004);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.lbl11, URVConst.Logic.COL1_VALUE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lbl12, URVConst.Logic.COL1_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lbl21, URVConst.Logic.COL2_VALUE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lbl22, URVConst.Logic.COL2_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.textIconA, URVConst.Logic.ICON_A, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // flash icon
        addItemElement(R.id.textIconB, URVConst.Logic.ICON_B, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // delete icon
        addItemElement(R.id.textIconC, URVConst.Logic.ICON_C, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // delete icon

        addClickItem(R.id.col1, 1, URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.col2, 2, URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.col3, 3, URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.col4, 4, URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.col5, 5, URVConst.ClickerAction.CUSTOM);
    }

    public void initList005(boolean useInternalActions) {
        ResourceItems.setId00(R.layout.urv_list_item_005);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.btnL1, URVConst.Logic.BUTTON1_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL2, URVConst.Logic.BUTTON2_LABEL, URVConst.ElementType.TEXT_LABEL, useInternalActions ? URVConst.LabelAction.VALUE_INT : URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL3, URVConst.Logic.BUTTON3_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addClickItem(R.id.btn1, 1, useInternalActions ? URVConst.ClickerAction.VALUE_DECREASE : URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.btn2, 2, URVConst.ClickerAction.CUSTOM); // label - non clickable
        addClickItem(R.id.btn3, 3, useInternalActions ? URVConst.ClickerAction.VALUE_INCREASE : URVConst.ClickerAction.CUSTOM);
    }



    public void initListMixed(boolean useInternalActions) {
        ResourceItems.setId00(R.layout.urv_list_item_001);
        ResourceItems.setId01(R.layout.urv_list_item_002);
        ResourceItems.setId02(R.layout.urv_list_item_003);
        ResourceItems.setId03(R.layout.urv_list_item_004);
        ResourceItems.setId04(R.layout.urv_list_item_005);


        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.btnL1, URVConst.Logic.BUTTON1_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL2, URVConst.Logic.BUTTON2_LABEL, URVConst.ElementType.TEXT_LABEL, useInternalActions ? URVConst.LabelAction.VALUE_INT : URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL3, URVConst.Logic.BUTTON3_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addClickItem(R.id.btn1, 1, useInternalActions ? URVConst.ClickerAction.VALUE_DECREASE : URVConst.ClickerAction.CUSTOM);
        //addClickItem(R.id.btn2, 2, URVConst.ClickerAction.CUSTOM); // label - non clickable
        addClickItem(R.id.btn3, 3, useInternalActions ? URVConst.ClickerAction.VALUE_INCREASE : URVConst.ClickerAction.CUSTOM);

        addItemElement(R.id.lblCounter, URVConst.Logic.COUNTER_VALUE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lblCounterUnits, URVConst.Logic.COUNTER_UNITS, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.textIconA, URVConst.Logic.ICON_A, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // flash icon
        addItemElement(R.id.textIconB, URVConst.Logic.ICON_B, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // delete icon
        addItemElement(R.id.textIconC, URVConst.Logic.ICON_C, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM); // delete icon
    }



    public void initListProperties(boolean useInternalActions) {
        ResourceItems.setId00(R.layout.urv_list_item_001);
        ResourceItems.setId01(R.layout.urv_list_item_002);
        ResourceItems.setId02(R.layout.urv_list_item_003);
        ResourceItems.setId03(R.layout.urv_list_item_004);
        ResourceItems.setId04(R.layout.urv_list_item_005);
        ResourceItems.setId05(R.layout.urv_list_item_btn2);
        ResourceItems.setId06(R.layout.urv_list_item_btn3);
        ResourceItems.setId07(R.layout.urv_list_item_btn4);

        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.btnL1, URVConst.Logic.BUTTON1_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL2, URVConst.Logic.BUTTON2_LABEL, URVConst.ElementType.TEXT_LABEL, useInternalActions ? URVConst.LabelAction.VALUE_INT : URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.btnL3, URVConst.Logic.BUTTON3_LABEL, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addClickItem(R.id.btn1, URVConst.ClickerID.BUTTON_DEC, useInternalActions ? URVConst.ClickerAction.VALUE_DECREASE : URVConst.ClickerAction.CUSTOM);
        addClickItem(R.id.btn2, URVConst.ClickerID.BUTTON_INPUT_NUMBER, useInternalActions ? URVConst.ClickerAction.SHOW_INPUT_NUMBER : URVConst.ClickerAction.CUSTOM); // label - non clickable
        addClickItem(R.id.btn3, URVConst.ClickerID.BUTTON_INC, useInternalActions ? URVConst.ClickerAction.VALUE_INCREASE : URVConst.ClickerAction.CUSTOM);

        addItemElement(R.id.lblCounter, URVConst.Logic.COUNTER_VALUE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.lblCounterUnits, URVConst.Logic.COUNTER_UNITS, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);

        addItemElement(R.id.textIconA, URVConst.Logic.ICON_A, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.textIconB, URVConst.Logic.ICON_B, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.textIconC, URVConst.Logic.ICON_C, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);


        addClickItem(R.id.ib1, URVConst.ClickerID.BUTTON_1, URVConst.ClickerAction.BUTTON_1);
        addClickItem(R.id.ib2, URVConst.ClickerID.BUTTON_2, URVConst.ClickerAction.BUTTON_2);
        addClickItem(R.id.ib3, URVConst.ClickerID.BUTTON_3, URVConst.ClickerAction.BUTTON_3);
        addClickItem(R.id.ib4, URVConst.ClickerID.BUTTON_4, URVConst.ClickerAction.BUTTON_4);


        addItemElement(R.id.il1, URVConst.Logic.ICON_A, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.il2, URVConst.Logic.ICON_B, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.il3, URVConst.Logic.ICON_C, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.il4, URVConst.Logic.ICON_D, URVConst.ElementType.TEXT_ICON, URVConst.LabelAction.CUSTOM);
    }




    public void addItemElement(int idResource, int id, int idType, int act) {
        URVItemElement el = new URVItemElement(idResource, id, idType, act);
        itemsElements.add(el);
    }


    private String getLogicText(int logic, URVItem item) {
        switch (logic) {
            case URVConst.Logic.TITLE: return item.getTitle();
            case URVConst.Logic.DESCR: return item.getDescription();
            case URVConst.Logic.COUNTER_VALUE: return item.Counter.getCounter();
            case URVConst.Logic.COUNTER_UNITS: return item.Counter.getUnits();

            case URVConst.Logic.COL1_LABEL: return item.getColumn1L();
            case URVConst.Logic.COL1_VALUE: return item.getColumn1V();

            case URVConst.Logic.COL2_LABEL: return item.getColumn2L();
            case URVConst.Logic.COL2_VALUE: return item.getColumn2V();

            case URVConst.Logic.COL3_LABEL: return item.getColumn3L();
            case URVConst.Logic.COL3_VALUE: return item.getColumn3V();

            case URVConst.Logic.COL4_LABEL: return item.getColumn4L();
            case URVConst.Logic.COL4_VALUE: return item.getColumn4V();

            case URVConst.Logic.ICON_A: return item.getTextIconA();
            case URVConst.Logic.ICON_B: return item.getTextIconB();
            case URVConst.Logic.ICON_C: return item.getTextIconC();
            case URVConst.Logic.ICON_D: return item.getTextIconD();

            case URVConst.Logic.BUTTON1_LABEL: return item.getButton1Label();
            case URVConst.Logic.BUTTON2_LABEL: return item.getButton2Label();
            case URVConst.Logic.BUTTON3_LABEL: return item.getButton3Label();
            case URVConst.Logic.BUTTON4_LABEL: return item.getButton4Label();

            default: return "";
        }
    }
    // new functions


    private void setLogicText(int logic, URVItem item, String text) {
        if(debug) Log.d(LOGTAG, String.format("setLogicText logic: %d, i-index: %d, txt: %s", logic, item.getIndex(), text));

        switch (logic) {
            case URVConst.Logic.TITLE: item.setTitle(text); break;
            case URVConst.Logic.DESCR: item.setDescription(text); break;
            case URVConst.Logic.COUNTER_VALUE: item.Counter.setCounter(text); break;
            case URVConst.Logic.COUNTER_UNITS: item.Counter.setUnits(text); break;

            case URVConst.Logic.COL1_LABEL: item.setColumn1L(text); break;
            case URVConst.Logic.COL1_VALUE: item.setColumn1V(text); break;

            case URVConst.Logic.COL2_LABEL: item.setColumn2L(text); break;
            case URVConst.Logic.COL2_VALUE: item.setColumn2V(text); break;

            case URVConst.Logic.COL3_LABEL: item.setColumn3L(text); break;
            case URVConst.Logic.COL3_VALUE: item.setColumn3V(text); break;

            case URVConst.Logic.COL4_LABEL: item.setColumn4L(text); break;
            case URVConst.Logic.COL4_VALUE: item.setColumn4V(text); break;

            case URVConst.Logic.ICON_A: item.setTextIconA(text); break;
            case URVConst.Logic.ICON_B: item.setTextIconB(text); break;
            case URVConst.Logic.ICON_C:  item.setTextIconC(text); break;
            case URVConst.Logic.ICON_D:  item.setTextIconD(text); break;

            case URVConst.Logic.BUTTON1_LABEL: item.setButton1Label(text); break;
            case URVConst.Logic.BUTTON2_LABEL: item.setButton2Label(text); break;
            case URVConst.Logic.BUTTON3_LABEL: item.setButton3Label(text); break;
            case URVConst.Logic.BUTTON4_LABEL: item.setButton4Label(text); break;
        }
    }


    public URVItemClicker getClicker(int idClicker) {
        for(URVItemClicker clicker : itemsClicker) {
          if(clicker.getId() == idClicker) return clicker;
        }
        return null;
    }

    public void initDefaultListParams(boolean sMultiselect, Typeface fontIcon) {
        setMultiselect(sMultiselect);

        if(fontIcon != null) {
            setIconFont(fontIcon);
            setTextIcons(true);
        } else {
            setIconFont(null);
            setTextIcons(false);
        }

        setColorSelected(Color.rgb(00, 85, 255));
        setupDefaultCheckbox("L", "M");
        Properties.setAutoHideEmpty(true);
    }





    public int getDefaultLayoutListItem() {
        return gridMode ? R.layout.urv_grid_item : R.layout.urv_list_item;
    }


    public void initDefaultResources() {
        setupResourceHolders(R.id.msgbox, R.id.markerColor);
        setupResourceImage(R.id.imgBck, R.id.imgBitmap, R.id.imgLbl);
        setupResourceItems(getDefaultLayoutListItem(), 0, 0, 0, 0);
        ResourceCounter.setup(R.id.pnlCounter, R.id.lblCounter, R.id.lblCounterUnits);
    }


    public URVItem addItem(int id, String title, String descr) {
        URVItem newItem = new URVItem(id, 0, title, descr, null);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addItem(int id, int viewType, String title, String descr, URVAbstractCustomData customData) {
        URVItem newItem = new URVItem(id, viewType, title, descr, customData);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addItemDefIcon(int id, int viewType, String txtIcon, String title, String descr, int act, int logic, int group, int marker, URVAbstractCustomData customData) {
        URVItem newItem = new URVItem(id, viewType, title, descr, customData);
        newItem.Icon.setIconText(txtIcon);
        newItem.setAction(act);
        newItem.setLogic(logic);
        newItem.setGroup(group);
        newItem.setMarker(marker);

        addItemRaw(newItem);
        return newItem;
    }


    public URVItem addPropertyCheckbox(int id, String title, String descr, String key, boolean checked) {
        URVItem newItem = new URVItem(id, 0, title, descr, null);
        newItem.setItemMode(URVConst.ItemMode.CHECKBOX);
        newItem.Value.setup(key, checked);
        newItem.setChecked(checked);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addPropertyText(int id, String descr, String key, String value, String txtIcon) {
        URVItem newItem = new URVItem(id, 0, value, descr, null);
        newItem.Icon.setIconText(txtIcon);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        newItem.Value.setup(key, (String) value);
        newItem.setClickAction(URVConst.ClickerAction.SHOW_INPUT_TEXT);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addPropertyList(int id, String descr, String key, String[] listValues, int selectedIndex, String txtIcon) {
        URVItem newItem = new URVItem(id, 0, listValues[selectedIndex], descr + getPreviewString(listValues, 3, true, true), null);
        newItem.Icon.setIconText(txtIcon);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        String combined = TextUtils.join(";", listValues);
        newItem.setValueString(combined);
        newItem.Value.setup(key, (String) listValues[selectedIndex]);
        newItem.Value.setValue(selectedIndex);
        newItem.setClickAction(URVConst.ClickerAction.SHOW_SELECT_ONE);
        addItemRaw(newItem);
        return newItem;
    }


    public URVItem addPropertyList(int id, String descr, String key, String[] listValues, String[] icons, int selectedIndex) {
        URVItem newItem = new URVItem(id, 0, listValues[selectedIndex], descr + getPreviewString(listValues, 3, true, true), null);
        newItem.Icon.setIconText(icons[selectedIndex]);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        String combined = TextUtils.join(";", listValues);

        newItem.setValueString(combined);
        newItem.attrSet("icons", TextUtils.join(";", icons));

        newItem.Value.setup(key, (String) listValues[selectedIndex]);
        newItem.Value.setValue(selectedIndex);
        newItem.setClickAction(URVConst.ClickerAction.SHOW_SELECT_ONE);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addPropertyColorExt(int id, String title, String descr, String key, int defColor, String icon) {
        URVItem newItem = new URVItem(id, 2, title, descr, null);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        newItem.setClickAction(URVConst.ClickerAction.SHOW_COLOR_PICKER);
        newItem.setMarkerColor(defColor);
        newItem.Value.setup(key, defColor);
        newItem.setValueInt(defColor);
        newItem.Icon.setIconText(icon);
        addItemRaw(newItem);
        return newItem;
    }


    public URVItem addPropertyNumber(int id, String title, String descr, String key, int value, int min, int max, int changeStep, String txtIcon) {
        URVItem newItem = new URVItem(id, 4, title, descr, null);
        newItem.Icon.setIconText(txtIcon);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        newItem.Value.setup(key, value);
        if(min != max) newItem.Value.setupMinMax(min, max);
        if(changeStep != 0) newItem.setValueChangeStep(changeStep);
        newItem.initEditorPlusMinus();
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addPropertyNumberF(int id, String title, String descr, String key, float value, float min, float max, float changeStep, String txtIcon) {
        URVItem newItem = new URVItem(id, 4, title, descr, null);
        newItem.Icon.setIconText(txtIcon);
        newItem.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
        newItem.Value.setup(key, value);
        if(min != max) newItem.Value.setupMinMax(min, max);
        if(changeStep != 0) newItem.setValueChangeStep(changeStep);
        newItem.initEditorPlusMinus();
        addItemRaw(newItem);
        return newItem;
    }




    private void addItemRaw(URVItem item) {
        item.setIndex(items.size());
        item.setCustomBackgroundColor(defaultItemBackgroundColor);
        items.add(item);
    }


    public void initRecyclerView(Context ctx, RecyclerView rList, boolean useDefResources, int columns) {
        rView = rList;
        gridColumns = columns;
        gridRows = 0;

        if(columns > 1) {
            gridMode = true;
            rView.setLayoutManager(new URVGridLayoutManager(ctx, columns));
        } else {
            gridMode = false;
            rView.setLayoutManager(new LinearLayoutManager(ctx));
        }

        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setAdapter(this);

        if(useDefResources) initDefaultResources();
    }


    public void setupGridRowsCols(Context ctx, int gRows, int gCols) {
        if(gCols > 1) {
            gridMode = true;
            rView.setLayoutManager(new URVGridLayoutManager(ctx, gCols));
            gridColumns = gCols;
        } else {
            gridMode = false;
            rView.setLayoutManager(new LinearLayoutManager(ctx));
        }
        gridRows = gRows;

        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL, URVConst.LabelAction.CUSTOM);
    }



    @Override
    public void onBindViewHolder(@NonNull URViewHolder holder, int position) {
        URVItem data = items.get(position);
        holder.updateSelection(data.isSelected());

        switch (data.getItemMode()) {

            case URVConst.ItemMode.CHECKBOX:
                if(data.isChecked()) {
                    data.setCustomBackgroundColor(colorBckChecked);
                    data.Icon.setIconText(iconCheckboxChecked);
                } else {
                    data.setCustomBackgroundColor(colorBckUnchecked);
                    data.Icon.setIconText(iconCheckboxUnchecked);
                }
                break;

            default:
                break;
        }

        for(TextView tvi : holder.holderTextViews) {
           String txt = getLogicText((int) tvi.getTag(), data);
           if(Properties.isAutoHideEmpty()) {
              tvi.setVisibility(TextUtils.isEmpty(txt) ? View.GONE : View.VISIBLE);
           }
           tvi.setText(txt);
        }


        switch (data.Icon.getIconType()) {

            case URVIcon.ICON_TYPE_TEXT:
                holder.setImageTextIcon(data.Icon.getIconText());
                break;

            case URVIcon.ICON_TYPE_BITMAP:
                holder.setImageBitmap(data.Icon.getIconBitmap());
                break;

            default:
                holder.setImageNone();
                break;
        }

        if (holder.getPanelBck() != null) {
            setViewBackgroundColor(holder.getPanelBck(), dpToPx(cornerRadius),
                    data.getCustomBackgroundColor() == 0 ? Color.TRANSPARENT : data.getCustomBackgroundColor());
        }

        if (holder.getColorMarker() != null) {
            setViewBackgroundColor(holder.getColorMarker(), dpToPx(cornerRadius),
                    data.getMarkerColor() == 0 ? Color.TRANSPARENT : data.getMarkerColor());
        }
    }


    @NonNull
    @Override
    public URViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getIdResourceByType(viewType), parent, false);

        if(isTextIcons() & (resItemImgLabel != 0)) {
            TextView tIcon = v.findViewById(resItemImgLabel);
            if(tIcon != null) tIcon.setTypeface(iconFont);
        }

        if(gridMode && (gridRows > 0)) {
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
            lp.height = parent.getMeasuredHeight() / gridRows;
            v.setLayoutParams(lp);
        }

        return new URViewHolder(v);
    }

    private void onItemLongClick(final int index) {
        if(debug) Log.d(LOGTAG, String.format("onItemLongClick index: %d", index));
        if (eventsItem != null) {
            eventsItem.onLongClick(index);
        }
    }


    // эта функция вызывается если item весь кликательный
    // без внутренних кликателей
    private void onItemClick(int index) {
        if(debug) Log.d(LOGTAG, String.format("onItemClick index: %d", index));
        final URVItem item = items.get(index);

        switch (item.getItemMode()) {
            case URVConst.ItemMode.CHECKBOX:
                onCheckBoxClick(index);
                return;
        }

        switch (item.getClickAction()) {
            case URVConst.ClickerAction.SHOW_INPUT_TEXT: onClickPropertyText(index);
            break;

            case URVConst.ClickerAction.SHOW_SELECT_ONE: onClickPropertyList(index);
            break;

            case URVConst.ClickerAction.SHOW_COLOR_PICKER:
                onClickPropertyColor(index);
                return;

            default:
              if(eventsItem != null) eventsItem.onItemClick(index);
            break;
        }
    }


    // эта функция вызывается для item если несколько кликательных элементов
    // например 2 - 3 доп кнопки
    private void onItemClickEx(int itemIndex, int idClicker, boolean longCLick) {
        int clickAction = URVConst.ClickerAction.CUSTOM;

        URVItem item = items.get(itemIndex);
        if(item == null) return;

        if(debug) Log.d(LOGTAG, String.format("onItemClickEx itemIndex: %d, id: %d, i-mode: %d", itemIndex, idClicker, item.getItemMode()));

        switch (item.getItemMode()) {

            case URVConst.ItemMode.PROPERTY_KEY_VALUE:
                onItemPropertyClick(itemIndex, idClicker, longCLick);
                return;

        }

        if (eventsItem != null) eventsItem.onClickEx(itemIndex, idClicker);
        if(debug) showDebugReport(true, true, true);
    }



    private int currentClickAction = 0;

    private void onItemPropertyClick(final int itemIndex, final int idClicker, final boolean longCLick) {
        final URVItem currentItem = items.get(itemIndex);
        currentClickAction = URVConst.ClickerAction.CUSTOM;

        if(currentItem.isClickerValueExists(idClicker)) {
            currentItem.Value.setValue(currentItem.getClickerValue(idClicker));
        } else {
            URVItemClicker clickItem = getClicker(idClicker);
            if(clickItem != null) {
                currentClickAction = clickItem.getAction();

                if(clickItem.isValueExists()) {
                    currentItem.Value.setValue(clickItem.getValueBool());
                    currentItem.Value.setValue(clickItem.getValueStr());
                    currentItem.Value.setValue(clickItem.getValueInt());
                    currentItem.Value.setValue(clickItem.getValueFloat());
                }
            }
        }

        //if(clickAction != URVConst.ClickerAction.CUSTOM) launchCLickAction(itemIndex, clickAction, idClicker, longCLick);
        //if(eventProperty != null) eventProperty.onPropertyChanges(itemIndex, clickAction, idClicker, item.Value);

        float step = currentItem.getValueChangeStep() * (longCLick ? 10 : 1);
        switch (currentClickAction) {

            case URVConst.ClickerAction.VALUE_DECREASE:
                currentItem.Value.dec(step);
                updateItemLabelAction(itemIndex);
                break;

            case URVConst.ClickerAction.VALUE_INCREASE:
                currentItem.Value.inc(step);
                updateItemLabelAction(itemIndex);
                break;

            case URVConst.ClickerAction.SHOW_INPUT_NUMBER:

                URVNumberDialogConfig nidCfg = new URVNumberDialogConfig();
                nidCfg.minValue = currentItem.Value.getMin();
                nidCfg.maxValue = currentItem.Value.getMax();
                nidCfg.useFloatNumber = currentItem.Value.isFloat();
                nidCfg.useNegative = true;

                if(currentItem.Value.isFloat()) nidCfg.defaultValue = currentItem.Value.getValueFloat();
                if(currentItem.Value.isInt()) nidCfg.defaultValue = currentItem.Value.getValueInt();

                URVNumberInputDialog nid = new URVNumberInputDialog(rView.getContext(), nidCfg, new URVNumberInputDialog.INumberInput() {
                    @Override
                    public void onNumberSet(float number) {
                        currentItem.setValueFloat(number);
                        currentItem.setValueInt(Math.round(number));

                        currentItem.Value.setValue(number);
                        currentItem.Value.setValue(Math.round(number));

                        updateItemLabelAction(itemIndex);
                        Toast.makeText(rView.getContext(), "Number: " + number, Toast.LENGTH_SHORT).show();
                        if(eventProperty != null) eventProperty.onPropertyChanges(itemIndex, currentClickAction, idClicker, currentItem.Value);
                    }
                });
                nid.show();
                break;

            default:
                break;
        }

        switch (currentClickAction) {

            case URVConst.ClickerAction.SHOW_INPUT_NUMBER:
                // skip property change event
                break;

            default:
                if(eventProperty != null) eventProperty.onPropertyChanges(itemIndex, currentClickAction, idClicker, currentItem.Value);
                break;
        }
    }


    public void updateItemLabelAction(final int index) {
        URVItem item = items.get(index);
        if(item == null) return;

        for (URVItemElement element : itemsElements) {
            switch (element.getAction()) {

                case URVConst.LabelAction.VALUE_INT:
                    setLogicText(element.getLogic(), item, item.Value.asString());
                    break;

                case URVConst.LabelAction.VALUE_FLOAT:
                    setLogicText(element.getLogic(), item, item.Value.asString());
                    break;
            }
        }

        notifyItemChanged(index);
    }


    public void updateItemsLabelActions() {
        for(int i=0; i < items.size(); i++) {
            updateItemLabelAction(i);
        }
    }

    private void onCheckBoxClick(int itemIndex) {
        final URVItem currentItem = items.get(itemIndex);
        currentClickAction = URVConst.ClickerAction.CUSTOM;

        currentItem.setChecked(!currentItem.isChecked());
        currentItem.Value.setValue(currentItem.isChecked());


        if(currentItem.isChecked()) {
            currentItem.setCustomBackgroundColor(colorBckChecked);
            currentItem.Icon.setIconText(iconCheckboxChecked);
        } else {
            currentItem.setCustomBackgroundColor(colorBckUnchecked);
            currentItem.Icon.setIconText(iconCheckboxUnchecked);
        }

        notifyItemChanged(itemIndex);

        if(eventProperty != null) eventProperty.onPropertyChanges(itemIndex, -1, -1, currentItem.Value);
    }


    private void onClickPropertyText(final int index) {
        final URVItem currentItem = items.get(index);

        if (!(rView.getContext() instanceof Activity))
           Toast.makeText(rView.getContext(), "Need to use Activity context for RVU properties", Toast.LENGTH_SHORT).show();

        URVTextInputDialog.show(rView.getContext(),
                currentItem.getDescription(),
                currentItem.getHint(),
                currentItem.Value.getValueStr(),
                Properties.getTextOK(),
                Properties.getTextCancel(),
                text -> {
                    // Обработка введённого текста
                    currentItem.setTitle(text);
                    currentItem.Value.setValue(text);
                    notifyItemChanged(index);
                    if(eventProperty != null) eventProperty.onPropertyChanges(index, -1, -1, currentItem.Value);
                    //Toast.makeText(rView.getContext(), "Вы ввели: " + text, Toast.LENGTH_SHORT).show();
                });
    }


    private void onClickPropertyList(final int index) {
        final URVItem currentItem = items.get(index);

        if (!(rView.getContext() instanceof Activity))
            Toast.makeText(rView.getContext(), "Need to use Activity context for RVU properties", Toast.LENGTH_SHORT).show();

        // Вариант с подтверждением через OK
        URVSingleChoiceDialog.showWithOk(rView.getContext(),
                currentItem.getDescription(),
                currentItem.getValueString().split(";"),
                currentItem.Value.getValueInt(),
                Properties.getTextOK(),
                Properties.getTextCancel(),
                new URVSingleChoiceDialog.IOnSelectedOneListener() {
                    @Override
                    public void onItemSelected(int which, CharSequence item) {

                        try {
                            if (currentItem.attrExists("icons")) {
                                String icons = currentItem.attrGet("icons");
                                String arrIcons[] = icons.split(";");
                                currentItem.Icon.setIconText(arrIcons[which]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        currentItem.setTitle(item.toString());
                        currentItem.Value.setValue(item.toString());
                        currentItem.Value.setValue(which);
                        currentItem.setValueInt(which);

                        notifyItemChanged(index);
                        if(eventProperty != null) eventProperty.onPropertyChanges(index, -1, -1, currentItem.Value);
                    }
                });
    }



    public IURVRequestActivity eventRequests = null;

    private void onClickPropertyColor(int index) {
        final URVItem currentItem = items.get(index);

        AppCompatActivity aContext = null;
        if (rView.getContext() instanceof AppCompatActivity) aContext = (AppCompatActivity) rView.getContext();
        if((aContext == null) && (eventRequests != null) ) aContext = eventRequests.needActivity();

        if(aContext == null) {
            Toast.makeText(rView.getContext(), "Need to use AppCompatActivity for context", Toast.LENGTH_SHORT).show();
            return;
        }

        URVColorPickerFS.show(aContext.getSupportFragmentManager(),
                currentItem.getValueInt(),
                currentItem.isUseAlpha(),
                Properties.getTextOK(),
                Properties.getTextCancel(),
                Properties.getTextSelect(),
                new URVColorPickerFS.IOnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        currentItem.setValueInt(color);
                        currentItem.setMarkerColor(color);
                        currentItem.Value.setValue(color);

                        notifyItemChanged(index);
                        if(eventProperty != null) eventProperty.onPropertyChanges(index, -1, -1, currentItem.Value);
                    }
                });
    }


    private void onSelectItem(final int index) {
        if(isMultiselect()) {
            boolean allowSelect = true;

            if(eventsTech != null) {
                allowSelect = eventsTech.onAllowSelect(index);
            }

            URVItem item = items.get(index);
            if(allowSelect) {
                item.setSelected(!item.isSelected());
            } else {
                item.setSelected(false);
            }

            notifyItemChanged(index);

            if(eventsTech != null) {
                eventsTech.onSelectionChanged();
            }
        }
    }


    private int getIdResourceByType(int viewType) {
        switch (viewType) {
            case 1: return ResourceItems.getId01();
            case 2: return ResourceItems.getId02();
            case 3: return ResourceItems.getId03();
            case 4: return ResourceItems.getId04();
            case 5: return ResourceItems.getId05();
            case 6: return ResourceItems.getId06();
            case 7: return ResourceItems.getId07();
            case 8: return ResourceItems.getId08();
            case 9: return ResourceItems.getId09();
            default:
                return ResourceItems.getId00();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public URVItem getItem(int index) {
        if(index < items.size()) {
            return items.get(index);
        } else
            return null;
    }


    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }


    public int findIndexByUID(final String uid) {
        if(TextUtils.isEmpty(uid)) {
            return -1;
        }
        for(URVItem item : items) {
            if(uid.equals(item.getUid())) {
                return item.getIndex();
            }
        }
        return -1;
    }


    public void setupResourceHolders(int idBck, int idColorMarker) {
        resItemPanelBck = idBck;
        resItemColorMarker = idColorMarker;
    }

    public void setupResourceImage(int idImgBck, int idImgBitmap, int idImgLabel) {
        this.resItemImgBck = idImgBck;
        this.resItemImgBitmap = idImgBitmap;
        this.resItemImgLabel = idImgLabel;
    }


    public void setupResourceItems(int id0, int id1, int id2, int id3, int id4) {
        ResourceItems.setId00(id0);
        ResourceItems.setId01(id1);
        ResourceItems.setId02(id2);
        ResourceItems.setId03(id3);
        ResourceItems.setId04(id4);
    }




    public int getSelectedCount() {
        int selCount = 0;
        for(URVItem item : items) {
            if(item.isSelected()) {
                selCount++;
            }
        }
        return selCount;
    }

    public ArrayList<URVItem> getSelectedItems() {
        ArrayList<URVItem> selItems = new ArrayList<URVItem>();
        for(URVItem item : items) {
            if(item.isSelected()) {
                selItems.add(item);
            }
        }

        return selItems;
    }

    public void setupCheckbox(int clrChecked, String iconChecked, int clrUnchecked, String iconUnchecked) {
        this.colorBckChecked = clrChecked;
        this.colorBckUnchecked = clrUnchecked;
        this.iconCheckboxChecked = iconChecked;
        this.iconCheckboxUnchecked = iconUnchecked;
    }

    public Typeface getIconFont() {
        return iconFont;
    }

    public void setIconFont(Typeface iconFont) {
        this.iconFont = iconFont;
    }

    public boolean isMultiselect() {
        return multiselect;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public boolean isTextIcons() {
        return textIcons;
    }

    public void setTextIcons(boolean textIcons) {
        this.textIcons = textIcons;
    }

    public int getColorSelected() {
        return colorSelected;
    }

    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
    }

    public int getColorNormal() {
        return colorNormal;
    }

    public void setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;
    }

    public void scrollListToBottom() {
        if(rView != null) {
            rView.scrollToPosition(getItemCount() - 1);
        }
    }

    public int getDefaultItemBackgroundColor() {
        return defaultItemBackgroundColor;
    }

    public void setDefaultItemBackgroundColor(int defaultItemBackgroundColor) {
        this.defaultItemBackgroundColor = defaultItemBackgroundColor;
    }




    // ......................... MESSAGER .......................................
    // Init simple messager interface: edit and send button
    public void initTerminal(EditText edText, View sendButton, TextView lblBase) {
        this.edit = edText;
        this.lblBase = lblBase;
        lblBase.setText(baseSymbol);

        edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(edit == null) {
                    return false;
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    retranslateUserMessage();
                    return true;
                }

                return false;
            }
        });

        if(sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retranslateUserMessage();
                }
            });
        }
    }


    private void retranslateUserMessage() {
        if(edit == null) {
            return;
        }

        final String msg = edit.getText().toString();
        edit.setText("");

        if(!TextUtils.isEmpty(msg)) {
            executeTextCommand(msg, true, true);
        }
    }


    public void executeTextCommand(String msg, boolean useBase, boolean showCmd) {
        if(TextUtils.isEmpty(msg)) {
            return;
        }

        if(showCmd) {
            addItem(0, 0, msg, "", null);
            notifyItemChanged(getItemCount() - 1);
            scrollListToBottom();
        }

        if(eventsTerminal != null) {
            eventsTerminal.onCommand(getTerminalBase(), msg);
        }
    }

    public String getTerminalBase() {
        return terminalBase;
    }

    public void setTerminalBase(String terminalBase) {
        this.terminalBase = terminalBase;
        lblBase.setText(this.terminalBase + baseSymbol);
    }


    public int getLibColor(int index) {
        return COLORS_BCK[index];
    }



    public boolean isFiltered() {
        return filtered;
    }


    public void setFiltered(boolean useFilter, boolean useNotify) {
        if(!useFilter) {
            this.filtered = useFilter;
            items.clear();
            items.addAll(filterSrcItems);
            filterSrcItems.clear();
            if(useNotify) { notifyDataSetChanged(); }
            return;
        }

        if(!this.filtered) {
            filterSrcItems.clear();
            filterSrcItems.addAll(items);
            items.clear();
            if(useNotify) { notifyDataSetChanged(); }
        }

        this.filtered = useFilter;
    }


    public void search(String text, boolean useTitle, boolean useDescr, boolean useKeywords,
                                boolean useValueS, boolean useHint, boolean useInfo, boolean useNote) {
        if(TextUtils.isEmpty(text)) {
            setFiltered(false, true);
            return;
        }
        setFiltered(true, false);
        items.clear();
        text = text.toUpperCase();

        StringBuilder sbSearch = new StringBuilder();

        for(URVItem item : filterSrcItems) {
            sbSearch.setLength(0);
            if(useTitle) sbSearch.append(item.getTitle()).append(" ");
            if(useDescr) sbSearch.append(item.getDescription()).append(" ");
            if(useKeywords) sbSearch.append(item.getKeywords()).append(" ");
            if(useValueS) sbSearch.append(item.getValueString()).append(" ");
            if(useHint) sbSearch.append(item.getHint()).append(" ");
            if(useInfo) sbSearch.append(item.getInfo()).append(" ");
            if(useNote) sbSearch.append(item.getNote()).append(" ");

            boolean needShow = sbSearch.toString().toUpperCase().contains(text);
            if(needShow) {
                items.add(item);
            }
        }

        notifyDataSetChanged();
    }

    public void setupDefaultCheckbox(String iconChecked, String iconUnchecked) {
        setupCheckbox(URVAdapter.COLORS_BCK[9], iconChecked,  URVAdapter.COLORS_BCK[17], iconUnchecked);
    }

    public boolean isGridMode() {
        return gridMode;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public int getRandomNum(int nFrom, int nTo) {
        return (int) Math.round( Math.random() * (nTo-1) + nFrom);
    }


    public int HexToColor(String hexColor, int defColor) {
        if(TextUtils.isEmpty(hexColor)) return defColor;
        try {
            return Color.parseColor(hexColor);
        } catch (Exception e) {
            e.printStackTrace();
            return Color.GRAY;
        }
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public String colorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }


    public void setupItemModeCheckbox(URVItem item, String key, boolean value) {
        item.setItemMode(URVConst.ItemMode.CHECKBOX);
        item.Value.setup(key, value);
        item.setChecked(value);
    }

    public void setupItemModeProperty(URVItem item) {
        item.setItemMode(URVConst.ItemMode.PROPERTY_KEY_VALUE);
    }


    /**
     *
     * @param idView
     * @param id
     * @param action
     * @return
     */
    public URVItemClicker addClickItem(int idView, int id, int action) {
        URVItemClicker ci = new URVItemClicker(idView, id, action);
        itemsClicker.add(ci);
        return ci;
    }


    public URVItemElement addCustomItem(int idView, int id, int valueType, int action) {
        URVItemElement ci = new URVItemElement(idView, id, valueType, action);
        itemsElements.add(ci);
        return ci;
    }


    private void applyEffect(View view, int defColor) {
        // Создаем нормальный фон
        view.setClickable(true);
        GradientDrawable normalState = new GradientDrawable();
        normalState.setShape(GradientDrawable.RECTANGLE);
        normalState.setCornerRadius(dpToPx(cornerRadius));
        normalState.setColor(defColor); // Color.parseColor("#222324")

        // Создаем ripple-эффект
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorStateList = ColorStateList.valueOf(Color.DKGRAY);
            RippleDrawable rippleDrawable = new RippleDrawable(
                    colorStateList,
                    normalState,
                    null // маска (если null, используется основной drawable как маска)
            );
            view.setBackground(rippleDrawable);
        } else {
            // Fallback для старых версий (как в примере 1)
            StateListDrawable selector = new StateListDrawable();
            GradientDrawable pressedState = new GradientDrawable();
            pressedState.setShape(GradientDrawable.RECTANGLE);
            pressedState.setCornerRadius(dpToPx(cornerRadius));
            pressedState.setColor(Color.LTGRAY);

            selector.addState(new int[]{android.R.attr.state_pressed}, pressedState);
            selector.addState(new int[]{}, normalState);
            view.setBackgroundDrawable(selector);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * rView.getContext().getResources().getDisplayMetrics().density);
    }

    public void setViewBackgroundColor(View v, int radius, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius); // радиус закругления в пикселях
        shape.setColor(color); // цвет фона (оранжевый)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(shape);
        } else {
            v.setBackgroundDrawable(shape);
        }
    }


    public void setColorPickerResult(int newColor, int itemIndex) {
        if(!isValidIndex(itemIndex)) return;
        final URVItem currentItem = items.get(itemIndex);
        currentItem.setMarkerColor(newColor);
        currentItem.Value.setValue(newColor);
        currentItem.setValueInt(newColor);
        notifyItemChanged(itemIndex);
        if(eventProperty != null) eventProperty.onPropertyChanges(itemIndex, -1, -1, currentItem.Value);
    }

    public boolean isValidIndex(int index) {
        return (items != null) & (items.size() > index) & (index >= 0);
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }


    public String getPreviewString(String[] array, int count, boolean addSpace, boolean asBrackets) {
        if (array == null || array.length == 0) return "";

        String ts, te;
        if(asBrackets) {
            ts = "(";
            te = ")";
        } else {
            ts = "";
            te = "";
        }

        if (array.length <= count) {
            return " " + ts + TextUtils.join(", ", array) + te;
        } else {
            String[] subArray = Arrays.copyOfRange(array, 0, count);
            return " " + ts + TextUtils.join(", ", subArray) + ", ..." + te;
        }
    }


    public void showDebugReport(boolean useAdapter, boolean useClickers, boolean useItems) {
        if(!debug) return;
        Log.d(LOGTAG, String.format("showDebugReport adapter: %s, clickers: %s, elements: %s", useAdapter, useClickers, useItems));

        if(useAdapter) {
            Log.d(LOGTAG, String.format("adapter records: %d", items.size()));
        }

        if(useClickers) {
            Log.d(LOGTAG, " ");
            Log.d(LOGTAG, String.format("clickers: %d", itemsClicker.size()));
            for (URVItemClicker clicker : itemsClicker)
                Log.d(LOGTAG, String.format(" > clicker id: %d, a: %d, v: %d", clicker.getId(), clicker.getAction(), clicker.getIdView()));

        }

        if(useItems) {
            Log.d(LOGTAG, " ");
            Log.d(LOGTAG, String.format("elements: %d", itemsElements.size()));
            for (URVItemElement element : itemsElements)
                Log.d(LOGTAG, String.format(" > element logic: %d, v-type: %d, a: %d, v: %d", element.getLogic(), element.getValueType(),
                        element.getAction(), element.getIdView()));

        }
    }







    /**
     * Holder Constructor
     */
    public class URViewHolder extends RecyclerView.ViewHolder {

        private int index;

        private final ViewGroup panelBck;
        private final View colorMarker;

        private final View imgBackground;
        private final ImageView imgBitmap;
        private final TextView imgLabel;

        private final ArrayList<View> holderClickerItems;
        private final ArrayList<TextView> holderTextViews;


        public URViewHolder(View v) {
            super(v);
            holderClickerItems = new ArrayList<View>();
            holderTextViews = new ArrayList<TextView>();

            if (resItemColorMarker != 0) {
                colorMarker = v.findViewById(resItemColorMarker);
                techViewSetup(colorMarker, true);
            } else colorMarker = null;

            if (resItemPanelBck != 0) {
                panelBck = v.findViewById(resItemPanelBck);
                techViewSetup(panelBck, true);
            } else panelBck = null;


            // Icon and Image ...........................................
            if(resItemImgBck != 0) {
                imgBackground = v.findViewById(resItemImgBck);
                techViewSetup(imgBackground, true);
            } else imgBackground = null;

            if(resItemImgLabel != 0) {
                imgLabel = v.findViewById(resItemImgLabel);
                techViewSetup(imgLabel, true);
            } else imgLabel = null;

            if(resItemImgBitmap != 0) {
                imgBitmap = v.findViewById(resItemImgBitmap);
                techViewSetup(imgBitmap, true);
            } else imgBitmap = null;


            if(itemsClicker.isEmpty()) {
                initDefaultClick(v);
            } else {
                // Scan all click elements
                // Use custom clickers
                //Log.d(LOGTAG, "clickItems count: " + clickItems.size());

                int foundClickers = 0;

                for(URVItemClicker clickItem : itemsClicker) {
                    View clickView = v.findViewById(clickItem.getIdView());

                    if(clickView != null) {
                        //Log.d(LOGTAG, " > set clicked: : " + clickItem.getId());
                        foundClickers++;
                        clickView.setTag(clickItem.getId());
                        applyEffect(clickView, Color.argb(60, 9, 9, 9));
                        holderClickerItems.add(clickView);

                        clickView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onItemClickEx(getAdapterPosition(), (int) v.getTag(), false);
                            }
                        });

                        clickView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                onItemClickEx(getAdapterPosition(), (int) view.getTag(), true);
                                return true;
                            }
                        });
                    }
                }

                if(foundClickers > 0) {
                    v.setBackground(null);
                } else {
                    initDefaultClick(v);
                }
            }

            if(!itemsElements.isEmpty()) {
                for(URVItemElement customItem : itemsElements) {
                    switch (customItem.getValueType()) {

                        case URVConst.ElementType.TEXT_LABEL:
                            TextView tvi = v.findViewById(customItem.getIdView());
                            if(tvi != null) {
                                tvi.setTag(customItem.getLogic());
                                holderTextViews.add(tvi);
                            }
                            break;

                        case URVConst.ElementType.TEXT_ICON:
                            tvi = v.findViewById(customItem.getIdView());
                            if(tvi != null) {
                                tvi.setTag(customItem.getLogic());
                                tvi.setTypeface(getIconFont());
                                holderTextViews.add(tvi);
                            }
                            break;

                        default:
                            break;
                    }
                }
                if(debug) Log.d(LOGTAG, "create holder with itemsElements: " + itemsElements.size() + ", holder: " + holderTextViews.size());
            }
        } // URViewHolder - constructor


        private void initDefaultClick(View v) {
            // Use default click/long click mode
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getAdapterPosition());
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }



        private void techViewSetup(View v, boolean modeVisible) {
            if(v == null) return;
            v.setClickable(false);
            v.setVisibility(modeVisible ? View.VISIBLE : View.GONE);
        }

        public View getColorMarker() { return colorMarker; }

        public ViewGroup getPanelBck() {
            return panelBck;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void updateSelection(boolean selected) {
            if(panelBck == null) return;
            panelBck.setBackgroundColor(selected ? colorSelected : colorNormal);
        }

        public void setTextIcon(String txtIconValue) {
            if(imgLabel != null) imgLabel.setText(txtIconValue);
        }

        public void setImageNone() {
            setViewVisible(imgBackground, false);
            setViewVisible(imgBitmap, false);
            setViewVisible(imgLabel, false);
        }

        public void setImageTextIcon(String value) {
            setViewVisible(imgBackground, true);
            setViewVisible(imgBitmap, false);
            setViewVisible(imgLabel, true);
            setTextIcon(value);
        }

        public void setImageBitmap(Bitmap value) {
            setViewVisible(imgBackground, true);
            setViewVisible(imgBitmap, true);
            setViewVisible(imgLabel, false);

            if(imgBitmap != null) imgBitmap.setImageBitmap(value);
        }


        private void setViewVisible(View v, boolean visible) {
            if(v != null) v.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    } // view holder end ......................................................................
    // ........................................................................................


}
/*
    private void launchCLickAction(final int index, final int clickAction, int idClicker, final boolean longCLick) {
        URVItem item = items.get(index);
        if(item == null) return;

        float step = item.getValueChangeStep() * (longCLick ? 10 : 1);

        switch (clickAction) {

            case URVConst.ClickerAction.VALUE_DECREASE:
                item.Value.dec(step);
                updateItemLabelAction(index);
                break;

            case URVConst.ClickerAction.VALUE_INCREASE:
                item.Value.inc(step);
                updateItemLabelAction(index);
                break;

            case URVConst.ClickerAction.SHOW_INPUT_NUMBER_DIALOG:
                NumberInputDialog nid = new NumberInputDialog(rView.getContext(), null, new NumberInputDialog.INumberInput() {
                    @Override
                    public void onNumberSet(float number) {
                        Toast.makeText(rView.getContext(), "Введено: " + number, Toast.LENGTH_SHORT).show();
                    }
                });
                nid.show();
                break;

            default:

                break;
        }
    }
*/