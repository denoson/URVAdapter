package com.sas.urvadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

    public ArrayList<URVItem> items;
    public ArrayList<URVItem> filterSrcItems;

    public IURVItemEvents eventsItem = null;
    public IURVTechEvents eventsTech = null;
    public IURVTerminalEvents eventsTerminal = null;

    public URVResources ResourceItems;
    public URVCounterResources ResourceCounter;

    public static final int[] COLORS_BCK = {
            0xFFB71C1C, 0xFF880E4F, 0xFF4A148C, 0xFF311B92, 0xFF1A237E,
            0xFF0D47A1, 0xFF01579B, 0xFF006064, 0xFF004D40, 0xFF1B5E20,
            0xFF33691E, 0xFF827717, 0xFFF57F17, 0xFFFF6F00, 0xFFE65100,
            0xFFBF360C, 0xFF3E2723, 0xFF212121, 0xFF263238
    };

    public static final int ITEM_MODE_NORMAL = 0;
    public static final int ITEM_MODE_CHECKBOX = 1;

    private int resItemTitle = 0;
    private int resItemDescr = 0;
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


    /**
     * Constructor
     */
    public URVAdapter() {
        items = new ArrayList<URVItem>();
        filterSrcItems = new ArrayList<URVItem>();

        ResourceItems = new URVResources();
        ResourceCounter = new URVCounterResources();
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


    public void initRecyclerView(Context ctx, RecyclerView rList) {
        rView = rList;
        rView.setLayoutManager(new LinearLayoutManager(ctx));
        rView.setItemAnimator(new DefaultItemAnimator());
        rView.setAdapter(this);
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
                //data.setCustomBackgroundColor();
                //data.setTextIcon(iconCheckboxChecked);
                break;
        }

        if (TextUtils.isEmpty(data.getTitle())) {
            holder.getTitle().setVisibility(View.GONE);
        } else {
            holder.getTitle().setVisibility(View.VISIBLE);
            holder.getTitle().setText(data.getTitle());
        }

        if (TextUtils.isEmpty(data.getDescription())) {
            holder.getDescr().setVisibility(View.GONE);
        } else {
            holder.getDescr().setVisibility(View.VISIBLE);
            holder.getDescr().setText(data.getDescription());
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


        if(ResourceCounter.isEnabled()) {
            holder.setCounterBoxVisible(data.Counter.isVisible());
            holder.setCounter(data.Counter.getCounter());
            holder.setCounterUnits(data.Counter.getUnits());
        }


        if (holder.getStyleBck() != null) {
            Drawable background = holder.getStyleBck().getBackground();
            if (data.getCustomBackgroundColor() == 0) {
                background.setTintList(null);
            } else {
                background.setTint(data.getCustomBackgroundColor());
            }
        }

    }


    @NonNull
    @Override
    public URViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getIdResourceByType(viewType), parent, false);
        if(isTextIcons() & (resItemImgLabel != 0)) {
            TextView tIcon = v.findViewById(resItemImgLabel);
            if(tIcon != null) {
                tIcon.setTypeface(iconFont);
            }
        }
        return new URViewHolder(v);
    }


    private void onItemClick(int index) {
        if(eventsItem != null) {
            eventsItem.onItemClick(index);
        }
    }

    private void onItemLongClick(final int index) {
        if (eventsItem != null) {
            eventsItem.onLongClick(index);
        }
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


    public void setupResourceHolders(int idTitle, int idDescr, int idBck, int idStyleBck) {
        resItemTitle = idTitle;
        resItemDescr = idDescr;
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

        private final TextView title;
        private final TextView descr;
        private final FrameLayout panelBck;
        private final View styleBck;

        private final View counterPanel;
        private final TextView counterValue;
        private final TextView counterUnits;

        private final View imgBackground;
        private final ImageView imgBitmap;
        private final TextView imgLabel;


        public URViewHolder(View v) {
            super(v);

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

            if (resItemTitle != 0) {
                title = v.findViewById(resItemTitle);
                techViewSetup(title, true);
            } else title = null;

            if (resItemDescr != 0) {
                descr = v.findViewById(resItemDescr);
                techViewSetup(descr, true);
            } else descr = null;

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




            // Counter .................................................
            if (ResourceCounter.useBox()) {
                counterPanel = v.findViewById(ResourceCounter.getBox());
                techViewSetup(counterPanel, ResourceCounter.isEnabled());
            } else counterPanel = null;

            if (ResourceCounter.useCounter()) {
                counterValue = v.findViewById(ResourceCounter.getCounter());
                techViewSetup(counterValue, true);
            } else counterValue = null;

            if (ResourceCounter.useUnits()) {
                counterUnits = v.findViewById(ResourceCounter.getUnits());
                techViewSetup(counterUnits, ResourceCounter.isVisibleUnits());
            } else counterUnits = null;

        }


        private void techViewSetup(View v, boolean modeVisible) {
            if(v != null) {
                v.setClickable(false);
                v.setVisibility(modeVisible ? View.VISIBLE : View.GONE);
            }
        }


        public TextView getTitle() {
            return title;
        }

        public TextView getDescr() {
            return descr;
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



        public void setCounterBoxVisible(boolean visible) {
            setViewVisible(counterPanel, visible);
        }

        public String getCounter() {
            return counterValue.getText().toString();
        }
        public void setCounter(String value) {
            counterValue.setText(value);
        }

        public String getCounterUnits() {
            return counterUnits.getText().toString();
        }
        public void setCounterUnits(String value) {
            counterUnits.setText(value);
        }


        private void setViewVisible(View v, boolean visible) {
            if(v != null) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
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
}
