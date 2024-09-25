package com.sas.urvadapter;

import android.content.Context;
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


import com.sas.obd.console.Const;

import java.util.ArrayList;

public class URVAdapter extends RecyclerView.Adapter<URVAdapter.Q3ViewHolder> {

    public ArrayList<URVItem> items;
    public IURVEvents events = null;
    public URVResources ResourceItems;

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
    private int resItemImage = 0;
    private int resItemTextIcon = 0;
    private int resItemPanelBck = 0;
    private int resItemStyleBck = 0;


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



    /**
     * Constructor
     */
    public URVAdapter() {
        items = new ArrayList<URVItem>();
        ResourceItems = new URVResources();
    }


    public URVItem addItem(int id, int viewType, String title, String descr, URVAbstractCustomData customData) {
        URVItem newItem = new URVItem(id, viewType, title, descr, customData);
        addItemRaw(newItem);
        return newItem;
    }

    public URVItem addItemDefIcon(int id, int viewType, String txtIcon, String title, String descr, int act, int logic, int group, int marker, URVAbstractCustomData customData) {
        URVItem newItem = new URVItem(id, viewType, title, descr, customData);
        newItem.setTextIcon(txtIcon);
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
    public void onBindViewHolder(@NonNull Q3ViewHolder holder, int position) {
        URVItem data = items.get(position);

        switch (data.getViewType()) {

        }

        holder.updateSelection(data.isSelected());

        switch (data.getItemMode()) {
            case ITEM_MODE_CHECKBOX:
                if(data.isChecked()) {
                    data.setCustomBackgroundColor(colorBckChecked);
                    data.setTextIcon(iconCheckboxChecked);
                } else {
                    data.setCustomBackgroundColor(colorBckUnchecked);
                    data.setTextIcon(iconCheckboxUnchecked);
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

        if (TextUtils.isEmpty(data.getTextIcon())) {
            holder.getIconView().setVisibility(View.GONE);
        } else {
            holder.setTextIcon(data.getTextIcon());
            holder.getIconView().setVisibility(View.VISIBLE);
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
    public Q3ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getIdResourceByType(viewType), parent, false);
        if(isTextIcons() & (resItemTextIcon != 0)) {
            TextView tIcon = v.findViewById(resItemTextIcon);
            if(tIcon != null) {
                tIcon.setTypeface(iconFont);
            }
        }
        return new Q3ViewHolder(v, events);
    }


    private void onItemClick(int index) {
        if(events != null) {
            events.onItemClick(index);
        }
    }

    private void onItemLongClick(final int index) {
        if (events != null) {
            events.onLongClick(index);
        }
    }


    private void onSelectItem(final int index) {
        if(isMultiselect()) {
            boolean allowSelect = true;

            if(events != null) {
                allowSelect = events.onAllowSelect(index);
            }

            URVItem item = items.get(index);
            if(allowSelect) {
                item.setSelected(!item.isSelected());
            } else {
                item.setSelected(false);
            }

            notifyItemChanged(index);

            if(events != null) {
                events.onSelectionChanged();
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


    public void setupResourceHolders(int idTitle, int idDescr, int idImg, int idBck, int idTxtIcon, int idStyleBck) {
        resItemTitle = idTitle;
        resItemDescr = idDescr;
        resItemImage = idImg;
        resItemPanelBck = idBck;
        resItemTextIcon = idTxtIcon;
        resItemStyleBck = idStyleBck;
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




    public class Q3ViewHolder extends RecyclerView.ViewHolder {

        private int index;

        private final ImageView img;
        private final TextView txtIcon;

        private final TextView title;
        private final TextView descr;

        private final FrameLayout panelBck;

        private final View styleBck;


        public Q3ViewHolder(View v, final IURVEvents events) {
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

            if(resItemTitle != 0) {
                title = v.findViewById(resItemTitle);
                title.setClickable(false);
            } else title = null;

            if(resItemDescr != 0) {
                descr = v.findViewById(resItemDescr);
                descr.setClickable(false);
            } else descr = null;

            if(resItemImage != 0) {
                img = v.findViewById(resItemImage);
                img.setClickable(false);
            } else img = null;

            if(resItemTextIcon != 0) {
                txtIcon = v.findViewById(resItemTextIcon);
                txtIcon.setClickable(false);
                //txtIcon.setOnClickListener(new View.OnClickListener() {
                //    @Override
                //    public void onClick(View view) {
                //        onSelectItem(getAdapterPosition());
                //    }
                //});
            } else txtIcon = null;

            if(resItemStyleBck != 0) {
                styleBck = v.findViewById(resItemStyleBck);
                if(styleBck != null) {
                    styleBck.setClickable(false);
                }
            } else styleBck = null;

            if(resItemPanelBck != 0) {
                panelBck = v.findViewById(resItemPanelBck);
                if(panelBck != null) {
                    panelBck.setClickable(false);
                }
            } else panelBck = null;

            setTextIconMode(isTextIcons());
        }


        public TextView getIconView() {
            return txtIcon;
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
            if(txtIcon != null) {
                txtIcon.setText(txtIconValue);
            }
        }

        public void setTextIconMode(boolean textIcon) {
            if(img != null) {
                img.setVisibility(textIcon ? View.GONE : View.VISIBLE);
            }

            if(txtIcon != null) {
                txtIcon.setVisibility(textIcon ? View.VISIBLE : View.GONE);
                txtIcon.setText(defaultTextIcon);
            }
        }

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
            addItem(0, Const.ITEM_MSG_OUT, msg, "", null);
            notifyItemChanged(getItemCount() - 1);
            scrollListToBottom();
        }

        if(events != null) {
            events.onCommand(getTerminalBase(), msg);
        }

        /*
        if(useBase && !TextUtils.isEmpty(terminalBase)) {
            msg = terminalBase + " " + msg;
        }

        boolean validCommand = false;
        int iSpace = msg.indexOf(" ");
        if(iSpace >= 0) {
            final String s1 = msg.substring(0, iSpace);
            final String s2 = msg.substring(iSpace+1).trim();

            if(!TextUtils.isEmpty(s2)) {
                switch (s1) {
                    case CMD_ODB:
                        validCommand = true;
                        events.onObdCommand(s2);
                        break;

                    case CMD_APP:
                        validCommand = true;
                        events.onAppCommand(s2);
                        break;

                    case CMD_CWG:
                        validCommand = true;
                        events.onCwgCommand(s2);
                        break;

                    case CMD_ADMIN:
                        validCommand = true;
                        events.onAdminCommand(s2);
                        break;
                }
            }
        }

        if(!validCommand) {
            events.onCommonCommand(msg);
        }
      */
    }

    public String getTerminalBase() {
        return terminalBase;
    }

    public void setTerminalBase(String terminalBase) {
        this.terminalBase = terminalBase;
        lblBase.setText(this.terminalBase + baseSymbol);
    }

}
