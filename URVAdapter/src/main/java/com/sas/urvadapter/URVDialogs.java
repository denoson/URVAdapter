package com.sas.urvadapter;

import android.content.Context;
import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class URVDialogs {


    public static void inputText(Context activityContext,
                                 String title,
                                 String hint,
                                 String initialText,
                                 String txtOK,
                                 String txtCancel,
                                 URVTextInputDialog.ICallbackInputText callback) {

        URVTextInputDialog.show(activityContext,
                title, hint,
                initialText,
                txtOK, txtCancel,
                callback);
    }


    public static void chooseOne(Context activityContext,
                                 String title,
                                 CharSequence[] items,
                                 int checkedItem,
                                 String txtOk,
                                 String txtCancel,
                                 URVSingleChoiceDialog.IOnSelectedOneListener listener) {

        URVSingleChoiceDialog.showWithOk(activityContext,
                title,
                items,
                checkedItem,
                txtOk,
                txtCancel,
                listener);
    }


    public static void chooseColor(FragmentManager fm,
                                   int initialColor,
                                   boolean enableAlpha,
                                   String txtOk,
                                   String txtCancel,
                                   String txtSelect,
                                   URVColorPickerFS.IOnColorSelectedListener listener) {

        URVColorPickerFS.show(fm,
                initialColor,
                enableAlpha,
                txtOk,
                txtCancel,
                txtSelect,
                listener);
    }


    public static void inputNumber(Context activityContext,
                                   float nMin,
                                   float nMax,
                                   boolean useFloat,
                                   boolean useNegative,
                                   float defNum,
                                   URVNumberInputDialog.INumberInput listener) {

        URVNumberDialogConfig nidCfg = new URVNumberDialogConfig();
        nidCfg.minValue = nMin;
        nidCfg.maxValue = nMax;
        nidCfg.defaultValue = defNum;
        nidCfg.useFloatNumber = useFloat;
        nidCfg.useNegative = useNegative;

        URVNumberInputDialog nid = new URVNumberInputDialog(activityContext, nidCfg, listener);
        nid.show();
    }



    public static String getColorHistory() {
        List<Integer> items = URVColorPickerFS.getHistory();
        return items == null ? "" : TextUtils.join(",", items);
    }


    public static void setColorHistory(String commaList) {
        List<Integer> result = new ArrayList<>();
        if(!TextUtils.isEmpty(commaList)) {
            String[] parts = commaList.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!TextUtils.isEmpty(trimmed)) {
                    try {
                        result.add(Integer.parseInt(trimmed));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        URVColorPickerFS.setHistory(result);
    }

}
