package com.sas.urvadapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

/**
 * Утилитный класс для отображения диалога с выбором одного варианта.
 */
public final class URVSingleChoiceDialog {

    /**
     * Интерфейс обратного вызова для получения выбранного элемента.
     */
    public interface IOnSelectedOneListener {
        /**
         * Вызывается при выборе элемента.
         * @param which индекс выбранного элемента
         * @param item  текст выбранного элемента
         */
        void onItemSelected(int which, CharSequence item);
    }

    private URVSingleChoiceDialog() {
        // Приватный конструктор запрещает создание экземпляров
    }

    /**
     * Показывает диалог с выбором одного варианта. Выбор подтверждается кнопкой "OK".
     *
     * @param context      контекст (должен быть активностью)
     * @param title        заголовок диалога
     * @param items        массив строк для выбора
     * @param checkedItem  индекс выбранного по умолчанию элемента (-1, если ничего не выбрано)
     * @param listener     слушатель, вызываемый при нажатии OK
     */
    public static void showWithOk(Context context, String title, CharSequence[] items,
                                  int checkedItem, String txtOk, String txtCancel, IOnSelectedOneListener listener) {
        if (!isValidContext(context)) return;

        if(TextUtils.isEmpty(txtOk)) txtOk = "OK";
        if(TextUtils.isEmpty(txtCancel)) txtCancel = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            // При клике на элемент просто меняется выделение (стандартное поведение)
        });

        builder.setPositiveButton(txtOk, (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            CharSequence selectedItem = items[selectedPosition];
            if (listener != null) {
                listener.onItemSelected(selectedPosition, selectedItem);
            }
        });

        builder.setNegativeButton(txtCancel, null);
        builder.show();
    }

    /**
     * Показывает диалог с выбором одного варианта. Выбор происходит немедленно при клике на элемент,
     * диалог закрывается автоматически.
     *
     * @param context      контекст (должен быть активностью)
     * @param title        заголовок диалога
     * @param items        массив строк для выбора
     * @param checkedItem  индекс выбранного по умолчанию элемента (-1, если ничего не выбрано)
     * @param listener     слушатель, вызываемый при клике на элемент
     */
    public static void showImmediate(Context context, String title, CharSequence[] items,
                                     int checkedItem, IOnSelectedOneListener listener, String txtCancel) {
        if (!isValidContext(context)) return;

        if(TextUtils.isEmpty(txtCancel)) txtCancel = "Cancel";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            // При клике на элемент сразу вызываем слушатель и закрываем диалог
            if (listener != null) {
                listener.onItemSelected(which, items[which]);
            }
            dialog.dismiss();
        });

        builder.setNegativeButton(txtCancel, null);
        builder.show();
    }

    /**
     * Проверяет, можно ли безопасно показывать диалог с данным контекстом.
     * @param context контекст
     * @return true, если диалог можно показать
     */
    private static boolean isValidContext(Context context) {
        if (context == null) return false;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
