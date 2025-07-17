package com.sas.urvadapter;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Date: 2024.09.25
 * Author: Den Vigovski
 * Universal RecycleView adapter
 * Licence: This is an experimental project for educational purposes. Not for commercial use.
 */


public class URVAdapter extends RecyclerView.Adapter<URVAdapter.URViewHolder> {

    public static final int VERSION = 1;
    public final String LOGTAG = "URVAdapter";

    public final URVProperties Properties; // visible for title, descr, autohide
    public ArrayList<URVItem> items;
    public ArrayList<URVItem> filterSrcItems;

    public IURVItemEvents eventsItem = null;
    public IURVTechEvents eventsTech = null;
    public IURVTerminalEvents eventsTerminal = null;

    public URVResources ResourceItems;  // resources id00 .. id09
    public URVCounterResources ResourceCounter; // enabled, id: box, counter, unit

    public final ArrayList<URVClickItem> itemsClicker;
    public final ArrayList<URVItemElement> itemsElements;


    public static final int[] COLORS_BCK = {
            0xFFB71C1C, 0xFF880E4F, 0xFF4A148C, 0xFF311B92, 0xFF1A237E,
            0xFF0D47A1, 0xFF01579B, 0xFF006064, 0xFF004D40, 0xFF1B5E20,
            0xFF33691E, 0xFF827717, 0xFFF57F17, 0xFFFF6F00, 0xFFE65100,
            0xFFBF360C, 0xFF3E2723, 0xFF212121, 0xFF263238
    };


    public static final int ITEM_MODE_NORMAL = 0;
    public static final int ITEM_MODE_CHECKBOX = 1;

    private int resItemPanelBck = 0;
    private int resItemStyleBck = 0;

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


    /**
     * Constructor
     */
    public URVAdapter() {
        items = new ArrayList<URVItem>();
        filterSrcItems = new ArrayList<URVItem>();
        itemsClicker = new ArrayList<URVClickItem>();
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
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL);

    }

    public void initList002() {
        ResourceItems.setId00(R.layout.urv_list_item_002);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.lblCounter, URVConst.Logic.COUNTER_VALUE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.lblCounterUnits, URVConst.Logic.COUNTER_UNITS, URVConst.ElementType.TEXT_LABEL);
    }


    public void initList003() {
        resItemStyleBck = R.id.pnlColor;
        ResourceItems.setId00(R.layout.urv_list_item_003);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.title, URVConst.Logic.TITLE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.descr, URVConst.Logic.DESCR, URVConst.ElementType.TEXT_LABEL);
    }


    public void initList004() {
        ResourceItems.setId00(R.layout.urv_list_item_004);
        itemsClicker.clear();
        itemsElements.clear();
        addItemElement(R.id.lbl11, URVConst.Logic.COL1_VALUE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.lbl12, URVConst.Logic.COL1_LABEL, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.lbl21, URVConst.Logic.COL2_VALUE, URVConst.ElementType.TEXT_LABEL);
        addItemElement(R.id.lbl22, URVConst.Logic.COL2_LABEL, URVConst.ElementType.TEXT_LABEL);

        addItemElement(R.id.textIconA, URVConst.Logic.ICON_A, URVConst.ElementType.TEXT_ICON); // flash icon
        addItemElement(R.id.textIconB, URVConst.Logic.ICON_B, URVConst.ElementType.TEXT_ICON); // delete icon
        addItemElement(R.id.textIconC, URVConst.Logic.ICON_C, URVConst.ElementType.TEXT_ICON); // delete icon

        addClickItem(R.id.col1, 1);
        addClickItem(R.id.col2, 2);
        addClickItem(R.id.col3, 3);
        addClickItem(R.id.col4, 4);
        addClickItem(R.id.col5, 5);
    }


    public void addItemElement(int idResource, int id, int idType) {
        URVItemElement el = new URVItemElement(idResource, id, idType);
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

            default: return "";
        }
    }
    // new functions





    public int getDefaultLayoutListItem() {
        return gridMode ? R.layout.urv_grid_item : R.layout.urv_list_item;
    }


    public void initDefaultResources() {
        setupResourceHolders(R.id.bckPanel, R.id.msgbox);
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


    private void addItemRaw(URVItem item) {
        item.setIndex(items.size());
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
    }



    @Override
    public void onBindViewHolder(@NonNull URViewHolder holder, int position) {
        URVItem data = items.get(position);
        holder.updateSelection(data.isSelected());

        switch (data.getItemMode()) {

            case ITEM_MODE_CHECKBOX:
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

        int currVisible;

        for(TextView tvi : holder.holderTextViews) {
           tvi.setText(getLogicText((int) tvi.getTag(), data));
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

        if (holder.getStyleBck() != null) {
            setViewBackgroundColor(holder.getStyleBck(), dpToPx(cornerRadius),
                    data.getCustomBackgroundColor() == 0 ? Color.TRANSPARENT : data.getCustomBackgroundColor());
            // old way
            //Drawable background = holder.getStyleBck().getBackground();
            //background.setTint(data.getCustomBackgroundColor());
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


    private void onItemClick(int index) {
        Log.d(LOGTAG, String.format("onItemClick index: %d", index));
        if(eventsItem != null) {
            eventsItem.onItemClick(index);
        }
    }

    private void onItemLongClick(final int index) {
        Log.d(LOGTAG, String.format("onItemLongClick index: %d", index));
        if (eventsItem != null) {
            eventsItem.onLongClick(index);
        }
    }

    private void onItemClickEx(int index, int id) {
        Log.d(LOGTAG, String.format("onItemClickEx index: %d, id: %d", index, id));
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


    public void setupResourceHolders(int idBck, int idStyleBck) {
        resItemPanelBck = idBck;
        resItemStyleBck = idStyleBck;
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


    /**
     * Holder Constructor
     */
    public class URViewHolder extends RecyclerView.ViewHolder {

        private int index;

        private final FrameLayout panelBck;
        private final View styleBck;

        private final View imgBackground;
        private final ImageView imgBitmap;
        private final TextView imgLabel;

        private final ArrayList<View> holderClickerItems;
        private final ArrayList<TextView> holderTextViews;


        public URViewHolder(View v) {
            super(v);
            holderClickerItems = new ArrayList<View>();
            holderTextViews = new ArrayList<TextView>();

            if (resItemStyleBck != 0) {
                styleBck = v.findViewById(resItemStyleBck);
                techViewSetup(styleBck, true);
            } else styleBck = null;

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
            } else {
                // Scan all click elements
                // Use custom clickers
                //Log.d(LOGTAG, "clickItems count: " + clickItems.size());

                v.setBackground(null);

                for(URVClickItem clickItem : itemsClicker) {
                    View clickView = v.findViewById(clickItem.getIdView());
                    if(clickView != null) {
                        //Log.d(LOGTAG, " > set clicked: : " + clickItem.getId());
                        clickView.setTag(clickItem.getId());
                        applyEffect(clickView, Color.argb(60, 9, 9, 9));
                        holderClickerItems.add(clickView);
                        clickView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onItemClickEx(getAdapterPosition(), (int) v.getTag());
                            }
                        });
                    }
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
                Log.d(LOGTAG, "create holder with itemsElements: " + itemsElements.size() + ", holder: " + holderTextViews.size());
            }
        }


        private void techViewSetup(View v, boolean modeVisible) {
            if(v != null) {
                v.setClickable(false);
                v.setVisibility(modeVisible ? View.VISIBLE : View.GONE);
            }
        }

        public View getStyleBck() { return styleBck; }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void updateSelection(boolean selected) {
          if(panelBck == null) {
              return;
          }
          if(selected) {
              panelBck.setBackgroundColor(colorSelected);
          } else {
              panelBck.setBackgroundColor(colorNormal);
          }
        }

        public void setTextIcon(String txtIconValue) {
            if(imgLabel != null) {
                imgLabel.setText(txtIconValue);
            }
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

            if(imgBitmap != null) {
                imgBitmap.setImageBitmap(value);
            }
        }


        private void setViewVisible(View v, boolean visible) {
          if(v != null) v.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    } // view holder end .....................................


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


    public URVClickItem addClickItem(int idView, int id) {
        URVClickItem ci = new URVClickItem(idView, id);
        itemsClicker.add(ci);
        return ci;
    }

    public URVItemElement addCustomItem(int idView, int id, int valueType) {
        URVItemElement ci = new URVItemElement(idView, id, valueType);
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
}
